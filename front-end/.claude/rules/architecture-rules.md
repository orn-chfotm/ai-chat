---
description: "관리자 페이지 프론트엔드의 프로젝트 아키텍처 규칙입니다."
---

# 프로젝트 아키텍처

## 가정

- 현재 프론트엔드 스택은 React, Next.js, TypeScript다.
- 이 프로젝트는 `src/app` 아래에서 Next.js App Router를 사용한다.
- 도메인/애플리케이션 구조는 `src` 아래에서 Feature-Sliced Design(축소판: `app`/`features`/`entities`/`shared`)을 따른다.
- 경로 alias `@/*`는 `src/*`에 매핑된다.
- 이 저장소는 사내 규정 챗봇의 **관리자 전용** 프론트엔드다. 일반 사용자 대상 서비스나 이커머스가 아니며, 로그인 없이 바로 관리자 화면이 보인다 (`front-end/CLAUDE.md`의 Product Scope, Auth 참고).
- FE는 Next.js 기본 서버(`next start`)로 실행한다. 별도 custom Node.js server(Express 등)를 앞에 두지 않는다.

이 가정이 코드, README, 패키지 메타데이터와 충돌하면 수정 전에 멈추고 충돌 내용을 드러낸다.

## 프레임워크 규칙

- 일반적인 Next.js 지식이 아니라 실제 설치된 Next.js 버전 기준으로 판단한다. `package.json`을 먼저 확인한다.
- Next.js 라우팅, 레이아웃, metadata, server/client component 동작, config, build 동작을 바꾸기 전에는 `node_modules/next/dist/docs/`의 관련 가이드를 읽는다.
- `src/app`의 기존 App Router 매핑을 우선한다 (`front-end/CLAUDE.md`의 URL Structure).
- `src/app` 밖에 별도 라우팅 규칙을 만들지 않는다.

## FSD 레이어 (축소판)

관리자 화면 규모(정책 문서, prompt, 테스트 채팅, job 상태 정도)를 고려해 `pages`, `widgets` 레이어는 지금 사용하지 않는다. `app`이 필요한 `features`/`entities`/`shared`를 직접 조합한다.

- `src/app`: Next.js 라우팅, 레이아웃, 전역 설정, 엔트리 포인트.
- `src/features`: 사용자 액션과 유스케이스 단위 기능 (예: `policy-upload`, `policy-text-edit`, `policy-vectorize-request`, `prompt-edit`, `chat-test`, `job-status-polling`).
- `src/entities`: 도메인 모델 중심 UI, 타입, 로직 (예: `policy-document`, `policy-text-revision`, `job`, `prompt`).
- `src/shared`: 재사용 UI, API client, 유틸리티, 설정.

여러 feature가 같은 큰 UI 블록을 조합해서 써야 하는 상황이 실제로 생기면 그때 `widgets`를, 화면 조합/레이아웃 분기가 복잡해지면 그때 `pages`를 단계적으로 추가한다. 미리 빈 레이어를 만들지 않는다.

## 의존 방향

허용되는 의존 방향:

```text
app      -> features, entities, shared
features -> entities, shared
entities -> shared
shared   -> 상위 레이어에 의존하지 않는다
```

국소 구현을 편하게 만들기 위해 역방향 import를 만들지 않는다.

## 변경 범위

- 요청된 동작에 필요한 레이어만 수정한다.
- 실제 호출 지점이 둘 이상 필요하거나 기존 로컬 패턴이 이미 요구할 때만 추상화를 추가한다.
- 작업이 아키텍처 정리를 명시적으로 요구하지 않는 한 FSD 레이어 사이에서 파일을 이동하지 않는다.
- 현재 변경으로 인해 사용되지 않게 된 import, 변수, 파일만 제거한다.

## 검증

- 코드 변경 시 사용자가 명시적으로 원하지 않는다고 하지 않는 한 `npm run lint`를 실행한다.
- 변경이 라우팅, 레이아웃, build 동작에 영향을 주면 가능한 경우 `npm run build`를 실행한다.
- 검증 명령을 실행할 수 없으면 명령과 정확한 이유를 보고한다.
