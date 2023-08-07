package com.grimpan.emodiary.exception;

import lombok.Getter;

@Getter
public final class CommonException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public CommonException(final ErrorCode errorCode, final String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        if (message == null) {
            return errorCode.getMessage();
        } else {
            return errorCode.getMessage() + message;
        }
    }
}
