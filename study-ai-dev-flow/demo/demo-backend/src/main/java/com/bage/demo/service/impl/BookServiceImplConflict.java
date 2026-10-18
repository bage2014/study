package com.bage.demo.service.impl;

import com.bage.demo.dto.BookRequest;
import com.bage.demo.dto.BookResponse;
import com.bage.demo.entity.Book;
import com.bage.demo.exception.ResourceConflictException;
import com.bage.demo.repository.BookRepository;
import com.bage.demo.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Alternative {@link BookService} implementation enforcing ISBN uniqueness.
 *
 * <p>This bean is intentionally not annotated with {@code @Service} to avoid
 * conflicting with {@link BookServiceImpl}. It is kept as a reference of the
 * conflict-handling logic. The active implementation must be one of the two.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImplConflict implements BookService {

    private final BookRepository bookRepository;

    @Override
    @Transactional
    public BookResponse createBook(BookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new ResourceConflictException("Book with isbn " + request.getIsbn() + " already exists");
        }
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .build();
        Book saved = bookRepository.save(book);
        log.info("Created book with id={}", saved.getId());
        return BookResponse.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .author(saved.getAuthor())
                .isbn(saved.getIsbn())
                .price(saved.getPrice())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(book -> BookResponse.builder()
                        .id(book.getId())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .isbn(book.getIsbn())
                        .price(book.getPrice())
                        .build())
                .toList();
    }
}
