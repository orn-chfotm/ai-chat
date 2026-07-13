@.ai-prompts/CLAUDE.md

# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

이 문서는 Claude가 이 저장소를 작업할 때 반드시 먼저 읽어야 하는 최상위 규칙이다.
하위 프로젝트를 수정할 때는 해당 디렉토리의 `CLAUDE.md`와 `.claude/rules`, `.claude/skills` 규칙을 추가로 따른다.
공통 AI 협업 워크플로우(PM/CTO/PL/PA 역할 모델, 사용자 승인 절차, 리뷰 로깅 기준)는 위 `@.ai-prompts/CLAUDE.md` import를 통해 자동으로 로드된다. 실제 구현(파일 생성/수정, 명령 실행)은 사용자 최종 승인 전까지 시작하지 않는다 — 자세한 기준은 `.ai-prompts/docs/20-ai-process/agents/03-approval-authority.md` 참고.

## Current Status

이 저장소는 아직 구현 단계 이전이다. `back-end/`와 `front-end/`에는 `CLAUDE.md`와 `.claude/` 규칙만 있고 실제 소스 코드(빌드 설정, `src/`)는 없다. 따라서 빌드/린트/테스트 명령이 아직 존재하지 않는다 — 코드가 추가되면 각 하위 프로젝트의 `CLAUDE.md`에 해당 명령을 채워 넣는다.

## Project Goal

회사의 내부 규정을 기준으로 휴가, 경조사, 보안, 연락망, 조직도 등 사내 질문에 답변하는 내부 규정 챗봇을 만든다.
PDF 하나에 여러 규정 내용이 함께 포함될 수 있으므로 규정 종류를 코드로 고정 분리하지 않는다.
업로드 기준 코드는 관리자가 등록하며, 모든 문서, 코드, API, DB 모델은 관리자 등록 코드와 버전 관리를 전제로 설계한다.

## Repository Structure

```text
chat-ai/
  CLAUDE.md
  .claude/
    rules/
    skills/
    docs/
  back-end/
    CLAUDE.md
    .claude/
      rules/
      skills/
  front-end/
    CLAUDE.md
    .claude/
      rules/
      skills/
```

## Absolute Rules

- 내부 규정 기반 답변만 허용한다.
- 근거 문서가 없거나 현재 적용 중인 최신 규정에서 확인되지 않는 내용은 추측해서 답변하지 않는다.
- 규정 문서는 `policyCode`, `policyVersion`, `effectiveFrom`, `effectiveTo`, `status`, `documentId`, `chunkId`를 추적할 수 있어야 한다.
- 신규 규정이 적용되면 이전 규정의 chunk가 답변 검색에 섞이지 않도록 상태와 유효기간을 엄격히 분리한다.
- Vector DB 저장 단위는 사람이 추적 가능한 ID를 가져야 하며, 원본 문서와 추출/수정된 텍스트의 출처를 역추적할 수 있어야 한다.
- 공통 규칙은 루트 `.claude` 아래에 둔다.
- 하위 프로젝트 작업은 각 하위 디렉토리의 `CLAUDE.md`를 기준으로 수행한다.
- 백엔드는 API만 제공하고 view를 제공하지 않는다.
- 프론트엔드는 관리자용 Next.js 애플리케이션으로 구성한다.

## Policy Code System

규정 코드는 시스템이 휴가/복지처럼 고정 분리하지 않고, 관리자가 업로드 시 등록하는 문자열로 관리한다.
코드는 APP DB에서 원장으로 관리하며, Vector DB metadata에는 APP DB의 현재 version 식별자를 함께 저장한다.

```text
INTERNAL_POLICY     사내규정
HR_POLICY           인사 관련 통합 규정
COMPANY_RULES       회사 통합 규정
```

규정 문서 ID 예시:

```text
POLICY-{policyCode}-{yyyyMMdd}-{version}
POLICY-INTERNAL_POLICY-20260710-v1
```

Vector chunk ID 예시:

```text
CHUNK-{documentId}-{sectionNo}-{chunkNo}
CHUNK-POLICY-INTERNAL_POLICY-20260710-v1-003-001
```

## Document Lifecycle

규정 문서는 다음 상태를 가진다.

```text
UPLOADED         파일 업로드 완료
EXTRACTING       PDF 텍스트 추출 중
REVIEWING        텍스트 추출 완료, 관리자 확인 중
EDITING          관리자 수정 중
READY_TO_APPLY   적용 대기
VECTORIZING      Vector DB 반영 중
ACTIVE           현재 답변에 사용 가능한 최신 규정
ARCHIVED         이전 규정, 답변 검색 제외
FAILED           처리 실패
```

`ACTIVE` 상태의 최신 규정만 챗봇 답변 검색 대상으로 사용한다.
이전 버전은 감사와 이력 확인을 위해 보관하되 기본 검색 대상에서 제외한다.

## Shared Agent Areas

- `.claude/rules`: 저장소 전체 공통 규칙
- `.claude/skills`: 문서 처리, 규정 코드화, Vector DB chunk 설계 같은 공통 작업 절차
- `.claude/docs`: 프로젝트 설계 문서와 결정 기록

## Product Requirements

@.claude/docs/prd/README.md

PRD 문서는 `.claude/docs/prd/README.md`를 기준으로 확인한다.
상세 기능 요구사항은 `.claude/docs/prd/*.md`에 기능별로 분리한다.
기능 구현 전 관련 PRD와 하위 프로젝트의 `CLAUDE.md`를 함께 확인한다.

## Recommended Stack

- Back-end: Java 21, Spring Boot 3.4.x 또는 Spring AI 호환 최신 안정 버전, Spring AI, JPA
- Front-end: Next.js, React, Node.js LTS
- LLM: Ollama `qwe3:8B`
- APP DB (원장): PostgreSQL
- Vector DB (AI 검색용): Elasticsearch, vector(dense_vector) 지원 모델 사용. 제품 교체 가능하도록 infra adapter로 격리
- 로컬 개발 환경: PostgreSQL, Elasticsearch 모두 Docker 컨테이너로 실행
- 운영 배포: 로컬과 동일한 PostgreSQL/Elasticsearch 구성을 사용하되 Docker 환경이 아닐 수 있음 (배포 방식은 추후 결정)
