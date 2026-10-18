package com.bage.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Book entity persisted in H2 via Spring Data JPA.
 */
@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "title must not be blank")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "author must not be blank")
    @Column(nullable = false)
    private String author;

    @NotBlank(message = "isbn must not be blank")
    @Column(nullable = false, unique = true)
    private String isbn;

    @NotNull(message = "price must not be null")
    @Positive(message = "price must be greater than 0")
    @Column(nullable = false)
    private BigDecimal price;
}
