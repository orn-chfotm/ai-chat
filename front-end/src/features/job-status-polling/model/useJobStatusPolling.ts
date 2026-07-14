"use client";

import { useEffect, useRef, useState } from "react";
import { getJob } from "@/entities/job/api/getJob";
import { TERMINAL_JOB_STATUSES, type JobStatusResponse } from "@/entities/job/model/types";
import { ApiError } from "@/shared/api/http";

const POLL_INTERVAL_MS = 2000;

/**
 * Polls GET /api-admin/jobs/{jobId} until the job reaches a terminal status (PRD 05/08:
 * async-first + polling). Stops polling once terminal or on error so the browser tab doesn't
 * keep hitting the API after the user has their answer.
 */
export function useJobStatusPolling(jobId: string) {
  const [job, setJob] = useState<JobStatusResponse | null>(null);
  const [error, setError] = useState<string | null>(null);
  const timerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  useEffect(() => {
    let cancelled = false;

    async function poll() {
      try {
        const result = await getJob(jobId);
        if (cancelled) return;
        setJob(result);

        if (!TERMINAL_JOB_STATUSES.includes(result.status)) {
          timerRef.current = setTimeout(poll, POLL_INTERVAL_MS);
        }
      } catch (err) {
        if (cancelled) return;
        setError(err instanceof ApiError ? err.message : "Job 상태 조회 중 오류가 발생했습니다.");
      }
    }

    poll();

    return () => {
      cancelled = true;
      if (timerRef.current) clearTimeout(timerRef.current);
    };
  }, [jobId]);

  return { job, error };
}
