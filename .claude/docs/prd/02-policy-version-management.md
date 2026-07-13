# PRD: Policy Version Management

## Objective

규정 문서의 업로드 파일, 추출 텍스트, 관리자 수정 텍스트, 적용 버전을 APP DB에서 원장으로 관리한다.
새 버전이 적용되기 전까지 챗봇은 기존 `ACTIVE` 버전을 계속 사용한다.

## Core Principle

APP DB는 기준 데이터 원장이다.
Vector DB는 AI 검색용 인덱스이며, 문서 상태와 버전의 최종 판단 근거가 아니다.

## Version Flow

```text
1. PDF 업로드
2. 텍스트 변환 중
3. 변환 완료 후 관리자 확인 중
4. 관리자 텍스트 수정
5. 관리자 적용
6. 신규 버전 생성
7. Vector DB 반영
8. 반영 성공 시 신규 버전 ACTIVE
9. 기존 ACTIVE 버전 ARCHIVED
```

## Status

```text
UPLOADED
EXTRACTING
REVIEWING
EDITING
READY_TO_APPLY
VECTORIZING
ACTIVE
ARCHIVED
FAILED
```

## Requirements

- 하나의 관리 코드에는 여러 버전이 존재할 수 있다.
- 동시에 하나의 관리 코드에는 하나의 `ACTIVE` 버전만 있어야 한다.
- 새 버전이 `ACTIVE`가 되기 전까지 기존 `ACTIVE` 버전이 계속 답변 기준이다.
- Vector DB 반영 실패 시 기존 `ACTIVE` 버전을 유지한다.
- 업로드 파일, 추출 텍스트, 수정 텍스트, 적용 버전은 추적 가능해야 한다.
- 버전 전환은 transaction 경계 안에서 처리해야 한다.

## Recommended APP DB Concepts

- `PolicyDocument`: 업로드된 문서 단위
- `PolicyVersion`: 적용 가능한 버전 단위
- `PolicyTextRevision`: 추출/수정 텍스트 이력
- `PolicyProcessingJob`: 업로드, 변환, Vector 반영 job
- `PolicyChunkRecord`: Vector DB에 저장된 chunk와 APP DB 추적 정보

## Acceptance Criteria

- 신규 버전 적용 전에는 기존 `ACTIVE` 버전이 검색된다.
- 신규 버전 적용 성공 후 기존 버전은 `ARCHIVED`가 된다.
- Vector DB 오류가 발생하면 신규 버전은 `ACTIVE`가 되지 않는다.
- 관리자는 이전 버전과 파일 이력을 조회할 수 있다.

