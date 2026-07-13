# PRD: Policy Ingestion

## Objective

관리자가 사내 규정 PDF를 업로드하고, 해당 문서를 어떤 내부 규정 코드로 관리할지 등록한다.
PDF 하나에는 휴가, 복지, 경조사 등 여러 내용이 함께 포함될 수 있으므로 시스템이 규정 종류를 고정 분리하지 않는다.

## Users

- 관리자

## Requirements

- 관리자는 PDF 파일을 업로드할 수 있다.
- 관리자는 업로드 시 관리 코드를 입력하거나 선택할 수 있다.
- 관리 코드는 운영자가 정의하는 문자열이며, 예시는 `INTERNAL_POLICY`, `HR_POLICY`, `COMPANY_RULES`처럼 확장 가능해야 한다.
- 업로드된 파일은 APP DB에 원본 파일 정보와 함께 기록된다.
- 업로드 직후 문서 상태는 `UPLOADED` 또는 `EXTRACTING`으로 전환된다.
- 업로드 후 텍스트 변환은 비동기 job으로 처리한다.
- 업로드 파일과 이후 버전 이력은 삭제보다 보관을 우선한다.

## Out Of Scope

- 초기 버전에서 자동 규정 분류는 필수 기능이 아니다.
- 초기 버전에서 관리자 로그인/권한 검증은 제외한다.

## Back-end Requirements

- `@RestController` 기반 API만 제공한다.
- 업로드 API는 즉시 `jobId`, `documentId`, `status`를 반환한다.
- 파일 저장소 구현은 infra 영역으로 분리한다.
- APP DB에는 원본 파일명, 저장 경로 또는 object key, content type, size, checksum, 등록 코드, 상태를 저장한다.

## Front-end Requirements

- `/policies/upload`에서 PDF 업로드와 관리 코드 입력을 제공한다.
- 업로드 성공 후 job 상태 화면 또는 문서 상세 화면으로 이동할 수 있다.
- 변환 중 상태를 사용자에게 표시한다.

## Acceptance Criteria

- PDF 업로드 시 API가 blocking되지 않고 job 정보를 반환한다.
- 업로드된 문서는 목록에서 상태와 함께 확인된다.
- 관리자가 입력한 코드가 문서 metadata로 저장된다.

