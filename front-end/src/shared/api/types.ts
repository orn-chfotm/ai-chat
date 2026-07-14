/** Mirrors back-end core.response.SuccessResponse<T>. */
export type SuccessResponse<T> = {
  success: true;
  status: number;
  message: string;
  timestamp: string;
  data: T;
};

/** Mirrors back-end core.response.FailResponse. */
export type FailResponse = {
  success: false;
  status: number;
  code: string;
  message: string;
  timestamp: string;
  details: string[];
};

export type ApiResponse<T> = SuccessResponse<T> | FailResponse;
