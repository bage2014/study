package com.bage.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for creating a book.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {

    @NotBlank(message = "title must not be blank")
    private String title;

    @NotBlank(message = "author must not be blank")
    private String author;

    @NotBlank(message = "isbn must not be blank")
    private String isbn;

    @NotNull(message = "price must not be null")
    @Positive(message = "price must be greater than 0")
    private BigDecimal price;
}
