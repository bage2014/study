package com.bage.my.app.end.point.config;

import com.bage.my.app.end.point.entity.ApiResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResponse<Void> handleException(Exception e) {
        log.error("GlobalException: {}", e.getMessage(), e);
        return ApiResponse.fail(500, e.getMessage());
    }
}