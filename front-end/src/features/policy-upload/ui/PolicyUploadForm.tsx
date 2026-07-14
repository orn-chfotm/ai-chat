"use client";

import { useRouter } from "next/navigation";
import { useState, type FormEvent } from "react";
import { ApiError } from "@/shared/api/http";
import { uploadPolicyDocument } from "@/features/policy-upload/api/uploadPolicyDocument";

export function PolicyUploadForm() {
  const router = useRouter();
  const [file, setFile] = useState<File | null>(null);
  const [policyCode, setPolicyCode] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!file || !policyCode.trim()) {
      setError("파일과 규정 코드를 모두 입력해 주세요.");
      return;
    }

    setSubmitting(true);
    setError(null);
    try {
      const result = await uploadPolicyDocument(file, policyCode.trim());
      // 업로드 직후 상태 확인이 가장 급한 정보이므로 job 상태(polling) 화면으로 이동한다.
      router.push(`/jobs/${result.jobId}`);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "업로드 중 오류가 발생했습니다.");
      setSubmitting(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-4 max-w-md">
      <label className="flex flex-col gap-1">
        <span className="text-sm font-medium">규정 코드</span>
        <input
          type="text"
          value={policyCode}
          onChange={(event) => setPolicyCode(event.target.value)}
          placeholder="예: INTERNAL_POLICY"
          className="rounded border border-zinc-300 px-3 py-2 dark:border-zinc-700 dark:bg-zinc-900"
          disabled={submitting}
        />
      </label>

      <label className="flex flex-col gap-1">
        <span className="text-sm font-medium">PDF 파일</span>
        <input
          type="file"
          accept="application/pdf"
          onChange={(event) => setFile(event.target.files?.[0] ?? null)}
          className="rounded border border-zinc-300 px-3 py-2 dark:border-zinc-700 dark:bg-zinc-900"
          disabled={submitting}
        />
      </label>

      {error && <p className="text-sm text-red-600 dark:text-red-400">{error}</p>}

      <button
        type="submit"
        disabled={submitting}
        className="rounded bg-foreground px-4 py-2 text-background disabled:opacity-50"
      >
        {submitting ? "업로드 중..." : "업로드"}
      </button>
    </form>
  );
}
