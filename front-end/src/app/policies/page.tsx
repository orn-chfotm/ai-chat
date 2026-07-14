import Link from "next/link";
import { getPolicyDocuments } from "@/entities/policy-document/api/getPolicyDocuments";
import { PolicyStatusBadge } from "@/entities/policy-document/ui/PolicyStatusBadge";

// 관리자 목록은 항상 최신 상태를 보여줘야 하므로 빌드 시점 정적 프리렌더링을 하지 않는다.
export const dynamic = "force-dynamic";

export default async function PolicyListPage() {
  const list = await getPolicyDocuments();

  return (
    <div className="flex flex-1 flex-col gap-6 p-8">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-semibold">규정 문서 목록</h1>
        <Link
          href="/policies/upload"
          className="rounded bg-foreground px-4 py-2 text-background"
        >
          PDF 업로드
        </Link>
      </div>

      {list.items.length === 0 ? (
        <p className="text-zinc-600 dark:text-zinc-400">업로드된 규정 문서가 없습니다.</p>
      ) : (
        <table className="w-full border-collapse text-left text-sm">
          <thead>
            <tr className="border-b border-zinc-200 dark:border-zinc-800">
              <th className="py-2 pr-4">문서 ID</th>
              <th className="py-2 pr-4">규정 코드</th>
              <th className="py-2 pr-4">파일명</th>
              <th className="py-2 pr-4">상태</th>
              <th className="py-2 pr-4">업로드 시각</th>
            </tr>
          </thead>
          <tbody>
            {list.items.map((item) => (
              <tr key={item.documentId} className="border-b border-zinc-100 dark:border-zinc-900">
                <td className="py-2 pr-4">
                  <Link href={`/policies/${item.documentId}`} className="underline">
                    {item.documentId}
                  </Link>
                </td>
                <td className="py-2 pr-4">{item.policyCode}</td>
                <td className="py-2 pr-4">{item.originalFileName}</td>
                <td className="py-2 pr-4">
                  <PolicyStatusBadge status={item.status} />
                </td>
                <td className="py-2 pr-4">{new Date(item.createdAt).toLocaleString("ko-KR")}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      <p className="text-xs text-zinc-500">
        총 {list.totalElements}건 · {list.page + 1} / {Math.max(list.totalPages, 1)} 페이지
      </p>
    </div>
  );
}
