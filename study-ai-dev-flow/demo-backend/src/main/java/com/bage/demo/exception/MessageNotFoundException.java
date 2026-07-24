package com.bage.demo.exception;

public class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException(Long id) {
        super("Message not found with id: " + id);
    }

    public MessageNotFoundException(String message) {
        super(message);
    }
}
