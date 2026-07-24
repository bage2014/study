package com.bage.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新消息请求 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMessageRequest {

    @NotBlank(message = "消息标题不能为空")
    @Size(max = 200, message = "消息标题长度不能超过200个字符")
    private String title;

    @NotBlank(message = "消息内容不能为空")
    @Size(max = 5000, message = "消息内容长度不能超过5000个字符")
    private String content;

    @Size(max = 100, message = "发送者长度不能超过100个字符")
    private String sender;

    @Size(max = 100, message = "接收者长度不能超过100个字符")
    private String receiver;
}