import { PolicyUploadForm } from "@/features/policy-upload/ui/PolicyUploadForm";

export default function PolicyUploadPage() {
  return (
    <div className="flex flex-1 flex-col gap-6 p-8">
      <h1 className="text-2xl font-semibold">규정 문서 업로드</h1>
      <PolicyUploadForm />
    </div>
  );
}
