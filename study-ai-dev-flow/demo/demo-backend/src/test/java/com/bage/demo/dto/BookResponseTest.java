package com.bage.demo.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookResponseTest {

    @Test
    void builderAndGettersWork() {
        BookResponse response = BookResponse.builder()
                .id(1L)
                .title("Title")
                .author("Author")
                .isbn("123-4567890123")
                .build();

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Title");
        assertThat(response.getAuthor()).isEqualTo("Author");
        assertThat(response.getIsbn()).isEqualTo("123-4567890123");
    }

    @Test
    void equalsAndHashCodeWork() {
        BookResponse r1 = BookResponse.builder().id(1L).title("T").author("A").isbn("I").build();
        BookResponse r2 = BookResponse.builder().id(1L).title("T").author("A").isbn("I").build();

        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }
}
