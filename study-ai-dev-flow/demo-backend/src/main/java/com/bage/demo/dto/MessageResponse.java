package com.bage.demo.dto;

import com.bage.demo.entity.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {

    private Long id;
    private String content;
    private String senderId;
    private String receiverId;
    private MessageType messageType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}