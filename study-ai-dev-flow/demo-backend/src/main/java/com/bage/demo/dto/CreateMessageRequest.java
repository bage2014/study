package com.bage.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMessageRequest {

    @NotBlank(message = "Content must not be blank")
    private String content;

    @NotBlank(message = "Sender must not be blank")
    private String sender;

    @NotNull(message = "Timestamp must not be null")
    private LocalDateTime timestamp;
}
