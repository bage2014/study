package com.bage.demo.service;

import com.bage.demo.dto.BookRequest;
import com.bage.demo.dto.BookResponse;
import com.bage.demo.entity.Book;
import com.bage.demo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookRequest request;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .id(1L)
                .title("Title")
                .author("Author")
                .isbn("123-4567890123")
                .build();

        request = BookRequest.builder()
                .title("Title")
                .author("Author")
                .isbn("123-4567890123")
                .build();
    }

    @Test
    void createBookSuccess() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponse response = bookService.createBook(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Title");
        assertThat(response.getAuthor()).isEqualTo("Author");
        assertThat(response.getIsbn()).isEqualTo("123-4567890123");

        verify(bookRepository).existsByIsbn("123-4567890123");
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void createBookDuplicateIsbnThrowsException() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(true);

        assertThatThrownBy(() -> bookService.createBook(request))
                .isInstanceOf(DuplicateIsbnException.class)
                .hasMessageContaining("123-4567890123");

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void getBookByIdSuccess() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponse response = bookService.getBookById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookByIdNotFoundThrowsException() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(99L))
                .isInstanceOf(RuntimeException.class);

        verify(bookRepository).findById(99L);
    }

    @Test
    void getAllBooksReturnsList() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookResponse> responses = bookService.getAllBooks();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getId()).isEqualTo(1L);
        verify(bookRepository).findAll();
    }

    @Test
    void getAllBooksEmptyList() {
        when(bookRepository.findAll()).thenReturn(List.of());

        List<BookResponse> responses = bookService.getAllBooks();

        assertThat(responses).isEmpty();
        verify(bookRepository).findAll();
    }

    @Test
    void updateBookSuccess() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponse response = bookService.updateBook(1L, request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void updateBookNotFoundThrowsException() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.updateBook(99L, request))
                .isInstanceOf(RuntimeException.class);

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void deleteBookSuccess() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteBook(1L);

        verify(bookRepository).existsById(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBookNotFoundThrowsException() {
        when(bookRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> bookService.deleteBook(99L))
                .isInstanceOf(RuntimeException.class);

        verify(bookRepository, never()).deleteById(anyLong());
    }
}
