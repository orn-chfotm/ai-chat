---
name: api-controller-implementation
description: 스프링 REST 컨트롤러, 사용자/관리자 API 경로, 요청 DTO, 응답 DTO, 공통 응답, API 페이지네이션을 추가하거나 변경할 때 사용한다.
when_to_use: 자바 Spring Boot API 작업 중 @RestController, /api-member, /api-admin, 요청/응답 DTO, 공통 응답 래퍼, Page-to-DTO 변환, 컨트롤러 응답 구조를 다룰 때 사용한다.
paths:
  - "back-end/**/*"
  - "src/**/*"
  - "**/*.java"
---

# 스킬: API 컨트롤러 구현

## 참조

- 규칙: `back-end/.claude/rules/api-controller-rules.md`
- DTO/page 규칙: `back-end/.claude/rules/dto-response-pagination-rules.md`
- 전체 가이드: `back-end/docs/backend-architecture-guidelines.md`

## 절차

1. 엔드포인트가 `api-member`에 속하는지 `api-admin`에 속하는지 결정한다.
2. 컨트롤러를 해당 API 모듈에 둔다.
3. `@RestController`를 사용한다.
4. 사용자 경로는 `/api-member/**`, 관리자 경로는 `/api-admin/**`를 사용한다.
5. 요청 DTO와 응답 DTO를 API 모듈에 정의한다.
6. `core`의 공통 응답 타입을 반환한다.
7. Entity는 반환 전에 response DTO로 변환한다.
8. `Page<T>`는 반환 전에 명시적인 목록 응답 DTO로 변환한다.

## 체크리스트

- 컨트롤러가 `domain`에 있지 않다.
- 컨트롤러가 entity를 반환하지 않는다.
- 컨트롤러가 `Page`를 직접 반환하지 않는다.
- DTO 이름이 `{Domain}{ApiPurpose}{Request|Response}Dto` 규칙을 따른다.
- 응답이 공통 응답 래퍼를 사용한다.
