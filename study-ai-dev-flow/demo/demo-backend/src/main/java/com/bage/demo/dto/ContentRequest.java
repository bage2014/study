package com.bage.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    private String title;

    @NotBlank(message = "Body is required")
    @Size(min = 1, max = 10000, message = "Body must be between 1 and 10000 characters")
    private String body;

    @Size(max = 500, message = "Summary must be at most 500 characters")
    private String summary;

    @Size(max = 50, message = "Category must be at most 50 characters")
    private String category;
}