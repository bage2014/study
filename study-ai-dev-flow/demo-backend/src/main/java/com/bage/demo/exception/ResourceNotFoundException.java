package com.bage.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 资源未找到异常，当请求的资源不存在时抛出。
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * 构造一个带错误消息的 ResourceNotFoundException。
     *
     * @param message 错误消息
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * 构造一个带错误消息和原因的 ResourceNotFoundException。
     *
     * @param message 错误消息
     * @param cause   原始异常
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造一个带资源名称和资源ID的 ResourceNotFoundException。
     *
     * @param resourceName 资源名称
     * @param fieldName    字段名称
     * @param fieldValue   字段值
     * @return 包含详细信息的 ResourceNotFoundException
     */
    public static ResourceNotFoundException of(String resourceName, String fieldName, Object fieldValue) {
        return new ResourceNotFoundException(
                String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}