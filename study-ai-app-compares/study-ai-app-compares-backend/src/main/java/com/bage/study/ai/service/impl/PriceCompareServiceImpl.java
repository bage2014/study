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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceCompareServiceImpl implements PriceCompareService {

    private final AddressService addressService;
    private final TaobaoApiService taobaoApiService;
    private final JdApiService jdApiService;
    private final MeituanApiService meituanApiService;
    private final RpaAggregateServiceImpl rpaAggregateService;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_PREFIX = "price:compare:";
    private static final long CACHE_EXPIRE_MINUTES = 30;

    @Override
    public CompareResultResponse comparePrices(String productName, String addressId) {
        log.info("开始比较价格，商品: {}, 地址ID: {}", productName, addressId);

        String cacheKey = buildCacheKey(productName, addressId);
        
        CompareResultResponse cachedResult = getFromCache(cacheKey);
        if (cachedResult != null) {
            log.info("缓存命中，直接返回结果");
            return cachedResult;
        }

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

        CompareResultResponse result = CompareResultResponse.builder()
                .productName(productName)
                .address(address)
                .prices(prices)
                .bestRecommendation(bestRecommendation)
                .build();

        saveToCache(cacheKey, result);

        return result;
    }

    private String buildCacheKey(String productName, String addressId) {
        return CACHE_PREFIX + productName.toLowerCase().trim() + ":" + (addressId != null ? addressId : "default");
    }

    @SuppressWarnings("unchecked")
    private CompareResultResponse getFromCache(String key) {
        try {
            Object cached = redisTemplate.opsForValue().get(key);
            if (cached instanceof CompareResultResponse) {
                return (CompareResultResponse) cached;
            }
        } catch (Exception e) {
            log.warn("从Redis缓存读取失败: {}", e.getMessage());
        }
        return null;
    }

    private void saveToCache(String key, CompareResultResponse result) {
        try {
            redisTemplate.opsForValue().set(key, result, CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            log.info("结果已存入Redis缓存，有效期{}分钟", CACHE_EXPIRE_MINUTES);
        } catch (Exception e) {
            log.warn("写入Redis缓存失败: {}", e.getMessage());
        }
    }

    private List<ProductPriceResponse> fetchPricesFromPlatforms(String productName) {
        List<ProductPriceResponse> prices = new ArrayList<>();

        CompletableFuture<List<ProductPriceResponse>> apiFuture = CompletableFuture.supplyAsync(() -> fetchFromApi(productName));
        CompletableFuture<List<ProductPriceResponse>> rpaFuture = CompletableFuture.supplyAsync(() -> fetchFromRpa(productName));

        CompletableFuture.allOf(apiFuture, rpaFuture).join();

        try {
            prices.addAll(apiFuture.get());
        } catch (Exception e) {
            log.error("获取API数据失败: {}", e.getMessage());
        }

        try {
            List<ProductPriceResponse> rpaResults = rpaFuture.get();
            if (!rpaResults.isEmpty()) {
                prices = mergeResults(prices, rpaResults);
            }
        } catch (Exception e) {
            log.error("获取RPA数据失败: {}", e.getMessage());
        }

        return prices;
    }

    private List<ProductPriceResponse> fetchFromApi(String productName) {
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
            log.error("获取淘宝API数据失败: {}", e.getMessage());
        }
        try {
            prices.addAll(jdFuture.get());
        } catch (Exception e) {
            log.error("获取京东API数据失败: {}", e.getMessage());
        }
        try {
            prices.addAll(meituanFuture.get());
        } catch (Exception e) {
            log.error("获取美团API数据失败: {}", e.getMessage());
        }

        return prices;
    }

    private List<ProductPriceResponse> fetchFromRpa(String productName) {
        if (!rpaAggregateService.isAnyRpaAvailable()) {
            log.info("RPA服务不可用，跳过RPA搜索");
            return new ArrayList<>();
        }
        log.info("使用RPA服务搜索商品: {}", productName);
        return rpaAggregateService.search(productName);
    }

    private List<ProductPriceResponse> mergeResults(List<ProductPriceResponse> apiResults, List<ProductPriceResponse> rpaResults) {
        List<ProductPriceResponse> merged = new ArrayList<>(apiResults);
        
        for (ProductPriceResponse rpaItem : rpaResults) {
            boolean exists = merged.stream()
                    .anyMatch(item -> item.getPlatform().equals(rpaItem.getPlatform()) 
                            && item.getProductName().contains(rpaItem.getProductName()));
            if (!exists) {
                merged.add(rpaItem);
            }
        }
        
        log.info("合并结果完成，API结果: {} 条, RPA结果: {} 条, 合并后: {} 条", 
                apiResults.size(), rpaResults.size(), merged.size());
        return merged;
    }
}
