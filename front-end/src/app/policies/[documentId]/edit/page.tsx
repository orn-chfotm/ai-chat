import Link from "next/link";
import { notFound } from "next/navigation";
import { getPolicyDocument } from "@/entities/policy-document/api/getPolicyDocument";
import { PolicyTextEditForm } from "@/features/policy-text-edit/ui/PolicyTextEditForm";
import { ApiError } from "@/shared/api/http";

// 편집 대상 텍스트는 최신 상태여야 하므로 정적 프리렌더링을 하지 않는다.
export const dynamic = "force-dynamic";

// 추출이 끝난 문서만 검수/수정할 수 있다 (백엔드 markEditing 가드와 동일 기준).
const EDITABLE_STATUSES = ["REVIEWING", "EDITING"] as const;

export default async function PolicyTextEditPage({
  params,
}: {
  params: Promise<{ documentId: string }>;
}) {
  const { documentId } = await params;

  const document = await getPolicyDocument(documentId).catch((error: unknown) => {
    if (error instanceof ApiError && error.status === 404) {
      notFound();
    }
    throw error;
  });

  const editable = (EDITABLE_STATUSES as readonly string[]).includes(document.status);

  return (
    <div className="flex flex-1 flex-col gap-6 p-8">
      <h1 className="text-2xl font-semibold">추출 텍스트 수정 · {document.originalFileName}</h1>

      {!editable ? (
        <div className="flex flex-col gap-3">
          <p className="text-zinc-600 dark:text-zinc-400">
            현재 상태({document.status})에서는 텍스트를 수정할 수 없습니다. 추출이 완료된 문서만
            검수/수정할 수 있습니다.
          </p>
          <Link href={`/policies/${documentId}`} className="underline">
            상세로 돌아가기
          </Link>
        </div>
      ) : (
        <PolicyTextEditForm
          documentId={documentId}
          initialContent={document.latestRevision?.content ?? ""}
        />
      )}
    </div>
  );
}
