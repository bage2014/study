package com.example.dto;

import com.example.entity.Order;
import com.example.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponse {

    private Long id;
    private String productName;
    private Integer quantity;
    private BigDecimal amount;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public static OrderListResponse fromEntity(Order order) {
        return OrderListResponse.builder()
                .id(order.getId())
                .productName(order.getProductName())
                .quantity(order.getQuantity())
                .amount(order.getAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}