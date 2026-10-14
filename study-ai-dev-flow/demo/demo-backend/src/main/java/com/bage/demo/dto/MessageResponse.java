package com.bage.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private Long id;
    private String content;
    private String sender;
    private String receiver;
    private Boolean read;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
