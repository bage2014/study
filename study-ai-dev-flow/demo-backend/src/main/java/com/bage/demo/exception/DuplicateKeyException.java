package com.bage.demo.exception;

public class DuplicateKeyException extends RuntimeException {

    public DuplicateKeyException(String message) {
        super(message);
    }

    public DuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
