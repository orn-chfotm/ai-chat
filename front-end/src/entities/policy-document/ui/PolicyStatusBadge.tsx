import type { PolicyLifecycleStatus } from "@/entities/policy-document/model/types";

const STATUS_LABEL: Record<PolicyLifecycleStatus, string> = {
  UPLOADED: "업로드됨",
  EXTRACTING: "텍스트 추출 중",
  REVIEWING: "검수 대기",
  EDITING: "수정 중",
  READY_TO_APPLY: "적용 대기",
  VECTORIZING: "Vector 반영 중",
  ACTIVE: "적용됨(ACTIVE)",
  ARCHIVED: "이전 버전(ARCHIVED)",
  FAILED: "실패",
};

const STATUS_COLOR: Record<PolicyLifecycleStatus, string> = {
  UPLOADED: "bg-zinc-100 text-zinc-700 dark:bg-zinc-800 dark:text-zinc-300",
  EXTRACTING: "bg-amber-100 text-amber-800 dark:bg-amber-900 dark:text-amber-200",
  REVIEWING: "bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200",
  EDITING: "bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200",
  READY_TO_APPLY: "bg-violet-100 text-violet-800 dark:bg-violet-900 dark:text-violet-200",
  VECTORIZING: "bg-amber-100 text-amber-800 dark:bg-amber-900 dark:text-amber-200",
  ACTIVE: "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200",
  ARCHIVED: "bg-zinc-100 text-zinc-500 dark:bg-zinc-800 dark:text-zinc-400",
  FAILED: "bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200",
};

export function PolicyStatusBadge({ status }: { status: PolicyLifecycleStatus }) {
  return (
    <span
      className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${STATUS_COLOR[status]}`}
    >
      {STATUS_LABEL[status]}
    </span>
  );
}
