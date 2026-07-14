import Link from "next/link";

export default function DashboardPage() {
  return (
    <div className="flex flex-1 flex-col gap-6 p-8">
      <h1 className="text-2xl font-semibold">관리자 대시보드</h1>
      <p className="text-zinc-600 dark:text-zinc-400">
        사내 규정 문서 업로드, 검수, Vector 반영 상태, 테스트 채팅을 관리합니다.
      </p>
      <nav className="flex gap-4 text-sm underline">
        <Link href="/policies">규정 문서 목록</Link>
      </nav>
    </div>
  );
}
