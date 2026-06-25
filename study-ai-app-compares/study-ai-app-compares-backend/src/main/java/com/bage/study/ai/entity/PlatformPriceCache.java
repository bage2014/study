package com.bage.study.ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "platform_price_cache", indexes = {
    @Index(name = "idx_cache_keyword", columnList = "keyword"),
    @Index(name = "idx_cache_platform", columnList = "platform"),
    @Index(name = "idx_cache_expire", columnList = "expire_time")
})
public class PlatformPriceCache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keyword", nullable = false, length = 100)
    private String keyword;

    @Column(name = "platform", nullable = false, length = 50)
    private String platform;

    @Column(name = "product_name", length = 200)
    private String productName;

    @Column(name = "store_name", length = 100)
    private String storeName;

    @Column(name = "price")
    private Double price;

    @Column(name = "delivery_fee")
    private Double deliveryFee;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "store_rating", length = 10)
    private String storeRating;

    @Column(name = "delivery_time")
    private Integer deliveryTime;

    @Column(name = "promotion", length = 100)
    private String promotion;

    @Column(name = "expire_time", nullable = false)
    private LocalDateTime expireTime;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}