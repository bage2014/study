package com.bage.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息创建/更新请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequestDTO {

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    @Size(max = 5000, message = "消息内容长度不能超过5000个字符")
    private String content;

    /**
     * 发送者ID
     */
    @NotNull(message = "发送者不能为空")
    private Long senderId;

    /**
     * 接收者ID
     */
    @NotNull(message = "接收者不能为空")
    private Long receiverId;

    /**
     * 消息类型：TEXT, IMAGE, FILE, SYSTEM等
     */
    @NotBlank(message = "消息类型不能为空")
    private String messageType;
}
