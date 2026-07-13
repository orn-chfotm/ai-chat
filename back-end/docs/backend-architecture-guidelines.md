# Back-end 아키텍처 가이드

이 문서는 back-end 구현 시 우선 적용할 기술/구조 기준을 정리한다.

## 기본 원칙

- Back-end는 view를 제공하지 않고 REST API만 제공한다.
- 모든 Controller는 `@RestController`로만 선언한다.
- API 문서화는 Swagger/OpenAPI를 사용한다.
- Controller, DTO, Entity에는 Swagger 문서화를 위한 `@Tag`, `@Schema`, `@Operation` 설명을 작성한다.
- Java 코드는 camelCase를 사용한다.
- 각 영역의 책임을 명확히 분리하고, 모듈 간 의존 방향을 통제한다.

## 확정 기술 구성

- APP DB는 PostgreSQL을 사용한다.
- AI 모델은 Ollama `qwen3:8b`를 사용한다.
- Vector DB는 Elasticsearch를 사용한다.
- RAG 검색은 Elasticsearch vector 검색 결과를 context로 주입한다.
- PostgreSQL 연동은 `infra.persistence`에서 처리한다.
- Elasticsearch 연동은 `infra.vectordb`에서 처리한다.
- Ollama/Spring AI 연동은 `infra.ai`에서 처리한다.
- Service와 API layer는 PostgreSQL, Elasticsearch, Ollama 구현 세부사항을 직접 알면 안 된다.

## 멀티 프로젝트 구조

Back-end는 Gradle multi-project 구조를 기준으로 구성한다.

```text
back-end/
  settings.gradle
  build.gradle
  core/
  domain/
  infra/
  api-member/
  api-admin/
```

권장 책임은 다음과 같다.

- `core`: 공통 응답, 공통 예외, 공통 설정, base entity, 유틸리티
- `domain`: DDD 기반 도메인 모델, 도메인 서비스, repository port
- `infra`: 외부 시스템 연동, persistence adapter, JPA repository 구현체, Elasticsearch adapter, AI adapter
- `api-member`: 사용자 API application, 사용자 API controller/request/response DTO
- `api-admin`: 관리자 API application, 관리자 API controller/request/response DTO

Gradle build 시 `api-member`, `api-admin`이 필요한 하위 프로젝트를 dependency로 주입받아 각각 빌드될 수 있어야 한다.

예상 의존 방향:

```text
api-member -> core, domain, infra
api-admin  -> core, domain, infra
infra      -> core, domain
domain     -> core
core       -> no project dependency
```

`domain`은 `infra`를 의존하지 않는다.

## API 분리

사용자 API와 관리자 API는 별도 프로젝트와 URL prefix로 분리한다.

- 사용자 API: `api-member`, `/api-member/**`
- 관리자 API: `api-admin`, `/api-admin/**`

Controller는 각 API 프로젝트에 위치한다. 도메인 내부에 Controller를 두지 않는다.

## DDD Domain 구조

`domain`은 DDD 형식을 따른다.

- Entity, Value Object, Domain Service, Repository Port를 도메인별 패키지로 분리한다.
- 도메인 패키지 내부는 역할별로 `entity/`, `enums/`, `port/`, `service/`, `exception/` 하위 패키지로 나눈다.
- 도메인 객체는 외부 API request/response DTO에 의존하지 않는다.
- 도메인 repository는 interface/port로 정의하고 `port/`에 둔다.
- JPA 구현체는 `infra.persistence`에 둔다.
- 도메인 전용 예외는 `exception/`에 두고 `core.exception.CustomException`을 상속한다.

예시:

```text
domain/
  policy/
    entity/
      PolicyDocument.java
    enums/
      PolicyLifecycleStatus.java
    port/
      PolicyDocumentRepository.java
    service/
      PolicyIngestionService.java
    exception/
      PolicyErrorCode.java
      PolicyException.java

infra/
  persistence/
    PolicyDocumentRepositoryAdapter.java
    PolicyDocumentJpaRepository.java
```

## Service와 Repository 의존 규칙

- Service는 `infra.persistence`의 adapter repository를 DI 받아 사용한다.
- Service에서 JPA interface를 직접 사용하지 않는다.
- `JpaRepository`, `CrudRepository` 등 Spring Data JPA interface는 `infra.persistence` 내부 구현 세부사항으로 취급한다.
- API layer가 JPA repository, EntityManager, DB connection, Vector DB 구현체를 직접 알면 안 된다.

