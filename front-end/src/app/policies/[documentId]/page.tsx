import Link from "next/link";
import { notFound } from "next/navigation";
import { getPolicyDocument } from "@/entities/policy-document/api/getPolicyDocument";
import { PolicyStatusBadge } from "@/entities/policy-document/ui/PolicyStatusBadge";
import { ApiError } from "@/shared/api/http";

// 문서 상태는 실시간으로 바뀌므로 빌드 시점 정적 프리렌더링을 하지 않는다.
export const dynamic = "force-dynamic";

export default async function PolicyDocumentDetailPage({
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

  const editable = document.status === "REVIEWING" || document.status === "EDITING";

  return (
    <div className="flex flex-1 flex-col gap-6 p-8">
      <div className="flex items-center gap-3">
        <h1 className="text-2xl font-semibold">{document.originalFileName}</h1>
        <PolicyStatusBadge status={document.status} />
        {editable && (
          <Link
            href={`/policies/${documentId}/edit`}
            className="ml-auto rounded bg-foreground px-4 py-2 text-sm text-background"
          >
            텍스트 수정
          </Link>
        )}
      </div>

      <dl className="grid grid-cols-[auto_1fr] gap-x-4 gap-y-2 text-sm">
        <dt className="text-zinc-500">문서 ID</dt>
        <dd>{document.documentId}</dd>
        <dt className="text-zinc-500">규정 코드</dt>
        <dd>{document.policyCode}</dd>
        <dt className="text-zinc-500">업로드 시각</dt>
        <dd>{new Date(document.createdAt).toLocaleString("ko-KR")}</dd>
      </dl>

      <section className="flex flex-col gap-2">
        <h2 className="text-lg font-medium">추출/수정 텍스트</h2>
        {document.latestRevision === null ? (
          <p className="text-zinc-600 dark:text-zinc-400">
            아직 텍스트 추출이 완료되지 않았습니다.
          </p>
        ) : (
          <div className="flex flex-col gap-2">
            <p className="text-xs text-zinc-500">
              {document.latestRevision.revisionType === "EXTRACTED" ? "PDF 추출 원문" : "관리자 수정본"}
              {" · "}
              {new Date(document.latestRevision.createdAt).toLocaleString("ko-KR")}
              {document.latestRevision.extractedPageCount !== null &&
                ` · ${document.latestRevision.extractedPageCount}페이지`}
              {document.latestRevision.extractedCharacterCount !== null &&
                ` · ${document.latestRevision.extractedCharacterCount}자`}
            </p>
            <pre className="max-h-96 overflow-auto whitespace-pre-wrap rounded border border-zinc-200 p-4 text-sm dark:border-zinc-800">
              {document.latestRevision.content}
            </pre>
          </div>
        )}
      </section>

      {document.status !== "ACTIVE" && document.status !== "ARCHIVED" && (
        <p className="text-xs text-zinc-500">
          적용(ACTIVE 전환) 전까지는 이 문서 내용이 챗봇 답변 기준에 반영되지 않습니다.
        </p>
      )}
    </div>
  );
}
