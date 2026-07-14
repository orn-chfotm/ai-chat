package com.learn.chatai.domain.policy.exception;

import com.learn.chatai.core.exception.CustomException;

public class PolicyException extends CustomException {

    public PolicyException(PolicyErrorCode errorCode) {
        super(errorCode);
    }

    public PolicyException(PolicyErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
