package com.bage.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing message.
 * Only the content field is editable; other fields (sender, timestamp) are immutable after creation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMessageRequest {

    @NotBlank(message = "Message content must not be blank")
    @Size(min = 1, max = 5000, message = "Message content must be between 1 and 5000 characters")
    private String content;
}