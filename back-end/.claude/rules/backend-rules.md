# Back-end 규칙

## 컨트롤러

- 모든 Controller는 `@RestController`다.
- view, template, server-side rendering 코드를 만들지 않는다.
- URL은 `/api/**` 아래에 둔다.
- request body는 `*Request`, response body는 `*Response`로 모델링한다.

## 도메인

- 규정 문서, 추출 텍스트, Vector 반영 작업, 채팅 작업은 독립 aggregate 후보로 본다.
- 상태 변경은 명시적인 method 또는 service 흐름으로 처리한다.
- 이전 규정이 답변에 섞이지 않도록 `ACTIVE`와 `ARCHIVED` 전환을 transaction 안에서 처리한다.

## 인프라

- JPA repository는 `infra.persistence`에 둔다.
- 외부 AI/Ollama 호출은 `infra.ai`에 둔다.
- Vector DB 저장/검색은 `infra.vectordb`에 둔다.
- PDF 추출 구현은 `infra.file` 또는 `infra.document`에 둔다.

## 오류 처리

- 비동기 작업 실패 시 `FAILED` 상태와 실패 사유를 저장한다.
- API 오류 응답은 일관된 response schema를 사용한다.
