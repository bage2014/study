package com.bage.study.rca.demo.order.model;

import java.math.BigDecimal;

/**
 * 供应服务库存校验结果（跨服务 DTO，字段与供应服务对齐）。
 */
public record SupplyCheckResult(String skuId, boolean available, int availableQuantity, BigDecimal unitPrice) {
}
