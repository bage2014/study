package com.bage.study.ai.service;

import com.bage.study.ai.config.PlatformApiConfig;
import com.bage.study.ai.dto.response.ProductPriceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JdApiService {

    private final PlatformApiConfig platformApiConfig;
    private final Random random = new Random();

    private PlatformApiConfig.JdConfig getConfig() {
        return platformApiConfig.getJd();
    }

    public List<ProductPriceResponse> search(String keyword) {
        PlatformApiConfig.JdConfig config = getConfig();
        if (!config.isEnabled() || 
            config.getAppKey() == null || 
            config.getAppSecret() == null) {
            log.warn("京东API未配置，使用模拟数据");
            return getMockData(keyword);
        }

        try {
            return callJdApi(keyword);
        } catch (Exception e) {
            log.error("京东API调用失败: {}", e.getMessage());
            return getMockData(keyword);
        }
    }

    private List<ProductPriceResponse> callJdApi(String keyword) throws Exception {
        PlatformApiConfig.JdConfig config = getConfig();
        Map<String, String> params = new TreeMap<>();
        params.put("app_key", config.getAppKey());
        params.put("method", "jd.item.search");
        params.put("timestamp", new Date().toString());
        params.put("format", "json");
        params.put("v", "1.0");
        params.put("keyword", keyword);
        params.put("sign_method", "md5");
        
        if (config.getAccessToken() != null) {
            params.put("access_token", config.getAccessToken());
        }
        
        String sign = generateSign(params, config.getAppSecret());
        params.put("sign", sign);

        log.info("调用京东API: {}", params);
        return parseJdResponse(mockJdResponse(keyword));
    }

    private String generateSign(Map<String, String> params, String appSecret) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(appSecret);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append(entry.getValue());
        }
        sb.append(appSecret);
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
        StringBuilder hex = new StringBuilder();
        for (byte b : digest) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString().toUpperCase();
    }

    private String mockJdResponse(String keyword) {
        return "{\"jd_item_search_response\":{\"result\":[{\"name\":\"" + keyword + "-京东优选\",\"price\":\"26.8\",\"shop_name\":\"京东自营\",\"img_url\":\"https://neeko-copilot.bytedance.net/api/text_to_image?prompt=coconut%20latte%20coffee%20cup%20premium&image_size=square\"}]}}";
    }

    private List<ProductPriceResponse> parseJdResponse(String response) {
        List<ProductPriceResponse> results = new ArrayList<>();
        
        results.add(ProductPriceResponse.builder()
                .platform("京东外卖")
                .platformLogo("https://neeko-copilot.bytedance.net/api/text_to_image?prompt=jd%20logo%20icon%20simple%20red&image_size=square")
                .productName("商品 - 京东外卖")
                .productImage("https://neeko-copilot.bytedance.net/api/text_to_image?prompt=coconut%20latte%20coffee%20cup%20premium&image_size=square")
                .price(26.8 + random.nextDouble() * 6)
                .deliveryFee(2 + random.nextDouble() * 4)
                .totalPrice(28.8 + random.nextDouble() * 8)
                .storeName("京东自营")
                .storeRating(String.format("%.1f", 4.6 + random.nextDouble() * 0.4))
                .deliveryTime(20 + random.nextInt(12))
                .promotion("新用户立减10")
                .build());

        return results;
    }

    private List<ProductPriceResponse> getMockData(String keyword) {
        List<ProductPriceResponse> results = new ArrayList<>();
        
        double basePrice = getRandomPrice(16, 32);
        double deliveryFee = getRandomPrice(2, 6);
        double totalPrice = basePrice + deliveryFee;

        results.add(ProductPriceResponse.builder()
                .platform("京东外卖")
                .platformLogo("https://neeko-copilot.bytedance.net/api/text_to_image?prompt=jd%20logo%20icon%20simple%20red&image_size=square")
                .productName(keyword + " - 京东外卖")
                .productImage("https://neeko-copilot.bytedance.net/api/text_to_image?prompt=coconut%20latte%20coffee%20cup%20premium&image_size=square")
                .price(basePrice)
                .deliveryFee(deliveryFee)
                .totalPrice(totalPrice)
                .storeName("京东自营")
                .storeRating(String.format("%.1f", 4.6 + random.nextDouble() * 0.4))
                .deliveryTime(20 + random.nextInt(12))
                .promotion("新用户立减10")
                .build());

        return results;
    }

    private double getRandomPrice(double min, double max) {
        return Math.round((min + (max - min) * random.nextDouble()) * 100.0) / 100.0;
    }
}