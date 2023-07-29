package com.grimpan.emodiary.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // Not Found Error
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "Not Exist User"),
    NOT_FOUND_SIGNUP_HISTORY(HttpStatus.NOT_FOUND, "해당 소셜로 로그인한 기록이 없습니다."),
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 일기를 찾을 수 없습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이미지를 찾을 수 없습니다."),

    // Server
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),

    // Bad Request Error
    NOT_END_POINT(HttpStatus.BAD_REQUEST , "Not Exist End Point Error"),

    // Access Denied Error
    ACCESS_DENIED_ERROR(HttpStatus.UNAUTHORIZED, "Access Denied Token Error"),

    // Token Error Set
    TOKEN_INVALID_ERROR(HttpStatus.UNAUTHORIZED, "Invalid Token Error"),
    TOKEN_MALFORMED_ERROR(HttpStatus.UNAUTHORIZED, "Malformed Token Error"),
    TOKEN_EXPIRED_ERROR(HttpStatus.UNAUTHORIZED, "Expired Token Error"),
    TOKEN_TYPE_ERROR(HttpStatus.UNAUTHORIZED, "Type Token Error"),
    TOKEN_UNSUPPORTED_ERROR(HttpStatus.UNAUTHORIZED, "Unsupported Token Error"),
    TOKEN_UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "Unknown Error");

    private final HttpStatus httpStatus;
    private final String message;
}
