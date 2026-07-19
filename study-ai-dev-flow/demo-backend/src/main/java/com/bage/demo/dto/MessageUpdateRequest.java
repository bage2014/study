package com.bage.demo.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageUpdateRequest {

    @Size(max = 5000, message = "内容长度不能超过5000个字符")
    private String content;

    @Size(max = 100, message = "发送者长度不能超过100个字符")
    private String sender;

    @Size(max = 100, message = "接收者长度不能超过100个字符")
    private String receiver;
}