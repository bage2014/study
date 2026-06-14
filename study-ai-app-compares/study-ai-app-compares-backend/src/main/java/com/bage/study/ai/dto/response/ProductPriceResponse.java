package com.bage.study.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceResponse {

    private String platform;

    private String platformLogo;

    private String productName;

    private String productImage;

    private Double price;

    private Double deliveryFee;

    private Double totalPrice;

    private String storeName;

    private String storeRating;

    private Integer deliveryTime;

    private String promotion;
}