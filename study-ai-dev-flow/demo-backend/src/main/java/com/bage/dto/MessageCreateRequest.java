package com.bage.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new message.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageCreateRequest {

    @NotBlank(message = "Content must not be blank")
    private String content;
}