금지 예시:

```java
@Service
public class PolicyService {
    private final PolicyDocumentJpaRepository repository;
}
```

허용 예시:

```java
@Service
public class PolicyService {
    private final PolicyDocumentRepository repository;
}
```

## DTO 규칙

Entity와 DTO는 분리한다.

- Request DTO는 각 API 프로젝트에서 도메인별로 정의한다.
- Response DTO는 Controller 반환 전용으로 정의한다.
- 공통 Controller 반환 구조는 `core`의 common response를 사용한다.
- 필요 시 Response DTO에 Entity 변환 메서드를 둔다.
- 필요 시 Request DTO에 Entity 또는 command 변환 메서드를 둔다.
- Entity 자체를 Controller 응답으로 직접 반환하지 않는다.

## Exception 처리 규칙

Exception 처리는 `core`의 `ExceptionHandler`에서 공통으로 관리한다.

- 공통 예외 처리는 `@RestControllerAdvice` 기반 `ExceptionHandler`에서 담당한다.
- 비즈니스 예외는 `RuntimeException`을 상속한 `CustomException` 계열로 처리한다.
- 예외 응답의 message와 HTTP status code는 enum으로 관리한다.
- 공통 예외 enum은 `core`에 둔다.
- 도메인별 예외 코드가 필요한 경우 domain별 exception enum을 확장해서 사용할 수 있다.
- 도메인별 예외 class가 필요한 경우 `CustomException`을 상속해 확장한다.
- Controller나 service에서 HTTP status, error message 문자열을 직접 흩뿌리지 않는다.
- API 응답은 공통 error response DTO로 통일한다.

권장 구조:

```text
core/
  exception/
    CustomException.java
    ErrorCode.java
    GlobalExceptionHandler.java

domain/
  policy/
    exception/
      PolicyErrorCode.java
      PolicyException.java
```

기본 흐름:

```text
Domain/Service에서 CustomException 발생
GlobalExceptionHandler에서 CustomException 처리
ErrorCode에서 message와 HttpStatus 제공
API는 공통 error response 반환
```

공통 네이밍:

```text
{Domain}{ApiPurpose}{Request|Response}Dto
```

예시:

```text
PolicyUploadRequestDto
PolicyUploadResponseDto
PolicySearchRequestDto
PolicySearchResponseDto
JobStatusResponseDto
```

## Page 응답 규칙

다중 목록 조회는 내부적으로 Spring Data의 `Page`를 사용할 수 있다.

단, `Page` 타입을 외부 API 응답으로 직접 노출하지 않는다.

- Service 내부 또는 repository adapter에서는 `Page<T>` 사용 가능
- Controller 응답은 별도 DTO로 변환
- 페이지 메타 정보도 API 전용 DTO로 감싼다.

예시:

```text
Page<PolicyDocument> -> PolicyDocumentListResponseDto
```

응답 DTO 예시 필드:

```text
items
page
size
totalElements
totalPages
hasNext
```

## Swagger/OpenAPI 문서화

- Controller에는 `@Tag`를 작성한다.
- API method에는 `@Operation`을 작성한다.
- Request/Response DTO에는 `@Schema`를 작성한다.
- Entity에도 DB/도메인 의미 설명을 작성한다.
- 문서 설명은 API 사용자와 관리자 모두가 이해할 수 있도록 도메인 용어를 기준으로 작성한다.

## 구현 체크리스트

- Controller가 모두 `@RestController`인지 확인한다.
- `/api-member`, `/api-admin` URL prefix가 분리되어 있는지 확인한다.
- Entity를 API response로 직접 반환하지 않는지 확인한다.
- `Page`를 API response로 직접 반환하지 않는지 확인한다.
- Service가 JPA repository interface를 직접 DI 받지 않는지 확인한다.
- JPA repository가 `infra.persistence` 밖으로 노출되지 않는지 확인한다.
- DTO 네이밍이 `{Domain}{ApiPurpose}{Request|Response}Dto` 규칙을 따르는지 확인한다.
- Swagger `@Tag`, `@Schema`, `@Operation` 설명이 누락되지 않았는지 확인한다.
