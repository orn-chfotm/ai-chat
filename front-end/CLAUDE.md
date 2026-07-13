# Front-end Claude Rules

이 디렉토리는 Next.js 기반 관리자 애플리케이션이다.
프론트엔드 작업 시 루트 `CLAUDE.md`를 먼저 따르고, 이 문서와 `front-end/.claude` 규칙을 추가로 따른다.

## Stack

- Next.js
- React
- Node.js LTS
- TypeScript 권장

## Product Scope

프론트엔드는 사내 규정 챗봇의 관리자 화면이다.

- 내부 규정 PDF 업로드
- 추출된 텍스트 확인 및 수정
- 수정 텍스트 기준 Vector DB 반영 요청
- default prompt 조회/수정
- 업로드/추출/적용 상태 확인
- 등록된 규정집 목록 확인
- 테스트 채팅

초기 범위에서 관리자 회원가입, 로그인, 권한 관리는 제외한다.
추후 SSO 또는 내부 사원 DB 기반 로그인으로 확장될 수 있도록 auth 영역은 확장 가능하게 남긴다.

## URL Structure

Next.js App Router 기준 권장 URL은 다음과 같다.

```text
/                         관리자 대시보드
/policies                 규정집 목록
/policies/upload          규정집 PDF 업로드
/policies/[documentId]    규정집 상세, 추출 텍스트 확인
/policies/[documentId]/edit
                          추출 텍스트 수정
/prompts                  default prompt 조회/수정
/chat-test                등록 규정 기준 테스트 채팅
/jobs/[jobId]             비동기 작업 상태 상세
```

## Directory Structure

```text
src/
  app/
    page.tsx
    policies/
    prompts/
    chat-test/
    jobs/
  components/
  features/
    policies/
    prompts/
    chat/
    jobs/
  lib/
    api/
    config/
  types/
```

## API Rules

- 백엔드 API 호출은 `lib/api` 또는 feature별 api module로 분리한다.
- 화면 컴포넌트가 fetch URL을 직접 흩뿌리지 않는다.
- 비동기 작업은 `jobId`로 polling 가능한 구조를 만든다.
- API request/response type은 백엔드 DTO 이름과 맞춘다.

## UI Rules

- 첫 화면은 관리자 대시보드이며 랜딩 페이지를 만들지 않는다.
- 로그인 없는 상태로 바로 관리자 view가 보여야 한다.
- 문서 상태, 추출 상태, Vector 반영 상태를 명확하게 표시한다.
- 추출 텍스트 수정 화면은 긴 텍스트 편집에 적합해야 한다.
- 테스트 채팅은 실제 사용자 서비스가 아니라 관리자 검증 도구로 표현한다.

