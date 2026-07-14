"use client";

import { Fragment } from "react";
import { JobStatusBadge } from "@/entities/job/ui/JobStatusBadge";
import { useJobStatusPolling } from "@/features/job-status-polling/model/useJobStatusPolling";

function formatMetricValue(value: unknown): string {
  return typeof value === "object" ? JSON.stringify(value) : String(value);
}

export function JobStatusView({ jobId }: { jobId: string }) {
  const { job, error } = useJobStatusPolling(jobId);

  if (error) {
    return <p className="text-sm text-red-600 dark:text-red-400">{error}</p>;
  }

  if (!job) {
    return <p className="text-zinc-600 dark:text-zinc-400">불러오는 중...</p>;
  }

  const metricEntries = Object.entries(job.metrics);

  return (
    <div className="flex flex-col gap-6">
      <div className="flex items-center gap-3">
        <h1 className="text-2xl font-semibold">{job.jobId}</h1>
        <JobStatusBadge status={job.status} />
      </div>

      <dl className="grid grid-cols-[auto_1fr] gap-x-4 gap-y-2 text-sm">
        <dt className="text-zinc-500">종류</dt>
        <dd>{job.jobType}</dd>
        <dt className="text-zinc-500">대상</dt>
        <dd>{job.targetId ?? "-"}</dd>
        <dt className="text-zinc-500">생성 시각</dt>
        <dd>{new Date(job.createdAt).toLocaleString("ko-KR")}</dd>
        <dt className="text-zinc-500">시작 시각</dt>
        <dd>{job.startedAt ? new Date(job.startedAt).toLocaleString("ko-KR") : "-"}</dd>
        <dt className="text-zinc-500">완료 시각</dt>
        <dd>{job.completedAt ? new Date(job.completedAt).toLocaleString("ko-KR") : "-"}</dd>
        <dt className="text-zinc-500">소요 시간</dt>
        <dd>{job.durationMs !== null ? `${job.durationMs}ms` : "-"}</dd>
      </dl>

      {job.status === "FAILED" && job.failureReason && (
        <p className="text-sm text-red-600 dark:text-red-400">실패 사유: {job.failureReason}</p>
      )}

      {metricEntries.length > 0 && (
        <section className="flex flex-col gap-2">
          <h2 className="text-lg font-medium">처리 지표</h2>
          <dl className="grid grid-cols-[auto_1fr] gap-x-4 gap-y-2 text-sm">
            {metricEntries.map(([key, value]) => (
              <Fragment key={key}>
                <dt className="text-zinc-500">{key}</dt>
                <dd>{formatMetricValue(value)}</dd>
              </Fragment>
            ))}
          </dl>
        </section>
      )}

      {job.status !== "COMPLETED" && job.status !== "FAILED" && job.status !== "CANCELLED" && (
        <p className="text-xs text-zinc-500">진행 상태를 자동으로 갱신하고 있습니다...</p>
      )}
    </div>
  );
}
