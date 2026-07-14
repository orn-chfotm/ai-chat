# shared

재사용 UI, API client, 유틸리티, 설정. 상위 레이어(`app`/`features`/`entities`)에 의존하지 않는다.

- `ui/`: 재사용 UI 컴포넌트
- `api/`: 백엔드 API client (fetch wrapper, job polling 공통 로직 등)
- `config/`: 환경 변수, API base URL 등 설정
- `lib/`: 범용 유틸리티
- `types/`: 여러 레이어에서 공유하는 타입

레이어 정의와 의존 방향은 `../.claude/rules/architecture-rules.md` 참고.
