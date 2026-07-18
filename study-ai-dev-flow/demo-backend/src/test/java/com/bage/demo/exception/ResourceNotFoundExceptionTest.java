package com.bage.demo.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void constructor_ShouldSetMessage() {
        ResourceNotFoundException exception = new ResourceNotFoundException(\