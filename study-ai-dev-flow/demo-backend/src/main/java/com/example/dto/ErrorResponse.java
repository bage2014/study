package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "错误响应")
public class ErrorResponse {

    @Schema(description = "错误码", example = "NOT_FOUND")
    private String code;

    @Schema(description = "错误消息", example = "订单不存在")
    private String message;
}