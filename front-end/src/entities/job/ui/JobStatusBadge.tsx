import type { JobStatus } from "@/entities/job/model/types";

const STATUS_LABEL: Record<JobStatus, string> = {
  QUEUED: "대기 중",
  RUNNING: "처리 중",
  WAITING_REVIEW: "검수 대기",
  COMPLETED: "완료",
  FAILED: "실패",
  CANCELLED: "취소됨",
};

const STATUS_COLOR: Record<JobStatus, string> = {
  QUEUED: "bg-zinc-100 text-zinc-700 dark:bg-zinc-800 dark:text-zinc-300",
  RUNNING: "bg-amber-100 text-amber-800 dark:bg-amber-900 dark:text-amber-200",
  WAITING_REVIEW: "bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200",
  COMPLETED: "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200",
  FAILED: "bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200",
  CANCELLED: "bg-zinc-100 text-zinc-500 dark:bg-zinc-800 dark:text-zinc-400",
};

export function JobStatusBadge({ status }: { status: JobStatus }) {
  return (
    <span
      className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${STATUS_COLOR[status]}`}
    >
      {STATUS_LABEL[status]}
    </span>
  );
}
