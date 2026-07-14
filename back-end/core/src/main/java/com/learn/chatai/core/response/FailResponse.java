package com.learn.chatai.core.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Returned only by {@code GlobalExceptionHandler}. See {@link SuccessResponse} for why this
 * type deliberately does not share an interface with it.
 */
public record FailResponse(
        boolean success,
        String code,
        String message,
        LocalDateTime timestamp,
        List<String> details
) {

    public static FailResponse of(String code, String message) {
        return new FailResponse(false, code, message, LocalDateTime.now(), List.of());
    }

    public static FailResponse of(String code, String message, List<String> details) {
        return new FailResponse(false, code, message, LocalDateTime.now(), details);
    }
}
