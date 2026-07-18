package com.bage.demo.dto;

import com.bage.demo.entity.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageCreateRequest {

    @NotBlank(message = "消息内容不能为空")
    @Size(max = 5000, message = "消息内容长度不能超过5000字符")
    private String content;

    @NotBlank(message = "发送者ID不能为空")
    private String senderId;

    @NotBlank(message = "接收者ID不能为空")
    private String receiverId;

    @NotNull(message = "消息类型不能为空")
    private MessageType messageType;
}