package com.bage.demo.service;

import com.bage.demo.dto.BookRequest;
import com.bage.demo.dto.BookResponse;
import java.util.List;

/**
 * Service contract for book operations.
 */
public interface BookService {

    BookResponse createBook(BookRequest request);

    BookResponse getBookById(Long id);

    List<BookResponse> getAllBooks();

    BookResponse updateBook(Long id, BookRequest request);

    void deleteBook(Long id);
}
