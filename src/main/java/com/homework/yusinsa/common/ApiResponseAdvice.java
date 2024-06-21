package com.homework.yusinsa.common;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@SuppressWarnings("NullableProblems")
@ControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  org.springframework.http.server.ServerHttpRequest request,
                                  org.springframework.http.server.ServerHttpResponse response) {

        // 이미 ResponseEntity로 래핑된 경우, ApiResponse로 다시 래핑하지 않음
        if (body instanceof ApiResponse || body instanceof ResponseEntity) {
            return body;
        }

        // HTTP 상태 코드 가져오기
        HttpStatus status = HttpStatus.OK;
        if (response instanceof org.springframework.http.server.ServletServerHttpResponse) {
            status = HttpStatus.valueOf(((org.springframework.http.server.ServletServerHttpResponse) response).getServletResponse().getStatus());
        }

        // ApiResponse로 래핑
        return new ApiResponse<>(body, status.getReasonPhrase(), status.value());
    }

}
