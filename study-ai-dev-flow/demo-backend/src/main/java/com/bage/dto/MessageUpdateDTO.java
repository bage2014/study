package com.bage.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息更新DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageUpdateDTO {

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送者
     */
    private String sender;

    /**
     * 接收者
     */
    private String receiver;

    /**
     * 消息类型（如 TEXT, IMAGE, FILE 等）
     */
    private String messageType;
}