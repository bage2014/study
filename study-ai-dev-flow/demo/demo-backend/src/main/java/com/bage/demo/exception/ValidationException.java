package com.bage.demo.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public ValidationException(String message) {
        super(message);
        this.errors = null;
    }

    public ValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.errors = null;
    }
}