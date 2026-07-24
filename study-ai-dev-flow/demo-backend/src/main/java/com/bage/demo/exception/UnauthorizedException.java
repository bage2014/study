package com.bage.demo.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String action, String reason) {
        super(String.format("Unauthorized to %s: %s", action, reason));
    }
}
