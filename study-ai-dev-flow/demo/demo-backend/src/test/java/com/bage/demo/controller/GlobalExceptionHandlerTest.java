package com.bage.demo.controller;

import com.bage.demo.exception.DuplicateIsbnException;
import com.bage.demo.exception.GlobalExceptionHandler;
import com.bage.demo.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private com.bage.demo.service.BookService bookService;

    @Test
    void handleDuplicateIsbnExceptionReturnsConflict() throws Exception {
        when(bookService.getBookById(1L)).thenThrow(new DuplicateIsbnException("123-4567890123"));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void handleResourceNotFoundExceptionReturnsNotFound() throws Exception {
        when(bookService.getBookById(99L)).thenThrow(new ResourceNotFoundException("Book not found"));

        mockMvc.perform(get("/api/books/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found"));
    }
}
