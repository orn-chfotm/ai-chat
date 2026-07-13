# Project Design

## Summary

사내 규정 문서를 업로드, 추출, 수정, Vector DB 반영한 뒤 로컬 LLM 기반 API 챗봇이 최신 규정을 근거로 답변하는 시스템이다.

## Main Capabilities

- 내부 규정 PDF 업로드
- PDF 텍스트 추출
- 추출 텍스트 관리자 수정
- 수정된 텍스트 기준 Vector DB 반영
- 규정 문서 상태 관리
- 로컬 LLM 기반 질의응답
- 관리자 Next.js 화면
- 테스트 채팅 화면

## Boundaries

- 백엔드는 REST API만 제공한다.
- 실제 챗봇 질의응답은 백엔드 API에서 처리한다.
- 프론트엔드는 관리자 도구이며 최종 사용자용 챗봇 서비스가 아니다.
- 관리자 회원/로그인은 초기 범위에서 제외한다.

