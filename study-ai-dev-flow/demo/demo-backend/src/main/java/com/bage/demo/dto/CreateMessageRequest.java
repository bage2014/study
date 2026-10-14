package com.bage.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMessageRequest {

    @NotBlank(message = "消息内容不能为空")
    private String content;

    @NotNull(message = "发送者ID不能为空")
    private Long senderId;

    @NotNull(message = "接收者ID不能为空")
    private Long receiverId;
}