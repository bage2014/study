package com.bage.study.rca.demo.supplier.config;

import com.bage.study.rca.demo.supplier.model.Inventory;
import com.bage.study.rca.demo.supplier.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库存种子数据：SKU-1 充足 / SKU-2 少量 / SKU-3 缺货（用于 RCA 场景演练）。
 */
@Component
public class InventoryDataSeeder implements CommandLineRunner {

    private final InventoryRepository inventoryRepository;

    public InventoryDataSeeder(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public void run(String... args) {
        if (inventoryRepository.count() > 0) {
            return;
        }
        inventoryRepository.saveAll(List.of(
                new Inventory("SKU-1", 1000, new BigDecimal("19.99")),
                new Inventory("SKU-2", 100, new BigDecimal("5.50")),
                new Inventory("SKU-3", 0, new BigDecimal("99.00"))
        ));
    }
}
