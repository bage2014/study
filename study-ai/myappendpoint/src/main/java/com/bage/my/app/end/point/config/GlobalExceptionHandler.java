package com.bage.my.app.end.point.config;

import com.bage.my.app.end.point.entity.ApiResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResponse<Void> handleException(Exception e) {
        return new ApiResponse<>(500, "服务器内部错误: " + e.getMessage(), null);
    }
}