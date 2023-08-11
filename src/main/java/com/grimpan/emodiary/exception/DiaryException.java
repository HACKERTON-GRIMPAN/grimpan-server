package com.grimpan.emodiary.exception;

import lombok.Getter;

@Getter
public class DiaryException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public DiaryException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = null;
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
