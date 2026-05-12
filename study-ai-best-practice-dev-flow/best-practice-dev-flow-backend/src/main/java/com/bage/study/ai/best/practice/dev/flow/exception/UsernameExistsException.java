package com.bage.study.ai.best.practice.dev.flow.exception;

public class UsernameExistsException extends RuntimeException {
    public UsernameExistsException(String message) {
        super(message);
    }
}