# PRD: Prompt Management

## Objective

관리자가 기본 system prompt를 확인하고 수정할 수 있게 한다.
백엔드는 Spring AI 호출 시 프로젝트 내부 markdown prompt를 주입한다.

## Requirements

- default prompt는 markdown 파일로 관리한다.
- 관리자 화면에서 현재 prompt를 조회할 수 있다.
- 관리자 화면에서 prompt를 수정할 수 있다.
- prompt 변경 이력을 APP DB에 저장할 수 있도록 설계한다.
- prompt에는 내부 규정 근거가 없을 때 추측 답변을 금지하는 내용이 포함되어야 한다.

## Recommended Prompt File

```text
back-end/src/main/resources/prompts/default-chat-system-prompt.md
```

## Front-end URL

```text
/prompts
```

## Acceptance Criteria

- 관리자가 prompt를 조회/수정할 수 있다.
- 채팅 생성 시 최신 적용 prompt가 사용된다.
- prompt 변경은 코드와 설정에서 추적 가능하다.

