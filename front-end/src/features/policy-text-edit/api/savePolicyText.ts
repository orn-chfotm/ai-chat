import { apiFetch } from "@/shared/api/http";
import type { PolicyDocumentDetail } from "@/entities/policy-document/model/types";

export function savePolicyText(documentId: string, content: string) {
  return apiFetch<PolicyDocumentDetail>(`/api-admin/policies/${documentId}/text`, {
    method: "PUT",
    body: JSON.stringify({ content }),
  });
}
