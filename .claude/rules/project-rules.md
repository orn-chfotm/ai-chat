# Common Project Rules

## Grounding

- 챗봇은 회사 내부 규정 근거가 있는 답변만 생성한다.
- 답변에는 가능한 경우 참조한 규정 코드, 문서 버전, 섹션 정보를 포함할 수 있도록 백엔드 응답 모델을 설계한다.
- 근거가 부족하면 "현재 등록된 규정에서 확인할 수 없습니다"와 같은 안전 응답을 반환한다.

## Policy Version Safety

- 새 규정이 `ACTIVE`가 되면 같은 `policyCode`의 이전 `ACTIVE` 문서는 `ARCHIVED`로 전환한다.
- Vector DB 검색 조건에는 반드시 `policyCode`, `policyVersion` 또는 `documentId`, `status=ACTIVE`에 준하는 필터가 포함되어야 한다.
- 삭제보다 비활성화와 이력 보존을 우선한다.

## Naming

- 규정 코드는 대문자 snake case를 사용한다.
- request DTO는 `*Request`, response DTO는 `*Response` 접미사를 사용한다.
- 비동기 작업은 `jobId` 또는 `taskId`로 추적한다.

## Documentation

- 설계 결정은 `.claude/docs` 또는 각 프로젝트 문서에 남긴다.
- 규칙 변경 시 루트와 하위 프로젝트 규칙의 충돌 여부를 확인한다.

