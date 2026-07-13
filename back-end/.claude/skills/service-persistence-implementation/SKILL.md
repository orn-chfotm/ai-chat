---
name: service-persistence-implementation
description: 서비스, repository port, persistence adapter, Spring Data JPA repository, infra.persistence 구현을 추가하거나 변경할 때 사용한다.
when_to_use: 서비스/repository DI 결정, JpaRepository 위치, persistence adapter, EntityManager 사용, repository port 구현, 서비스가 JPA interface에 직접 의존하지 않도록 하는 작업에 사용한다.
paths:
  - "back-end/**/*"
  - "src/**/*"
  - "**/*.java"
---

# 스킬: 서비스 영속성 구현

## 참조

- 규칙: `back-end/.claude/rules/service-persistence-rules.md`
- Domain 규칙: `back-end/.claude/rules/domain-ddd-rules.md`
- Exception 규칙: `back-end/.claude/rules/exception-handling-rules.md`

## 절차

1. `domain`에 repository port를 정의하거나 기존 port를 재사용한다.
2. Spring Data JPA interface는 `infra.persistence`에 둔다.
3. Domain repository port 구현체는 `infra.persistence`의 adapter로 작성한다.
4. 서비스에는 repository port 또는 adapter를 DI한다.
5. 서비스에 JPA repository를 직접 DI하지 않는다.
6. Persistence 변환은 adapter 내부에 둔다.
7. 비즈니스 예외는 `CustomException` 계열로 발생시키고, message와 HTTP status는 error enum으로 관리한다.

## 체크리스트

- 서비스가 `JpaRepository`를 주입받지 않는다.
- 서비스가 `EntityManager`를 주입받지 않는다.
- JPA interface가 `infra.persistence` 내부에 있다.
- Adapter가 persistence 세부사항을 도메인 수준 동작으로 변환한다.
- Service가 HTTP status나 error message 문자열을 직접 관리하지 않는다.
