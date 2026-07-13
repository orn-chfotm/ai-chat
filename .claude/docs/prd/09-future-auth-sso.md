# PRD: Future Auth And SSO

## Objective

초기 버전에서는 로그인/회원가입을 제외한다.
다만 추후 SSO 또는 내부 사원 DB 기반 인증으로 확장 가능하도록 구조를 막지 않는다.

## Current Scope

- 로그인 화면 없음
- 회원가입 없음
- 관리자 권한 체크 없음
- 앱 진입 시 바로 관리자 대시보드 표시

## Future Considerations

- SSO 연동
- 내부 사원 DB 조회 기반 로그인
- 관리자 권한 role
- prompt 수정 권한
- 규정 적용 권한
- 감사 로그의 actor 저장

## Design Rules

- 초기 구현에서 auth 코드를 화면 전체에 흩뿌리지 않는다.
- API request model에는 추후 actor 정보를 추가할 수 있도록 확장성을 고려한다.
- job과 변경 이력에는 `createdBy`, `updatedBy`, `approvedBy` 같은 필드를 nullable로 둘 수 있다.

## Acceptance Criteria

- 초기 버전에서 로그인 없이 관리자 화면 접근이 가능하다.
- 추후 auth 도입 시 주요 feature 구조를 크게 바꾸지 않아도 된다.

