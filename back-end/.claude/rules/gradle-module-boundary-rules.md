# Gradle 모듈 경계 규칙

이 규칙은 back-end Gradle multi-project 구조에 적용한다.

## 모듈

- Back-end는 Gradle multi-project 모듈 구조를 사용한다.
- 기본 모듈은 `core`, `domain`, `infra`, `api-member`, `api-admin`이다.
- `core`는 공통 응답 타입, 공통 예외, `CustomException`, 공통 error enum, `ExceptionHandler`, base entity, 공통 설정을 담당한다.
- `domain`은 DDD 도메인 모델, value object, domain service, repository port를 담당한다.
- `infra`는 persistence adapter, Elasticsearch adapter, AI adapter, 외부 시스템 구현체를 담당한다.
- `api-member`는 사용자 API application code, controller, API 전용 DTO를 담당한다.
- `api-admin`은 관리자 API application code, controller, API 전용 DTO를 담당한다.

## 의존 방향

허용되는 의존 방향:

```text
api-member -> core, domain, infra
api-admin  -> core, domain, infra
infra      -> core, domain
domain     -> core
core       -> no project dependency
```

- `domain`은 `infra`에 의존하지 않는다.
- API 모듈은 JPA repository interface에 직접 의존하지 않는다.
- 공유 코드는 두 개 이상의 모듈에서 필요할 때만 `core`로 이동한다.
