package com.bage.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for returning message data in responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {

    private UUID id;
    private String content;
    private LocalDateTime timestamp;
}