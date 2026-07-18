package com.bage.demo.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuplicateKeyExceptionTest {

    @Test
    void constructor_ShouldSetMessage() {
        DuplicateKeyException exception = new DuplicateKeyException(\