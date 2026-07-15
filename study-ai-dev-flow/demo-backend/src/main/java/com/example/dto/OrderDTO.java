package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Order data transfer object")
public class OrderDTO {

    @Schema(description = "Unique identifier of the order", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull
    @Schema(description = "ID of the user placing the order", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @NotEmpty
    @Valid
    @Schema(description = "List of items in the order")
    private List<OrderItemDTO> items;

    @Positive
    @Schema(description = "Total price of the order", example = "59.98", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal totalPrice;

    @Schema(description = "Status of the order", example = "PENDING", allowableValues = {"PENDING", "SHIPPED", "DELIVERED", "CANCELLED"})
    private String status;

    @Schema(description = "Timestamp when the order was created", example = "2025-04-01T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    public OrderDTO() {
    }

    public OrderDTO(Long id, Long userId, List<OrderItemDTO> items, BigDecimal totalPrice, String status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Schema(description = "Item within an order")
    public static class OrderItemDTO {

        @NotNull
        @Schema(description = "ID of the product", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        private Long productId;

        @Positive
        @Schema(description = "Quantity of the product", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
        private Integer quantity;

        @Positive
        @Schema(description = "Price per unit at the time of order", example = "29.99", requiredMode = Schema.RequiredMode.REQUIRED)
        private BigDecimal unitPrice;

        public OrderItemDTO() {
        }

        public OrderItemDTO(Long productId, Integer quantity, BigDecimal unitPrice) {
            this.productId = productId;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }
    }
}