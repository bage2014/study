package com.bage.demo.controller;

import com.bage.demo.dto.BookRequest;
import com.bage.demo.dto.BookResponse;
import com.bage.demo.service.BookService;
import com.bage.demo.service.DuplicateIsbnException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Test
    void createBookReturnsCreated() throws Exception {
        BookRequest request = BookRequest.builder()
                .title("Title")
                .author("Author")
                .isbn("123-4567890123")
                .build();

        BookResponse response = BookResponse.builder()
                .id(1L)
                .title("Title")
                .author("Author")
                .isbn("123-4567890123")
                .build();

        when(bookService.createBook(any(BookRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.author").value("Author"))
                .andExpect(jsonPath("$.isbn").value("123-4567890123"));

        verify(bookService).createBook(any(BookRequest.class));
    }

    @Test
    void createBookDuplicateIsbnReturnsConflict() throws Exception {
        BookRequest request = BookRequest.builder()
                .title("Title")
                .author("Author")
                .isbn("123-4567890123")
                .build();

        when(bookService.createBook(any(BookRequest.class)))
                .thenThrow(new DuplicateIsbnException("123-4567890123"));

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        verify(bookService).createBook(any(BookRequest.class));
    }

    @Test
    void createBookInvalidRequestReturnsBadRequest() throws Exception {
        BookRequest request = BookRequest.builder()
                .title("")
                .author("")
                .isbn("")
                .build();

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).createBook(any(BookRequest.class));
    }

    @Test
    void getBookByIdReturnsBook() throws Exception {
        BookResponse response = BookResponse.builder()
                .id(1L)
                .title("Title")
                .author("Author")
                .isbn("123-4567890123")
                .build();

        when(bookService.getBookById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Title"));

        verify(bookService).getBookById(1L);
    }

    @Test
    void getBookByIdNotFoundReturnsNotFound() throws Exception {
        when(bookService.getBookById(99L)).thenThrow(new RuntimeException("Book not found"));

        mockMvc.perform(get("/api/books/99"))
                .andExpect(status().isNotFound());

        verify(bookService).getBookById(99L);
    }

    @Test
    void getAllBooksReturnsList() throws Exception {
        BookResponse response = BookResponse.builder()
                .id(1L)
                .title("Title")
                .author("Author")
                .isbn("123-4567890123")
                .build();

        when(bookService.getAllBooks()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(bookService).getAllBooks();
    }

    @Test
    void updateBookReturnsUpdatedBook() throws Exception {
        BookRequest request = BookRequest.builder()
                .title("Updated")
                .author("Author")
                .isbn("123-4567890123")
                .build();

        BookResponse response = BookResponse.builder()
                .id(1L)
                .title("Updated")
                .author("Author")
                .isbn("123-4567890123")
                .build();

        when(bookService.updateBook(eq(1L), any(BookRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));

        verify(bookService).updateBook(eq(1L), any(BookRequest.class));
    }

    @Test
    void updateBookNotFoundReturnsNotFound() throws Exception {
        BookRequest request = BookRequest.builder()
                .title("Updated")
                .author("Author")
                .isbn("123-4567890123")
                .build();

        when(bookService.updateBook(eq(99L), any(BookRequest.class)))
                .thenThrow(new RuntimeException("Book not found"));

        mockMvc.perform(put("/api/books/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(bookService).updateBook(eq(99L), any(BookRequest.class));
    }

    @Test
    void deleteBookReturnsNoContent() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService).deleteBook(1L);
    }

    @Test
    void deleteBookNotFoundReturnsNotFound() throws Exception {
        doThrow(new RuntimeException("Book not found")).when(bookService).deleteBook(99L);

        mockMvc.perform(delete("/api/books/99"))
                .andExpect(status().isNotFound());

        verify(bookService).deleteBook(99L);
    }
}
