package com.bage.demo.dto;

import com.bage.demo.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {

    private Long id;
    private String content;
    private String sender;
    private LocalDateTime timestamp;
    private LocalDateTime updatedAt;

    public static MessageDTO fromEntity(Message message) {
        return MessageDTO.builder()
                .id(message.getId())
                .content(message.getContent())
                .sender(message.getSender())
                .timestamp(message.getTimestamp())
                .updatedAt(message.getUpdatedAt())
                .build();
    }
}
