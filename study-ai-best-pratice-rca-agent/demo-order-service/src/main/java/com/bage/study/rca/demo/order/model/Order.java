package com.bage.study.rca.demo.order.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * 订单实体（内存存储 demo）。
 */
public record Order(Long id,
                    String skuId,
                    Integer quantity,
                    BigDecimal unitPrice,
                    BigDecimal totalAmount,
                    String status,
                    Instant createdAt) {
}
