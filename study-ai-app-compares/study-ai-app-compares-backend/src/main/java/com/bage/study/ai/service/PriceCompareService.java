package com.bage.study.ai.service;

import com.bage.study.ai.dto.response.CompareResultResponse;

public interface PriceCompareService {

    CompareResultResponse comparePrices(String productName, String addressId);
}