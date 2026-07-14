import { apiFetch } from "@/shared/api/http";
import type { PolicyDocumentListResponse } from "@/entities/policy-document/model/types";

export function getPolicyDocuments(page = 0, size = 20) {
  return apiFetch<PolicyDocumentListResponse>(
    `/api-admin/policies?page=${page}&size=${size}`,
  );
}
