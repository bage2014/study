package com.bage.demo.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void builder_ShouldCreateErrorResponse() {
        ErrorResponse response = ErrorResponse.builder()
                .code(404)
                .message(\