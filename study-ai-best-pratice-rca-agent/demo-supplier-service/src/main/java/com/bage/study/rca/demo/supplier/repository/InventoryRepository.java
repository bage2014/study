package com.bage.study.rca.demo.supplier.repository;

import com.bage.study.rca.demo.supplier.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, String> {
}
