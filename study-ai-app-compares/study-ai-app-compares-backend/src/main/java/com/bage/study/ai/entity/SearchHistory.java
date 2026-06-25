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
@Table(name = "search_history", indexes = {
    @Index(name = "idx_search_keyword", columnList = "keyword"),
    @Index(name = "idx_search_created", columnList = "created_at")
})
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keyword", nullable = false, length = 100)
    private String keyword;

    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "platform_count", nullable = false)
    private Integer platformCount;

    @Column(name = "best_price")
    private Double bestPrice;

    @Column(name = "best_platform", length = 50)
    private String bestPlatform;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}