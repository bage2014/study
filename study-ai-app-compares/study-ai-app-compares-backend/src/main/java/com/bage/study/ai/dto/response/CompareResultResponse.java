package com.bage.study.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompareResultResponse {

    private String productName;

    private String address;

    private List<ProductPriceResponse> prices;

    private ProductPriceResponse bestRecommendation;
}