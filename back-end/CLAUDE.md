# Back-end Claude 규칙

## 아키텍처 가이드

우선 기준 문서: `docs/backend-architecture-guidelines.md`.
책임별 상시 규칙은 `.claude/rules/` 아래에 둔다.
책임별 작업 절차는 `.claude/skills/` 아래에 둔다.

이 디렉터리는 Java/Spring 기반 API 서버다.
Back-end 작업 시 루트 `CLAUDE.md`를 먼저 따르고, 이 문서와 `back-end/.claude` 규칙을 추가로 따른다.

## 기술 스택

- Java 21
- Spring Boot 3.4.x 또는 Spring AI 호환 최신 안정 버전
- Spring AI
- Spring Data JPA
- REST API only
- APP DB: PostgreSQL
- AI 모델: Ollama `qwen3:8b`
- Vector DB: Elasticsearch
- Vector 검색은 Elasticsearch의 vector 검색 기능을 사용한다.
- 로컬 개발은 PostgreSQL과 Elasticsearch를 Docker container로 실행한다.
- 운영 배포 방식은 아직 확정하지 않았지만, 저장소와 검색 엔진은 PostgreSQL/Elasticsearch 조합을 기준으로 설계한다.

## 아키텍처

Back-end는 Gradle multi-project 구조다 (`gradle-module-boundary-rules.md` 기준).
각 모듈은 `<module>/src/main/java/com/learn/chatai/...` 아래에 소스를 둔다.

```text
back-end/
  settings.gradle          (core, domain, infra, api-admin, api-member 등록)
  build.gradle             (subprojects 공통 설정: toolchain, lombok, dependencyManagement)
  core/                    (공통 응답, 공통 예외, base entity, 공통 설정)
  domain/                  (DDD 도메인 모델, 도메인 서비스, repository port)
  infra/                   (persistence adapter, Elasticsearch adapter, AI adapter)
  api-admin/               (관리자 API application — 현재 유일한 실행 가능 앱, ChatAiApplication 포함)
  api-member/              (사용자 API 모듈 — 현재 placeholder, 컨트롤러 없음, Boot plugin 미적용)
```

`domain`의 각 도메인 aggregate 디렉터리(`policy/`, `job/`, ...)는 역할별 하위 패키지로 정리한다.

```text
domain/src/main/java/com/learn/chatai/domain/policy/
  entity/       PolicyDocument, PolicyVersion, PolicyTextRevision (JPA entity)
  enums/        PolicyLifecycleStatus, PolicyTextRevisionType
  port/         PolicyDocumentRepository 등 repository port, FileStoragePort, PdfTextExtractorPort, PolicyVectorStorePort
  service/      PolicyIngestionService, PolicyExtractionService, PolicyIdGenerator
  exception/    PolicyErrorCode, PolicyException (core.exception.CustomException 상속)
```

`core`의 공통 예외/응답 구조:

```text
core/src/main/java/com/learn/chatai/core/
  exception/    ErrorCode (interface), CustomException
  response/     SuccessResponse<T>, FailResponse
```

`SuccessResponse`/`FailResponse`는 의도적으로 공통 상위 interface(`ApiResponse` 같은)를 두지 않는다.
Controller 메서드는 항상 `ResponseEntity<SuccessResponse<{Domain}ResponseDto>>`를 반환 타입으로 명시하고,
실패 응답(`FailResponse`)은 `GlobalExceptionHandler`(별도 반환 타입)에서만 만들어진다.
공통 interface로 묶으면 성공 경로 메서드가 타입상 `FailResponse`도 반환할 수 있게 되므로 이를 막기 위함이다.

도메인별 예외는 `core.exception.CustomException`을 상속하고, 도메인별 `ErrorCode` enum(`PolicyErrorCode`, `JobErrorCode` 등)을 가진다.
`GlobalExceptionHandler`는 `CustomException`을 공통으로 처리하고, validation 예외와 예상하지 못한 예외는 별도 핸들러로 처리한다.

`api-admin`의 API 계층은 도메인 aggregate별로 컨트롤러/DTO를 묶는다.

```text
api-admin/src/main/java/com/learn/chatai/api/admin/policy/
  PolicyController.java
  dto/
    PolicyUploadResponseDto.java
    PolicyDocumentResponseDto.java
```

## 계층 규칙

- `Controller`는 반드시 `@RestController`를 사용하고, `api-admin`/`api-member` 모듈에만 둔다. `domain` 모듈에는 컨트롤러를 두지 않는다.
- Controller 반환 타입은 `ResponseEntity<SuccessResponse<{Domain}ResponseDto>>`로 통일한다.
- Controller는 request DTO를 받고 response DTO를 반환한다.
- Entity와 DTO는 분리한다.
- Request/response DTO를 명확히 분리하고, DTO는 컨트롤러가 속한 API 모듈(`api-admin`/`api-member`)에 둔다.
- Service는 application/business 흐름을 담당하고 `domain.<aggregate>.service`에 둔다.
- Repository port는 `domain.<aggregate>.port`, 구현체는 `infra.persistence` 아래에 둔다.
- AI 통신은 `infra.ai` adapter로 분리한다.
- Elasticsearch vector 검색 연동은 `infra.vectordb` adapter로 분리한다.
- Service는 repository port 또는 adapter를 DI 받아 처리한다.
- Service는 Spring Data JPA interface를 직접 사용하지 않는다.
- API layer는 DB, Elasticsearch, AI 구현체를 직접 알면 안 된다.
- 도메인 전용 예외는 `domain.<aggregate>.exception`에 두고 `core.exception.CustomException`을 상속한다.

## 비동기 규칙

채팅, 규정 업로드, 텍스트 추출, Elasticsearch vector 반영은 비동기 처리를 전제로 설계한다.

- Chat API는 `jobId` 기반 비동기 응답 또는 streaming/async 응답 구조를 고려한다.
- 문서 업로드는 즉시 `jobId`와 상태를 반환한다.
- 상태 예시: `UPLOADED`, `EXTRACTING`, `REVIEWING`, `EDITING`, `READY_TO_APPLY`, `VECTORIZING`, `ACTIVE`, `ARCHIVED`, `FAILED`
- Front-end는 상태 polling 또는 추후 SSE/WebSocket으로 상태를 확인할 수 있어야 한다.

## 프롬프트 규칙

- Default prompt는 프로젝트 내부 markdown 파일로 관리한다.
- Spring AI 설정에서 prompt markdown을 읽어 system prompt로 주입한다.
- Prompt에는 회사 내부 규정 근거가 없으면 답변하지 않는 규칙을 포함한다.

권장 경로:

```text
api-admin/src/main/resources/prompts/default-chat-system-prompt.md
```

## API 범위

- 규정 문서 업로드
- PDF 텍스트 추출 상태 조회
- 추출 텍스트 조회/수정
- 수정 텍스트 Elasticsearch vector 반영
- 규정 문서 목록/상태 조회
- 기본 prompt 조회/수정
- 테스트 채팅 요청/응답 조회
