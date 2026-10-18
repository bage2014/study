package com.bage.demo.exception;

/**
 * Thrown when a duplicate resource is detected (e.g. duplicate ISBN).
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
