package com.familytree.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    private final String detailMessage;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.httpStatus = mapToHttpStatus(errorCode);
        this.detailMessage = null;
    }

    public BusinessException(ErrorCode errorCode, String detailMessage) {
        super(buildMessage(errorCode, detailMessage));
        this.errorCode = errorCode;
        this.httpStatus = mapToHttpStatus(errorCode);
        this.detailMessage = detailMessage;
    }

    public BusinessException(ErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
        this.httpStatus = mapToHttpStatus(errorCode);
        this.detailMessage = null;
    }

    public BusinessException(ErrorCode errorCode, HttpStatus httpStatus) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.detailMessage = null;
    }

    public BusinessException(ErrorCode errorCode, HttpStatus httpStatus, String detailMessage) {
        super(buildMessage(errorCode, detailMessage));
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.detailMessage = detailMessage;
    }

    public BusinessException(ErrorCode errorCode, HttpStatus httpStatus, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.detailMessage = cause != null ? cause.getMessage() : null;
    }

    private static String buildMessage(ErrorCode errorCode, String detailMessage) {
        if (detailMessage == null || detailMessage.isEmpty()) {
            return errorCode.getMessage();
        }
        return errorCode.getMessage() + ": " + detailMessage;
    }

    private HttpStatus mapToHttpStatus(ErrorCode errorCode) {
        String code = errorCode.getCode();
        if (code.startsWith("G") || code.startsWith("A")) {
            return HttpStatus.BAD_REQUEST;
        }
        switch (code.charAt(0)) {
            case 'U':
            case 'F':
            case 'M':
            case 'R':
            case 'E':
            case 'D':
            case 'L':
                return HttpStatus.NOT_FOUND;
            case 'P':
                return HttpStatus.FORBIDDEN;
            case 'A':
                return HttpStatus.UNAUTHORIZED;
            case 'I':
                return HttpStatus.INTERNAL_SERVER_ERROR;
            default:
                return HttpStatus.BAD_REQUEST;
        }
    }

    public String getFullMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("[错误码: ").append(errorCode.getCode()).append("]");
        sb.append(" ").append(errorCode.getMessage());
        if (detailMessage != null) {
            sb.append(" [详细信息: ").append(detailMessage).append("]");
        }
        return sb.toString();
    }
}