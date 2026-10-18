package com.bage.demo.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response payload describing a message.
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
    private Boolean read;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
