package com.grimpan.emodiary.exception;

import com.grimpan.emodiary.dto.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(DiaryException.class)
    public ResponseDto<?> diaryExceptionHandler(DiaryException e){
        log.error("diaryExceptionHandler() in ExceptionManager throw DiaryException : {}", e.getMessage());
        return ResponseDto.toResponseEntity(e);
    }


    @ExceptionHandler(CommonException.class)
    public ResponseDto<?> commonExceptionHandler(CommonException e) {
        log.error("HandleException throw CommonException : {}", e.getMessage());
        return ResponseDto.toResponseEntity(e);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> defaultExceptionHandler(Exception e){
//        log.error("HandleException throw Exception : {}", e.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//    }
}
