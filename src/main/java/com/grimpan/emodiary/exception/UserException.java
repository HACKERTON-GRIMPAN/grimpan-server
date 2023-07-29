package com.grimpan.emodiary.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserException extends RuntimeException{
    private ErrorCode errorCode;
    private String message;

    public UserException(ErrorCode errorCode) {
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