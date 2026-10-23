package com.bage.demo.repository;

import com.bage.demo.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void saveAndFindById() {
        Book book = Book.builder()
                .title("Test Book")
                .author("Author")
                .isbn("123-4567890123")
                .build();

        Book saved = bookRepository.save(book);
        assertThat(saved.getId()).isNotNull();

        Optional<Book> found = bookRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Test Book");
    }

    @Test
    void findByIsbnReturnsBook() {
        Book book = Book.builder()
                .title("Test Book")
                .author("Author")
                .isbn("999-8887776665")
                .build();
        bookRepository.save(book);

        Optional<Book> found = bookRepository.findByIsbn("999-8887776665");
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Test Book");
    }

    @Test
    void findByIsbnReturnsEmptyWhenNotFound() {
        Optional<Book> found = bookRepository.findByIsbn("non-existent");
        assertThat(found).isEmpty();
    }

    @Test
    void existsByIsbnReturnsTrueWhenExists() {
        Book book = Book.builder()
                .title("Test Book")
                .author("Author")
                .isbn("111-2223334445")
                .build();
        bookRepository.save(book);

        boolean exists = bookRepository.existsByIsbn("111-2223334445");
        assertThat(exists).isTrue();
    }

    @Test
    void existsByIsbnReturnsFalseWhenNotExists() {
        boolean exists = bookRepository.existsByIsbn("000-0000000000");
        assertThat(exists).isFalse();
    }
}
