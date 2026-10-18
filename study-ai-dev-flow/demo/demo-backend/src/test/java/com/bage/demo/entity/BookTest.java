package com.bage.demo.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class BookTest {

    @Test
    void builderAndGettersWork() {
        Book book = Book.builder()
                .id(1L)
                .title("Effective Java")
                .author("Joshua Bloch")
                .isbn("978-0134685991")
                .build();

        assertThat(book.getId()).isEqualTo(1L);
        assertThat(book.getTitle()).isEqualTo("Effective Java");
        assertThat(book.getAuthor()).isEqualTo("Joshua Bloch");
        assertThat(book.getIsbn()).isEqualTo("978-0134685991");
    }

    @Test
    void settersWork() {
        Book book = new Book();
        book.setId(2L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setIsbn("978-0132350884");

        assertThat(book.getId()).isEqualTo(2L);
        assertThat(book.getTitle()).isEqualTo("Clean Code");
        assertThat(book.getAuthor()).isEqualTo("Robert C. Martin");
        assertThat(book.getIsbn()).isEqualTo("978-0132350884");
    }

    @Test
    void equalsAndHashCodeWork() {
        Book b1 = Book.builder().id(1L).title("T").author("A").isbn("I").build();
        Book b2 = Book.builder().id(1L).title("T").author("A").isbn("I").build();

        assertThat(b1).isEqualTo(b2);
        assertThat(b1.hashCode()).isEqualTo(b2.hashCode());
    }

    @Test
    void toStringContainsFields() {
        Book book = Book.builder().id(1L).title("T").author("A").isbn("I").build();
        assertThat(book.toString()).contains("T", "A", "I");
    }
}
