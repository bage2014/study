package com.bage.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for submitting feedback.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackRequest {

    @NotBlank(message = "name is required")
    @Size(max = 100, message = "name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "email is required")
    @Email(message = "email must be a valid email address")
    @Size(max = 150, message = "email must not exceed 150 characters")
    private String email;

    @NotBlank(message = "content is required")
    @Size(max = 2000, message = "content must not exceed 2000 characters")
    private String content;
}
