package com.grimpan.emodiary.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(DiaryException.class)
    public ResponseEntity<?> diaryExceptionHandler(DiaryException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getMessage());
    }


    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> UserExceptionHandler(UserException e){
        log.error("HandleException throw UserException : {}", e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getMessage());
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> defaultExceptionHandler(Exception e){
//        log.error("HandleException throw Exception : {}", e.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//    }
}
