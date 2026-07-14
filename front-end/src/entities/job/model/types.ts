/** Mirrors domain.job.enums.JobType. */
export type JobType =
  | "POLICY_UPLOAD"
  | "PDF_EXTRACTION"
  | "POLICY_VECTORIZATION"
  | "CHAT_COMPLETION"
  | "PROMPT_UPDATE";

/** Mirrors domain.job.enums.JobStatus. */
export type JobStatus =
  | "QUEUED"
  | "RUNNING"
  | "WAITING_REVIEW"
  | "COMPLETED"
  | "FAILED"
  | "CANCELLED";

export const TERMINAL_JOB_STATUSES: readonly JobStatus[] = [
  "COMPLETED",
  "FAILED",
  "CANCELLED",
];

/** Mirrors api.admin.job.dto.JobStatusResponseDto. */
export type JobStatusResponse = {
  jobId: string;
  jobType: JobType;
  targetId: string | null;
  status: JobStatus;
  createdAt: string;
  queuedAt: string | null;
  startedAt: string | null;
  completedAt: string | null;
  durationMs: number | null;
  failureReason: string | null;
  metrics: Record<string, unknown>;
};
