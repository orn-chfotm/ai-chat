# Domain DDD 규칙

이 규칙은 `domain` 모듈과 도메인 모델 변경에 적용한다.

## 도메인 모델

- `domain` 모듈은 DDD 형식의 도메인 구조를 사용한다.
- Entity, value object, domain service, repository port는 도메인별로 묶는다.
- 도메인 객체는 request DTO나 response DTO에 의존하지 않는다.
- 도메인 객체는 API 모듈에 의존하지 않는다.
- 도메인 객체는 `infra`에 의존하지 않는다.

## Repository Port

- 도메인/애플리케이션 서비스가 사용하는 repository interface는 domain repository port로 정의한다.
- Repository port는 도메인 의도를 표현해야 하며 JPA 구현 세부사항을 노출하지 않는다.
- Spring Data JPA interface는 `domain` 모듈에 두지 않는다.
