# PRD: Async Chat API

## Objective

프론트엔드가 질문을 제출하면 백엔드가 비동기 job으로 RAG 기반 답변을 생성한다.
프론트엔드는 job 상태를 지속 확인하고, 답변 생성 전까지 채팅 답변 영역에 로딩 UI를 표시한다.

## Decision

채팅은 `Async-first + polling` 방식으로 구현한다.
관리자 테스트 환경도 동일하게 비동기 방식을 사용한다.

## User Flow

```text
1. 관리자가 테스트 질문 입력
2. Front-end가 POST /api/chat/jobs 호출
3. Back-end가 jobId와 QUEUED/RUNNING 상태 반환
4. Front-end가 답변 영역에 로딩 표시
5. Front-end가 GET /api/chat/jobs/{jobId}를 polling
6. 답변 생성 완료 시 답변과 출처 표시
7. 실패 시 실패 메시지 표시
```

## Requirements

- 채팅 요청은 즉시 `jobId`를 반환한다.
- 답변 생성은 백그라운드에서 처리한다.
- 프론트엔드는 답변 생성 중 다른 작업이 가능해야 한다.
- 새로고침 전까지 관리자 테스트 대화 내역은 프론트 상태에 유지한다.
- 새로고침 후 관리자 테스트 대화 내역 유지는 필수 아님.
- 답변은 현재 `ACTIVE` 버전의 규정만 근거로 생성한다.
- 근거가 없으면 추측 답변을 하지 않는다.
- 답변에는 참조 chunk/source 정보를 포함할 수 있어야 한다.
- 응답 시간과 추론 시간을 관리자에게 제공한다.

## Recommended API

```text
POST /api/chat/jobs
GET  /api/chat/jobs/{jobId}
```

## Chat Job Status

```text
QUEUED
RETRIEVING
GENERATING
COMPLETED
FAILED
CANCELLED
```

## Metrics

- `requestedAt`
- `startedAt`
- `retrievalStartedAt`
- `retrievalCompletedAt`
- `generationStartedAt`
- `generationCompletedAt`
- `completedAt`
- `queueDurationMs`
- `retrievalDurationMs`
- `generationDurationMs`
- `totalResponseDurationMs`
- `promptTokenCount` if available
- `completionTokenCount` if available
- `retrievedChunkCount`
- `modelName`

## Front-end Requirements

- `/chat-test`에서 질문을 입력하면 즉시 사용자 메시지를 표시한다.
- 답변 메시지 영역은 job 완료 전까지 loading 상태로 표시한다.
- polling 중 사용자는 다른 관리자 화면으로 이동할 수 있어야 한다.
- 같은 화면에 머무는 동안에는 대화 내역을 유지한다.
- 완료 후 답변, 출처, 추론 시간, 전체 응답 시간을 표시한다.

## Acceptance Criteria

- 채팅 요청 API가 LLM 완료를 기다리지 않고 jobId를 반환한다.
- 프론트엔드는 job 상태를 polling한다.
- 답변 생성 전에는 로딩 UI가 표시된다.
- 답변 완료 후 추론 시간과 전체 응답 시간이 관리자에게 보인다.
- 답변은 `ACTIVE` 규정 기준으로만 생성된다.

