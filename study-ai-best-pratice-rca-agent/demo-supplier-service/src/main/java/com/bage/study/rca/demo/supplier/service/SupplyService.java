package com.bage.study.rca.demo.supplier.service;

import com.bage.study.rca.demo.supplier.model.SupplyCheckResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 供应领域服务：内存模拟库存，不依赖数据库，便于独立启动。
 */
@Service
public class SupplyService {

    private record Inventory(int quantity, BigDecimal price) {
    }

    private static final Map<String, Inventory> INVENTORY = Map.of(
            "SKU-1", new Inventory(1000, new BigDecimal("19.99")),
            "SKU-2", new Inventory(100, new BigDecimal("5.50")),
            "SKU-3", new Inventory(0, new BigDecimal("99.00"))
    );

    /**
     * 正常库存查询：毫秒级返回。
     */
    public SupplyCheckResult check(String skuId, int requestedQuantity) {
        Inventory inventory = INVENTORY.get(skuId);
        if (inventory == null) {
            return new SupplyCheckResult(skuId, false, 0, BigDecimal.ZERO);
        }
        return new SupplyCheckResult(skuId,
                inventory.quantity() >= requestedQuantity,
                inventory.quantity(),
                inventory.price());
    }
}
