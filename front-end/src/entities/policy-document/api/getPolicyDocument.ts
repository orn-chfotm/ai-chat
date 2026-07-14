import { apiFetch } from "@/shared/api/http";
import type { PolicyDocumentDetail } from "@/entities/policy-document/model/types";

export function getPolicyDocument(documentId: string) {
  return apiFetch<PolicyDocumentDetail>(`/api-admin/policies/${documentId}`);
}
