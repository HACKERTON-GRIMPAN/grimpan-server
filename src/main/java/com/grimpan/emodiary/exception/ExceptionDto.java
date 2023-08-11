package com.grimpan.emodiary.exception;

import lombok.Getter;

@Getter
public final class ExceptionDto {
    private final String message;

    public ExceptionDto(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }
}
