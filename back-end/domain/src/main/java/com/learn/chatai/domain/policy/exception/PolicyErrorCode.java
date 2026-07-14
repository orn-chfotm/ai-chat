package com.learn.chatai.domain.policy.exception;

import com.learn.chatai.core.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum PolicyErrorCode implements ErrorCode {

    DOCUMENT_NOT_FOUND("POLICY_DOCUMENT_NOT_FOUND", "문서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    PolicyErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }
}
