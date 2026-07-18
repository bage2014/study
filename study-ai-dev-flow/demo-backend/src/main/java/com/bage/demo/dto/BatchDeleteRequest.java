package com.bage.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 批量删除消息请求 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchDeleteRequest {

    /**
     * 待删除的消息ID列表
     */
    @NotEmpty(message = "消息ID列表不能为空")
    private List<Long> ids;
}