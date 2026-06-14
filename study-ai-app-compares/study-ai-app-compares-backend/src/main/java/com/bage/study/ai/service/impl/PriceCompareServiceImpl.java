package com.bage.study.ai.service.impl;

import com.bage.study.ai.dto.response.CompareResultResponse;
import com.bage.study.ai.dto.response.ProductPriceResponse;
import com.bage.study.ai.service.AddressService;
import com.bage.study.ai.service.JdApiService;
import com.bage.study.ai.service.MeituanApiService;
import com.bage.study.ai.service.PriceCompareService;
import com.bage.study.ai.service.TaobaoApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceCompareServiceImpl implements PriceCompareService {

    private final AddressService addressService;
    private final TaobaoApiService taobaoApiService;
    private final JdApiService jdApiService;
    private final MeituanApiService meituanApiService;

    @Override
    public CompareResultResponse comparePrices(String productName, String addressId) {
        log.info("开始比较价格，商品: {}, 地址ID: {}", productName, addressId);

        String address = "未选择地址";
        if (addressId != null && !addressId.isEmpty()) {
            try {
                var addr = addressService.getAddressById(addressId);
                address = addr.getProvinceCityDistrict() + " " + addr.getDetailAddress();
            } catch (Exception e) {
                log.warn("获取地址失败: {}", e.getMessage());
            }
        }

        List<ProductPriceResponse> prices = fetchPricesFromPlatforms(productName);
        
        ProductPriceResponse bestRecommendation = prices.stream()
                .min(Comparator.comparingDouble(ProductPriceResponse::getTotalPrice))
                .orElse(null);

        return CompareResultResponse.builder()
                .productName(productName)
                .address(address)
                .prices(prices)
                .bestRecommendation(bestRecommendation)
                .build();
    }

    private List<ProductPriceResponse> fetchPricesFromPlatforms(String productName) {
        List<ProductPriceResponse> prices = new ArrayList<>();

        CompletableFuture<List<ProductPriceResponse>> taobaoFuture = 
            CompletableFuture.supplyAsync(() -> taobaoApiService.search(productName));
        CompletableFuture<List<ProductPriceResponse>> jdFuture = 
            CompletableFuture.supplyAsync(() -> jdApiService.search(productName));
        CompletableFuture<List<ProductPriceResponse>> meituanFuture = 
            CompletableFuture.supplyAsync(() -> meituanApiService.search(productName));

        CompletableFuture.allOf(taobaoFuture, jdFuture, meituanFuture).join();

        try {
            prices.addAll(taobaoFuture.get());
        } catch (Exception e) {
            log.error("获取淘宝数据失败: {}", e.getMessage());
        }
        try {
            prices.addAll(jdFuture.get());
        } catch (Exception e) {
            log.error("获取京东数据失败: {}", e.getMessage());
        }
        try {
            prices.addAll(meituanFuture.get());
        } catch (Exception e) {
            log.error("获取美团数据失败: {}", e.getMessage());
        }

        return prices;
    }
}