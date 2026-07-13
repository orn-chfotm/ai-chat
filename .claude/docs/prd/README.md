# PRD Index

이 디렉토리는 Chat AI 프로젝트의 기능별 제품 요구사항을 정의한다.
Claude가 기능을 설계하거나 구현할 때는 루트 `CLAUDE.md`, 대상 프로젝트의 `CLAUDE.md`, 그리고 관련 PRD를 함께 확인한다.

## Product Summary

사내 규정 PDF를 업로드하고, 텍스트로 변환한 뒤, 관리자가 검수/수정하여 신규 버전으로 적용한다.
적용된 최신 `ACTIVE` 버전만 Vector DB 검색과 챗봇 답변에 사용한다.
챗봇은 백엔드 API에서 비동기로 처리되며, 프론트엔드는 job 상태를 polling하여 답변 생성 여부를 확인한다.

## PRD Documents

- `01-policy-ingestion.md`: 사내 규정 PDF 업로드와 등록 코드
- `02-policy-version-management.md`: 규정 버전, ACTIVE/ARCHIVED 전환, APP DB 원장
- `03-pdf-text-extraction-review.md`: PDF 텍스트 변환, 검수, 수정
- `04-vectorization.md`: Vector DB 반영, chunk metadata, 검색 안전성
- `05-chat-api.md`: 비동기 채팅 API, polling, 답변 출처, 응답 지표
- `06-prompt-management.md`: default prompt 조회/수정
- `07-admin-nextjs-ui.md`: Next.js 관리자 화면과 URL
- `08-async-job-status.md`: 공통 비동기 job 상태와 소요 시간 측정
- `09-future-auth-sso.md`: 추후 SSO/내부 사원 DB 연동 고려사항

## Global Decisions

- 규정 문서는 `휴가`, `복지`처럼 코드가 고정 분리되지 않는다.
- PDF 하나에 여러 규정 내용이 합쳐질 수 있다.
- 업로드 기준 코드는 관리자가 등록한다.
- 새 문서는 검수 후 적용되기 전까지 답변에 사용하지 않는다.
- 새 버전 적용 성공 시 기존 `ACTIVE` 버전은 `ARCHIVED`가 된다.
- APP DB가 원장이고 Vector DB는 AI 검색용 인덱스다.
- 채팅과 문서 처리는 async job 기반이다.
- 관리자 테스트 채팅도 async job 기반이며 프론트는 polling한다.
- 관리자 화면은 새로고침 전까지만 테스트 대화 내역을 유지한다.
- 초기 범위에서 로그인/회원가입은 제외한다.

