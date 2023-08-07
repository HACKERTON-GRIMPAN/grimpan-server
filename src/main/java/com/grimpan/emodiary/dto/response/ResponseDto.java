package com.grimpan.emodiary.dto.response;

import com.grimpan.emodiary.exception.CommonException;
import com.grimpan.emodiary.exception.ErrorCode;
import com.grimpan.emodiary.exception.ExceptionDto;
import io.micrometer.common.lang.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
public class ResponseDto<Data_> {

    @JsonIgnore
    private final HttpStatus httpStatus;
    private final Boolean success;
    private final Data_ data;
    private final ExceptionDto error;

    @Builder
    private ResponseDto(final HttpStatus httpStatus, final Boolean success,
                       @Nullable final Data_ data, final ExceptionDto error) {
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

    public static ResponseDto<Object> toResponseEntity(final Exception e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, false, null, new ExceptionDto(ErrorCode.SERVER_ERROR));
    }

    public static ResponseDto<Object> toResponseEntity(final CommonException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, false, null, new ExceptionDto(e.getErrorCode()));
    }
}
