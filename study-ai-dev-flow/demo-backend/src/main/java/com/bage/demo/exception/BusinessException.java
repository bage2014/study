package com.bage.demo.exception;

/**
 * 业务异常类，用于处理业务逻辑中的异常情况。
 */
public class BusinessException extends RuntimeException {

    private final Integer code;

    /**
     * 构造业务异常。
     *
     * @param code    错误码
     * @param message 错误消息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造业务异常。
     *
     * @param code    错误码
     * @param message 错误消息
     * @param cause   原始异常
     */
    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 获取错误码。
     *
     * @return 错误码
     */
    public Integer getCode() {
        return code;
    }
}