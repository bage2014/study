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
public class MessageDTO {

    private Long id;

    @NotBlank(message = "消息内容不能为空")
    private String content;

    @NotBlank(message = "发送者不能为空")
    private String sender;

    @NotBlank(message = "接收者不能为空")
    private String receiver;

    @NotNull(message = "消息类型不能为空")
    private Integer messageType;

    private Integer status; // 0-未读, 1-已读
}
