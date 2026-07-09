package com.bage.study.ai.service;

import com.bage.study.ai.dto.response.ProductPriceResponse;

import java.util.List;

public interface RpaService {

    List<ProductPriceResponse> search(String keyword);

    String getPlatformName();

    boolean isAvailable();
}
