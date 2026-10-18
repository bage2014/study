package com.bage.demo.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BookRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    void validBookRequestHasNoViolations() {
        BookRequest request = BookRequest.builder()
                .title("Title")
                .author("Author")
                .isbn("123-4567890123")
                .build();

        Set<ConstraintViolation<BookRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void blankTitleFailsValidation() {
        BookRequest request = BookRequest.builder()
                .title("")
                .author("Author")
                .isbn("123-4567890123")
                .build();

        Set<ConstraintViolation<BookRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("title"));
    }

    @Test
    void nullAuthorFailsValidation() {
        BookRequest request = BookRequest.builder()
                .title("Title")
                .author(null)
                .isbn("123-4567890123")
                .build();

        Set<ConstraintViolation<BookRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("author"));
    }

    @Test
    void nullIsbnFailsValidation() {
        BookRequest request = BookRequest.builder()
                .title("Title")
                .author("Author")
                .isbn(null)
                .build();

        Set<ConstraintViolation<BookRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("isbn"));
    }
}
