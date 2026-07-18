package com.bage.demo.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuplicateResourceExceptionTest {

    @Test
    void constructor_ShouldSetMessage() {
        DuplicateResourceException exception = new DuplicateResourceException(\