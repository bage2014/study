package com.bage.demo.exception;

/**
 * Thrown when a resource already exists and conflicts with the request.
 */
public class ResourceConflictException extends RuntimeException {

    public ResourceConflictException(String message) {
        super(message);
    }
}
