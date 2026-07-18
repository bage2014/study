package com.bage.demo.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void testConstructorWithMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        assertEquals("Resource not found", ex.getMessage());
    }

    @Test
    void testConstructorWithNullMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException(null);
        assertNull(ex.getMessage());
    }

    @Test
    void testConstructorWithEmptyMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("");
        assertEquals("", ex.getMessage());
    }

    @Test
    void testIsRuntimeException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("test");
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    void testExceptionThrown() {
        assertThrows(ResourceNotFoundException.class, () -> {
            throw new ResourceNotFoundException("Thrown");
        });
    }
}