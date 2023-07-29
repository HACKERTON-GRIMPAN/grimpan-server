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
@Builder
@AllArgsConstructor
public class ResponseDto<Data_> {
    private final Boolean success;
    private final Data_ data;
    private final ErrorCode error;

    public ResponseDto(@Nullable Data_ data) {
        this.success = true;
        this.data = data;
        error = null;
    }

    // 로그인 실패 에러
    public static ResponseEntity<Object> of(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.builder()
                        .success(false)
                        .data(null)
                        .error(ErrorCode.SERVER_ERROR).build());
    }
}
