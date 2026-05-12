package com.bage.study.ai.best.practice.dev.flow.exception;

public class EmailExistsException extends RuntimeException {
    public EmailExistsException(String message) {
        super(message);
    }
}