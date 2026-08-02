package com.bage.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing message.
 * Only the content field is updatable.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageUpdateDTO {

    /**
     * Updated message content. Must not be blank.
     */
    @NotBlank(message = "Message content must not be blank")
    @Size(max = 5000, message = "Message content must not exceed 5000 characters")
    private String content;
}
