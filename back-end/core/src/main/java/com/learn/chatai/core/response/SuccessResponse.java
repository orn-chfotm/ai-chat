package com.learn.chatai.core.response;

/**
 * The only response wrapper a Controller method's return type may declare
 * (e.g. {@code ResponseEntity<SuccessResponse<PolicyDocumentResponseDto>>}). There is no shared
 * supertype with {@link FailResponse} on purpose — that would let a success-path method
 * declare/return a failure body. Failures only ever come from {@code GlobalExceptionHandler},
 * whose methods have their own unrelated return type.
 */
public record SuccessResponse<T>(
        boolean success,
        T data
) {

    public static <T> SuccessResponse<T> of(T data) {
        return new SuccessResponse<>(true, data);
    }
}
