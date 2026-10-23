package com.bage.demo.exception;

/**
 * Thrown when attempting to persist a book with an ISBN that already exists.
 */
public class DuplicateIsbnException extends RuntimeException {

    public DuplicateIsbnException(String message) {
        super(message);
    }

    public DuplicateIsbnException(String message, Throwable cause) {
        super(message, cause);
    }
}
