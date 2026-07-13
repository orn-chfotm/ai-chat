# Service Persistence 규칙

이 규칙은 service와 persistence 변경에 적용한다.

## Service 의존성

- Service는 Spring Data JPA interface를 직접 주입받지 않는다.
- Service는 `JpaRepository`, `CrudRepository`, `EntityManager`, DB connection 객체를 직접 주입받지 않는다.
- Service는 repository port 또는 persistence adapter를 DI 받아 사용한다.
- 비즈니스 흐름은 컨트롤러가 아니라 service 또는 domain service에 둔다.

## Infra Persistence

- Spring Data JPA interface는 `infra.persistence` 내부에 둔다.
- Persistence adapter는 `infra.persistence` 내부에 둔다.
- Persistence adapter는 repository port와 JPA repository 사이를 변환한다.
- API 모듈은 JPA repository interface를 알면 안 된다.
