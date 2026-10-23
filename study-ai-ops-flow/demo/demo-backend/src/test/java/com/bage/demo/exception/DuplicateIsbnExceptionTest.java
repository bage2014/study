package com.bage.demo.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DuplicateIsbnExceptionTest {

    @Test
    void constructorWithIsbnSetsMessage() {
        DuplicateIsbnException ex = new DuplicateIsbnException("123-4567890123");
        assertThat(ex.getMessage()).contains("123-4567890123");
    }

    @Test
    void isRuntimeException() {
        DuplicateIsbnException ex = new DuplicateIsbnException("isbn");
        assertThat(ex).isInstanceOf(RuntimeException.class);
    }
}
