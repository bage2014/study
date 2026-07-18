package com.bage.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequest {

    @NotBlank(message = "内容不能为空")
    @Size(max = 5000, message = "内容长度不能超过5000个字符")
    private String content;

    private LocalDateTime timestamp;
}