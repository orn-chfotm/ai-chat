"use client";

import { useRouter } from "next/navigation";
import { useState, type FormEvent } from "react";
import { ApiError } from "@/shared/api/http";
import { savePolicyText } from "@/features/policy-text-edit/api/savePolicyText";

export function PolicyTextEditForm({
  documentId,
  initialContent,
}: {
  documentId: string;
  initialContent: string;
}) {
  const router = useRouter();
  const [content, setContent] = useState(initialContent);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!content.trim()) {
      setError("텍스트를 입력해 주세요.");
      return;
    }

    setSubmitting(true);
    setError(null);
    try {
      await savePolicyText(documentId, content);
      // 저장 후 상세로 복귀. refresh로 서버 컴포넌트가 최신 EDITED 이력을 다시 읽게 한다.
      router.push(`/policies/${documentId}`);
      router.refresh();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "저장 중 오류가 발생했습니다.");
      setSubmitting(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="flex flex-1 flex-col gap-4">
      <textarea
        value={content}
        onChange={(event) => setContent(event.target.value)}
        className="min-h-[24rem] flex-1 resize-y rounded border border-zinc-300 p-4 font-mono text-sm dark:border-zinc-700 dark:bg-zinc-900"
        disabled={submitting}
      />

      {error && <p className="text-sm text-red-600 dark:text-red-400">{error}</p>}

      <div className="flex gap-3">
        <button
          type="submit"
          disabled={submitting}
          className="rounded bg-foreground px-4 py-2 text-background disabled:opacity-50"
        >
          {submitting ? "저장 중..." : "저장"}
        </button>
        <button
          type="button"
          onClick={() => router.push(`/policies/${documentId}`)}
          disabled={submitting}
          className="rounded border border-zinc-300 px-4 py-2 dark:border-zinc-700"
        >
          취소
        </button>
      </div>
    </form>
  );
}
