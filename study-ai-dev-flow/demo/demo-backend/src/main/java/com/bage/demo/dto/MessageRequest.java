package com.bage.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息创建/更新请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    @Size(max = 1000, message = "消息内容长度不能超过1000个字符")
    private String content;
}
