package com.grimpan.emodiary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(DiaryException.class)
    public ResponseEntity<?> diaryExceptionHandler(DiaryException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> defaultExceptionHandler(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
