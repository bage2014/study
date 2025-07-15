package com.bage.my.app.end.point.config;

import com.bage.my.app.end.point.entity.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import lombok.extern.slf4j.Slf4j;
import com.bage.my.app.end.point.util.JsonUtil;

@ControllerAdvice
@Slf4j
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 排除ApiResponse类型，防止重复包装
        return !returnType.getParameterType().isAssignableFrom(ApiResponse.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // com.bage.my.app.end.point.entity.ApiResponse cannot be cast to class java.lang.String (com.bage.my.app.end.point.entity.ApiResponse is in unnamed module of loader 'app'; java.lang.String is in module java.base of loader 'bootstrap')
        ApiResponse<Object> apiResponse = new ApiResponse<>(200, "success", body);
        log.info("beforeBodyWrite apiResponse: {}", JsonUtil.toJson(apiResponse));
        return apiResponse;
    }
}