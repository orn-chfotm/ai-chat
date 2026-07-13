package com.learn.chatai.domain.job.exception;

import com.learn.chatai.core.exception.CustomException;

public class JobException extends CustomException {

    public JobException(JobErrorCode errorCode) {
        super(errorCode);
    }

    public JobException(JobErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
