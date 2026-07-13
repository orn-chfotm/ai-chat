# PRD: Admin Next.js UI

## Objective

Next.js 기반 관리자 화면을 제공한다.
초기 버전에서는 로그인 없이 바로 관리자 view가 보인다.

## URL Structure

```text
/                         관리자 대시보드
/policies                 규정 문서 목록
/policies/upload          PDF 업로드
/policies/[documentId]    문서 상세, 추출 텍스트 확인, version 이력
/policies/[documentId]/edit
                          추출 텍스트 수정
/prompts                  default prompt 관리
/chat-test                테스트 채팅
/jobs/[jobId]             비동기 작업 상태 상세
```

## Requirements

- 랜딩 페이지가 아니라 관리자 대시보드를 첫 화면으로 제공한다.
- API base URL은 환경 변수로 관리한다.
- 문서 상태, job 상태, 처리 시간을 확인할 수 있어야 한다.
- PDF 업로드 후 변환 상태를 표시한다.
- 텍스트 검수/수정 화면을 제공한다.
- 적용 버튼은 신규 버전 생성과 Vector 반영을 요청한다.
- 테스트 채팅은 비동기 job 기반이다.
- 답변 생성 중에는 로딩 UI를 표시한다.
- 새로고침 전까지 테스트 대화 내역을 유지한다.
- 관리자 테스트 대화 내역의 영구 저장은 필수 아님.

## Admin Metrics Display

- PDF 텍스트 변환 시간
- Vector DB 반영 시간
- Chat retrieval 시간
- Chat LLM generation 추론 시간
- Chat 전체 응답 시간
- 처리 실패 사유

## Acceptance Criteria

- 관리자가 주요 작업 상태를 화면에서 파악할 수 있다.
- 답변 생성 중에도 UI가 blocking되지 않는다.
- 로그인 없이 바로 관리자 화면에 접근된다.

