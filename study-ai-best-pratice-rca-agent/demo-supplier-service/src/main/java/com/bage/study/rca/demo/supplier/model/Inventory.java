package com.bage.study.rca.demo.supplier.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * 库存实体（H2 持久化）。
 */
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    private String skuId;
    private Integer quantity;
    private BigDecimal price;

    protected Inventory() {
    }

    public Inventory(String skuId, Integer quantity, BigDecimal price) {
        this.skuId = skuId;
        this.quantity = quantity;
        this.price = price;
    }

    public String getSkuId() {
        return skuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
