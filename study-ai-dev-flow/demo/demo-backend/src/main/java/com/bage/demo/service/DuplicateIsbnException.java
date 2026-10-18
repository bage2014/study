package com.bage.demo.service;

/**
 * Thrown when attempting to create a book with an ISBN that already exists.
 */
public class DuplicateIsbnException extends RuntimeException {

    public DuplicateIsbnException(String isbn) {
        super("Book with isbn '" + isbn + "' already exists");
    }
}
