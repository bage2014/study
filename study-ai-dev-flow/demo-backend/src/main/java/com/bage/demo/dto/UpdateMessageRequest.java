package com.bage.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新消息请求 DTO
 * 用于接收更新消息的请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMessageRequest {

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    @Size(max = 2000, message = "消息内容长度不能超过2000个字符")
    private String content;

    /**
     * 消息状态：0-未读，1-已读
     */
    private Integer status;
}