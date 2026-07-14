# Review Log

- 날짜: 2026-07-14
- 작업: PRD 03 추출 텍스트 수정 API (PUT /api-admin/policies/{documentId}/text) 검증
- 작성 역할: PL
- 심각도: improvement
- 분류: error-handling / API 응답 일관성
- 위치: `back-end/core/src/main/java/com/learn/chatai/core/GlobalExceptionHandler.java`
- 내용: 요청 body가 파싱 불가(예: 잘못된 UTF-8, 깨진 JSON)일 때 `HttpMessageNotReadableException`이 전용 핸들러 없이 generic `Exception` 핸들러로 떨어져 `500 INTERNAL_ERROR`로 응답한다. 클라이언트 입력 오류이므로 `400`이 적절하다.
- 재현 방법: `PUT .../text`에 유효하지 않은 UTF-8 바이트를 body로 전송 (검증 중 Git Bash CP949 인코딩으로 우연히 재현됨).
- 영향 범위: 정상 프론트(fetch + JSON.stringify)는 항상 UTF-8을 보내므로 실사용 영향은 낮음. 다만 잘못된 클라이언트/직접 호출 시 500이 나가 원인 파악이 어렵고 서버 오류로 오인될 수 있음.
- 제안 조치: `GlobalExceptionHandler`에 `@ExceptionHandler(HttpMessageNotReadableException.class)` 추가 → `400 INVALID_REQUEST`로 매핑. (검증/제약 위반 핸들러와 동일 계열)
- 상태: open
- 사용자 승인 필요 여부: 예 (이번 PRD 03 수정 기능 범위 밖이므로 별도 작업으로 진행 여부 확인 필요)
