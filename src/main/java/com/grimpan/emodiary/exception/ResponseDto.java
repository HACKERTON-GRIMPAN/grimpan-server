package com.grimpan.emodiary.exception;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@Getter
public class ResponseDto<Data_> {
    private final HttpStatus httpStatus;
    private final Boolean success;
    private final Data_ data;
    private final ErrorCode error;

    @Builder
    private ResponseDto(final HttpStatus httpStatus, final Boolean success,
                       @Nullable final Data_ data, final ErrorCode error) {
        this.httpStatus = httpStatus;
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> ResponseDto<T> ok(@Nullable final T data) {
        return new ResponseDto<>(HttpStatus.OK, true, data, null);
    }

    public static <T> ResponseDto<T> created(@Nullable final T data) {
        return new ResponseDto<>(HttpStatus.CREATED, true, data, null);
    }


    // 객체 생성 패턴 해야함
//    public static ResponseDto<Object> toResponseEntity(final Exception e) {
//        return new ResponseDto<>(HttpStatus.BAD_REQUEST, false, null, ErrorCode.SERVER_ERROR);
//    }

    // 로그인 실패 에러
    public static ResponseEntity<Object> of(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.builder()
                        .success(false)
                        .data(null)
                        .error(ErrorCode.SERVER_ERROR).build());
    }
}
