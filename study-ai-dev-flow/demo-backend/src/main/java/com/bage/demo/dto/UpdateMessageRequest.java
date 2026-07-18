package com.bage.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMessageRequest {

    @NotBlank(message = "Content must not be blank")
    private String content;

    @NotBlank(message = "Sender must not be blank")
    private String sender;

    private LocalDateTime timestamp;
}
