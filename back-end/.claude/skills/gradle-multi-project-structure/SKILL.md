---
name: gradle-multi-project-structure
description: 백엔드 Gradle 멀티 프로젝트 구조, 모듈 의존성, api-member/api-admin 애플리케이션, core/domain/infra 모듈 경계를 만들거나 변경할 때 사용한다.
when_to_use: 하위 프로젝트 생성, 의존 방향, 모듈 분리, api-member/api-admin 빌드 구성, 공유 모듈 추출, settings.gradle, build.gradle 변경 작업에 사용한다.
paths:
  - "back-end/**/*"
  - "settings.gradle"
  - "build.gradle"
  - "**/*.gradle"
---

# 스킬: Gradle 멀티 프로젝트 구조

## 참조

- 규칙: `back-end/.claude/rules/gradle-module-boundary-rules.md`
- 전체 가이드: `back-end/docs/backend-architecture-guidelines.md`

## 절차

1. 대상 책임 영역이 `core`, `domain`, `infra`, `api-member`, `api-admin` 중 무엇인지 확인한다.
2. `settings.gradle`에 모듈을 등록한다.
3. 올바른 방향으로 모듈 의존성을 설정한다.
4. `domain`이 `infra`와 독립되도록 유지한다.
5. `api-member`와 `api-admin`은 하위 모듈을 사용하는 애플리케이션 모듈로 빌드되게 구성한다.
6. 공유 코드는 여러 모듈에서 필요할 때만 `core`에 둔다.

## 체크리스트

- 의존 방향이 rule 파일과 일치한다.
- API 모듈이 JPA repository interface에 직접 의존하지 않는다.
- `domain`이 `infra`에 의존하지 않는다.
- Gradle build에서 사용자/관리자 application을 각각 조립할 수 있다.
