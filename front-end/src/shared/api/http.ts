import { API_BASE_URL } from "@/shared/config/env";
import type { ApiResponse } from "@/shared/api/types";

export class ApiError extends Error {
  readonly status: number;
  readonly code: string;
  readonly details: string[];

  constructor(status: number, code: string, message: string, details: string[]) {
    super(message);
    this.name = "ApiError";
    this.status = status;
    this.code = code;
    this.details = details;
  }
}

type ApiFetchInit = Omit<RequestInit, "body"> & { body?: BodyInit | null };

/**
 * Calls the back-end API and unwraps the common SuccessResponse/FailResponse envelope
 * (core.response.SuccessResponse / FailResponse). Never sets Content-Type for FormData bodies
 * so the browser can attach its own multipart boundary (needed for policy upload).
 */
export async function apiFetch<T>(path: string, init?: ApiFetchInit): Promise<T> {
  const isFormData = init?.body instanceof FormData;
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...init,
    headers: isFormData
      ? init?.headers
      : { "Content-Type": "application/json", ...init?.headers },
  });

  const body = (await response.json()) as ApiResponse<T>;

  if (!body.success) {
    throw new ApiError(body.status, body.code, body.message, body.details);
  }

  return body.data;
}
