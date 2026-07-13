# PRD: Vectorization

## Objective

관리자가 검수 완료한 규정 텍스트를 chunk로 분할하고 Vector DB에 반영한다.
반영 완료 후에만 해당 버전을 `ACTIVE`로 전환한다.

## Requirements

- Vector DB 반영은 비동기 job으로 처리한다.
- Vector DB 저장 전 APP DB에 신규 version 후보를 생성한다.
- chunk는 추적 가능한 ID를 가져야 한다.
- chunk metadata에는 APP DB의 version 정보를 포함한다.
- 검색 시 APP DB에서 현재 `ACTIVE versionId`를 확인하고 Vector DB 필터로 사용한다.
- Vector DB 반영 실패 시 기존 `ACTIVE` 버전을 유지한다.
- Vector DB 제품은 추상화한다.
- 현재 채택 제품은 Elasticsearch이며, vector(dense_vector) 지원 임베딩 모델을 사용한다. 로컬은 Docker 컨테이너, 운영은 동일 구성이되 배포 방식은 별도 결정한다.

## Chunk Metadata

```json
{
  "policyCode": "INTERNAL_POLICY",
  "documentId": "POLICY-DOC-20260710-0001",
  "versionId": "POLICY-VERSION-20260710-0001-v2",
  "chunkId": "CHUNK-POLICY-VERSION-20260710-0001-v2-0001",
  "status": "ACTIVE",
  "sectionPath": "제3장 휴가",
  "effectiveFrom": "2026-07-10"
}
```

## Metrics

- `vectorizingStartedAt`
- `vectorizingCompletedAt`
- `vectorizingDurationMs`
- `chunkCount`
- `embeddingModel`
- `vectorStoreProvider`

## Back-end Requirements

- Vector DB adapter는 `infra.vectordb`에 둔다.
- domain/service는 Vector DB 구현체가 아니라 port/interface에 의존한다.
- chunk 기록은 APP DB에도 저장해 Vector DB 데이터와 대조할 수 있어야 한다.

## Acceptance Criteria

- 신규 버전 Vector 반영 성공 후에만 `ACTIVE`가 된다.
- 기존 버전 chunk가 새 답변에 섞이지 않는다.
- 관리자는 Vector 반영 시간과 chunk 수를 확인할 수 있다.

