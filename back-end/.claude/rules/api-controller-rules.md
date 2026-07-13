# API 컨트롤러 규칙

이 규칙은 back-end API 컨트롤러 변경 작업에 적용한다.

## REST 컨트롤러

- Back-end는 REST API만 제공한다.
- 모든 컨트롤러는 `@RestController`를 사용한다.
- Back-end에는 view, template, server-side rendering 코드를 만들지 않는다.
- 컨트롤러는 request DTO를 입력으로 받고 response DTO를 반환한다.
- 컨트롤러는 entity를 직접 반환하지 않는다.

## API 분리

- 사용자 API는 `/api-member/**` URL prefix를 사용한다.
- 관리자 API는 `/api-admin/**` URL prefix를 사용한다.
- 사용자 API 컨트롤러는 member API 모듈에 둔다.
- 관리자 API 컨트롤러는 admin API 모듈에 둔다.
- domain 모듈에는 컨트롤러를 두지 않는다.

## 응답

- 컨트롤러 응답은 `core`의 공통 응답 타입을 사용한다.
- 컨트롤러 응답으로 Spring Data `Page`를 직접 노출하지 않는다.
- 컨트롤러 응답으로 JPA나 persistence 구현 세부사항을 노출하지 않는다.
