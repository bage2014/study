package com.bage.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 删除消息响应 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteResponse {

    /**
     * 删除是否成功
     */
    private Boolean success;

    /**
     * 删除的消息数量
     */
    private Integer deletedCount;

    /**
     * 提示消息
     */
    private String message;

    /**
     * 快速创建成功响应
     *
     * @param deletedCount 删除数量
     * @return DeleteResponse
     */
    public static DeleteResponse success(int deletedCount) {
        return DeleteResponse.builder()
                .success(true)
                .deletedCount(deletedCount)
                .message("删除成功")
                .build();
    }

    /**
     * 快速创建失败响应
     *
     * @param message 失败原因
     * @return DeleteResponse
     */
    public static DeleteResponse failure(String message) {
        return DeleteResponse.builder()
                .success(false)
                .deletedCount(0)
                .message(message)
                .build();
    }
}