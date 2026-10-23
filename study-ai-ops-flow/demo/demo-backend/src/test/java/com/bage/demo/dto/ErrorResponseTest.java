package com.bage.demo.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    void builderAndGettersWork() {
        ErrorResponse response = ErrorResponse.builder()
                .message("Error")
                .status(400)
                .build();

        assertThat(response.getMessage()).isEqualTo("Error");
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void equalsAndHashCodeWork() {
        ErrorResponse r1 = ErrorResponse.builder().message("Error").status(400).build();
        ErrorResponse r2 = ErrorResponse.builder().message("Error").status(400).build();

        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }
}
