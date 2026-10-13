package com.bage.study.rca.demo.supplier.service;

import com.bage.study.rca.demo.supplier.model.Inventory;
import com.bage.study.rca.demo.supplier.model.SupplyCheckResult;
import com.bage.study.rca.demo.supplier.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 供应领域服务：库存持久化到 H2，启动时写入种子数据。
 */
@Service
public class SupplyService {

    private final InventoryRepository inventoryRepository;

    public SupplyService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * 正常库存查询：毫秒级返回。
     */
    public SupplyCheckResult check(String skuId, int requestedQuantity) {
        Inventory inventory = inventoryRepository.findById(skuId).orElse(null);
        if (inventory == null) {
            return new SupplyCheckResult(skuId, false, 0, BigDecimal.ZERO);
        }
        return new SupplyCheckResult(skuId,
                inventory.getQuantity() >= requestedQuantity,
                inventory.getQuantity(),
                inventory.getPrice());
    }
}
