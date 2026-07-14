/** Document Lifecycle (root CLAUDE.md). Mirrors domain.policy.enums.PolicyLifecycleStatus. */
export type PolicyLifecycleStatus =
  | "UPLOADED"
  | "EXTRACTING"
  | "REVIEWING"
  | "EDITING"
  | "READY_TO_APPLY"
  | "VECTORIZING"
  | "ACTIVE"
  | "ARCHIVED"
  | "FAILED";

/** Mirrors api.admin.policy.dto.PolicyDocumentListResponseDto.Item. */
export type PolicyDocumentSummary = {
  documentId: string;
  policyCode: string;
  originalFileName: string;
  status: PolicyLifecycleStatus;
  createdAt: string;
};

/** Mirrors api.admin.policy.dto.PolicyDocumentListResponseDto. */
export type PolicyDocumentListResponse = {
  items: PolicyDocumentSummary[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  hasNext: boolean;
};

/** Mirrors api.admin.policy.dto.PolicyDocumentResponseDto.RevisionSummary. */
export type PolicyTextRevisionSummary = {
  revisionId: string;
  revisionType: "EXTRACTED" | "EDITED";
  content: string;
  extractedPageCount: number | null;
  extractedCharacterCount: number | null;
  createdAt: string;
};

/** Mirrors api.admin.policy.dto.PolicyDocumentResponseDto. */
export type PolicyDocumentDetail = {
  documentId: string;
  policyCode: string;
  originalFileName: string;
  status: PolicyLifecycleStatus;
  createdAt: string;
  latestRevision: PolicyTextRevisionSummary | null;
};
