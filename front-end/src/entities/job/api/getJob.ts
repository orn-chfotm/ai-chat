import { apiFetch } from "@/shared/api/http";
import type { JobStatusResponse } from "@/entities/job/model/types";

export function getJob(jobId: string) {
  return apiFetch<JobStatusResponse>(`/api-admin/jobs/${jobId}`);
}
