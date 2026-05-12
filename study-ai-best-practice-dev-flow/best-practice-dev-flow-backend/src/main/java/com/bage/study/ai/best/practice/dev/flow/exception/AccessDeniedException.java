package com.bage.study.ai.best.practice.dev.flow.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}