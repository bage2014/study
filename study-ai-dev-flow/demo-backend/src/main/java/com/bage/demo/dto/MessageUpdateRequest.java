package com.bage.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating an existing message.
 * Only the fields that are allowed to be updated are included.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageUpdateRequest {

    /**
     * The updated content of the message.
     * Must not be blank and must be at most 5000 characters.
     */
    @NotBlank(message = "Content must not be blank")
    @Size(max = 5000, message = "Content must not exceed 5000 characters")
    private String content;
}