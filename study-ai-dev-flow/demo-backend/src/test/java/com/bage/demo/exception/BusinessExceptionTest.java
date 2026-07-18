package com.bage.demo.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    void constructor_ShouldSetMessage() {
        BusinessException ex = new BusinessException(\