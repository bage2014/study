package com.bage.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 消息响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponseDTO {

    /**
     * 消息ID
     */
    private Long id;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送者ID
     */
    private Long senderId;

    /**
     * 接收者ID
     */
    private Long receiverId;

    /**
     * 消息类型：TEXT, IMAGE, FILE, SYSTEM等
     */
    private String messageType;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
