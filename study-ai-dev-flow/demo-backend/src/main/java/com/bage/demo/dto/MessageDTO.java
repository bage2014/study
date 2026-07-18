package com.bage.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Message entity.
 * Used to transfer message data between client and server.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {

    private Long id;

    private String content;

    private String sender;

    private String receiver;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}