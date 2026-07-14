import { apiFetch } from "@/shared/api/http";

/** Mirrors api.admin.policy.dto.PolicyUploadResponseDto. */
export type PolicyUploadResponse = {
  documentId: string;
  jobId: string;
  status: string;
};

export function uploadPolicyDocument(file: File, policyCode: string) {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("policyCode", policyCode);

  return apiFetch<PolicyUploadResponse>("/api-admin/policies", {
    method: "POST",
    body: formData,
  });
}
