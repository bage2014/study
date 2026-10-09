package com.bage.study.rca.demo.supplier.model;

import java.math.BigDecimal;

/**
 * 库存校验结果。
 *
 * @param skuId             商品 SKU
 * @param available         库存是否充足
 * @param availableQuantity 当前可用库存数量
 * @param unitPrice         单价
 */
public record SupplyCheckResult(String skuId, boolean available, int availableQuantity, BigDecimal unitPrice) {
}
