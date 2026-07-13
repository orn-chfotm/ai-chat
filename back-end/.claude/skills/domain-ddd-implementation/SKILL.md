---
name: domain-ddd-implementation
description: 자바 스프링 백엔드에서 DDD 도메인 모델, 값 객체, 도메인 서비스, 애그리거트, repository port를 추가하거나 변경할 때 사용한다.
when_to_use: 도메인 모듈 작업 중 entity, 값 객체, 애그리거트 경계, 도메인 서비스, repository port, policy/chat/job 도메인 로직, domain과 API/infra 독립성을 다룰 때 사용한다.
paths:
  - "back-end/**/*"
  - "src/**/*"
  - "**/*.java"
---

# 스킬: 도메인 DDD 구현

## 참조

- 규칙: `back-end/.claude/rules/domain-ddd-rules.md`

## 절차

1. 도메인 코드는 `domain` 모듈에 둔다.
2. 도메인 클래스는 비즈니스 도메인별로 묶는다.
3. 요청 DTO와 응답 DTO를 도메인 객체에 넣지 않는다.
4. Repository port는 도메인 의도를 기준으로 정의한다.
5. Repository port에 JPA 전용 타입을 노출하지 않는다.
6. 도메인 서비스에 외부 시스템 구현 세부사항을 넣지 않는다.

## 체크리스트

- 도메인이 API DTO에 의존하지 않는다.
- 도메인이 `infra`에 의존하지 않는다.
- Repository port 이름이 도메인 의도를 표현한다.
- 도메인 서비스에 persistence 구현 세부사항이 없다.
