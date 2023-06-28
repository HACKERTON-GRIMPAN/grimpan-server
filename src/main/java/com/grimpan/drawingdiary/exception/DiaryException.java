package com.grimpan.drawingdiary.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DiaryException extends RuntimeException{
    private ErrorCode errorCode;
    private String message;

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
