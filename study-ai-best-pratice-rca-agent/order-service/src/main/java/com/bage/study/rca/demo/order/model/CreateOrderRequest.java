package com.bage.study.rca.demo.order.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 下单请求。
 */
public record CreateOrderRequest(
        @NotBlank(message = "skuId 不能为空") String skuId,
        @NotNull(message = "quantity 不能为空")
        @Min(value = 1, message = "quantity 必须大于 0") Integer quantity) {
}
