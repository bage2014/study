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
public class UpdateMessageRequest {

    @Size(max = 5000, message = "内容长度不能超过5000个字符")
    private String content;

    private String sender;

    private String receiver;
}