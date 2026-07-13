# PRD: PDF Text Extraction And Review

## Objective

업로드된 PDF를 텍스트로 변환하고, 관리자가 변환 결과를 확인/수정한 뒤 적용할 수 있게 한다.

## Requirements

- PDF 텍스트 변환은 비동기 job으로 실행한다.
- 변환 중 상태는 `EXTRACTING`으로 표시한다.
- 변환 완료 후 상태는 `REVIEWING`으로 표시한다.
- 관리자는 추출된 텍스트를 화면에서 확인할 수 있다.
- 관리자는 오탈자, 줄바꿈, 표 변환 오류 등 텍스트 변환 결과를 수정할 수 있다.
- Vector DB 반영은 관리자 수정 텍스트를 기준으로 한다.
- 변환 실패 시 `FAILED` 상태와 실패 사유를 저장한다.
- 변환 시간은 job metric으로 저장한다.

## Metrics

- `extractionStartedAt`
- `extractionCompletedAt`
- `extractionDurationMs`
- `extractedCharacterCount`
- `extractedPageCount`
- `failureReason`

## Back-end Requirements

- PDF 변환 구현은 infra 영역에 둔다.
- 변환 결과 원문과 관리자 수정본은 분리 저장한다.
- 텍스트 저장은 긴 문서 편집을 고려해 설계한다.
- 변환 job 상태 조회 API를 제공한다.

## Front-end Requirements

- `/policies/[documentId]`에서 추출 텍스트와 상태를 확인한다.
- `/policies/[documentId]/edit`에서 텍스트를 수정한다.
- 변환 중에는 진행 상태와 로딩 UI를 표시한다.
- 변환 완료 후 관리자가 적용하기 전까지는 답변 기준에 반영되지 않음을 표시한다.

## Acceptance Criteria

- PDF 업로드 후 변환 중/확인 중 상태가 구분된다.
- 관리자가 수정한 텍스트가 저장된다.
- 적용 요청 전까지 기존 챗봇 답변 기준은 변경되지 않는다.
- 관리자는 텍스트 변환 소요 시간을 확인할 수 있다.

