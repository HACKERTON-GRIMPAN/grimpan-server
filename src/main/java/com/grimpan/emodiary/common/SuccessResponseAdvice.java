package com.grimpan.emodiary.common;

import com.grimpan.emodiary.exception.ResponseDto;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = "com.grimpan.emodiary")
public class SuccessResponseAdvice implements ResponseBodyAdvice<ResponseDto<?>>{

    /**
     * controller 작업이 끝난 response를 beforeBodyWrite 메서드에 보낼지 판단
     * 판단하는 기준으로는 해당 메서드의 파라미터인 controller의 returnType 정보, messageConverter 정보가 있다.
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    /**
     * controller 작업이 끝나고 어떠한 Converter를 통해 응답을 보낼지 결정된 후에 불린다.
     * 다만, Converter를 통하지 않은 상태이다. ( 어떠한 converter를 사용할지는 정해졌지만, 아직 convert 하지는 않은 상태 )
     * 이 메서드에서 실제 사용자가 원하는 body의 값을 교체 또는 response에 헤더 정보를 추가할 수 있다.
     * 우리는 Response 의 상태를 지정해주기 위해 사용한다.
     */
    @Override
    public ResponseDto<?> beforeBodyWrite(ResponseDto<?> body, MethodParameter returnType, MediaType selectedContentType,
                                          Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                          ServerHttpRequest request, ServerHttpResponse response) {
        if (returnType.getParameterType().equals(ResponseDto.class)) {
            response.setStatusCode(((ResponseDto<?>) body).getHttpStatus());
        }
        return body;
    }
}
