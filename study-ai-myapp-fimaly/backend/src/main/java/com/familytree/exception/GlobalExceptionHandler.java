package com.familytree.exception;

import com.familytree.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 处理运行时异常
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<?> handleRuntimeException(RuntimeException ex) {
        logger.error("RuntimeException occurred: {}", ex.getMessage(), ex);
        return ApiResponse.error(ex.getMessage());
    }

    // 处理其他异常
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception ex) {
        logger.error("Exception occurred: {}", ex.getMessage(), ex);
        return ApiResponse.serverError("服务器内部错误");
    }
}
