package com.bage.demo.service;

import com.bage.demo.dto.BookRequest;
import com.bage.demo.dto.BookResponse;
import java.util.List;

/**
 * Service contract for book operations.
 */
public interface BookService {

    BookResponse createBook(BookRequest request);

    List<BookResponse> getAllBooks();

    BookResponse getBook(Long id);
}
