# PRD: Async Job Status And Metrics

## Objective

문서 업로드, PDF 변환, Vector 반영, 채팅 답변 생성 같은 오래 걸리는 작업을 공통 job 모델로 추적한다.
관리자가 각 작업의 상태, 실패 사유, 처리 시간을 확인할 수 있게 한다.

## Job Types

```text
POLICY_UPLOAD
PDF_EXTRACTION
POLICY_VECTORIZATION
CHAT_COMPLETION
PROMPT_UPDATE
```

## Common Status

```text
QUEUED
RUNNING
WAITING_REVIEW
COMPLETED
FAILED
CANCELLED
```

## Common Fields

- `jobId`
- `jobType`
- `targetId`
- `status`
- `createdAt`
- `queuedAt`
- `startedAt`
- `completedAt`
- `durationMs`
- `failureReason`
- `createdBy`

## Detailed Metrics

작업 유형별 세부 metric은 JSON 또는 별도 테이블로 확장 가능하게 설계한다.

```json
{
  "extractionDurationMs": 12450,
  "vectorizingDurationMs": 8230,
  "retrievalDurationMs": 310,
  "generationDurationMs": 4810,
  "totalResponseDurationMs": 5460
}
```

## Requirements

- 모든 장기 작업은 job 상태 조회 API를 제공한다.
- 프론트엔드는 job 상태를 polling할 수 있다.
- 실패 시 실패 사유를 저장하고 화면에 표시한다.
- 처리 시간 metric은 관리자 화면에서 확인 가능해야 한다.
- metric은 추후 운영 모니터링으로 확장 가능해야 한다.

## Acceptance Criteria

- 관리자가 job별 상태와 소요 시간을 확인할 수 있다.
- 채팅 답변에는 추론 시간과 전체 응답 시간이 기록된다.
- PDF 변환에는 텍스트 변환 시간이 기록된다.
- Vector 반영에는 반영 시간이 기록된다.

