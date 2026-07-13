---
description: 내부 규정 문서, PDF 추출, APP DB 원장, Vector DB chunk metadata 구조를 설계하거나 수정할 때 사용한다.
---

# Skill: Policy Document Design

Claude가 내부 규정 문서, PDF 추출, Vector DB 저장 구조를 설계하거나 수정할 때 따르는 절차다.

## Steps

1. 관리자가 등록한 업로드 기준 코드를 `policyCode`로 식별한다.
2. 원본 파일 업로드 기록과 추출 텍스트 기록을 분리한다.
3. 관리자 수정 텍스트를 Vector DB 반영 기준 원문으로 사용한다.
4. chunk 생성 시 `documentId`, `versionId`, `chunkId`, `sectionPath`, `policyCode`, `status` metadata를 포함한다.
5. APP DB에서 현재 `ACTIVE versionId`를 확인한 뒤 Vector DB 검색 필터로 사용한다.
6. 답변에는 검색된 chunk의 출처를 추적할 수 있는 정보를 포함한다.

## Chunk Metadata

```json
{
  "policyCode": "INTERNAL_POLICY",
  "documentId": "POLICY-INTERNAL_POLICY-20260710-v1",
  "versionId": "POLICY-VERSION-20260710-0001-v1",
  "chunkId": "CHUNK-POLICY-VERSION-20260710-0001-v1-0001",
  "sectionPath": "제3장 휴가",
  "effectiveFrom": "2026-07-10",
  "effectiveTo": null,
  "status": "ACTIVE"
}
```

