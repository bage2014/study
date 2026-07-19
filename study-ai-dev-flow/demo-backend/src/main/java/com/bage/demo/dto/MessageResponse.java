package com.bage.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for returning message data in API responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {

    private Long id;
    private String content;
    private String sender;
    private String receiver;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
