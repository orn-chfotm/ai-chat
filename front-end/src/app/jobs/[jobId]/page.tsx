import { JobStatusView } from "@/features/job-status-polling/ui/JobStatusView";

export default async function JobStatusPage({
  params,
}: {
  params: Promise<{ jobId: string }>;
}) {
  const { jobId } = await params;

  return (
    <div className="flex flex-1 flex-col gap-6 p-8">
      <JobStatusView jobId={jobId} />
    </div>
  );
}
