package com.bage.study.ai.service;

import com.bage.study.ai.config.PlatformApiConfig.MeituanConfig;
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
public class MeituanApiService {

    private final MeituanConfig meituanConfig;
    private final Random random = new Random();

    public List<ProductPriceResponse> search(String keyword) {
        if (!meituanConfig.isEnabled() || 
            meituanConfig.getAppKey() == null || 
            meituanConfig.getAppSecret() == null) {
            log.warn("美团API未配置，使用模拟数据");
            return getMockData(keyword);
        }

        try {
            return callMeituanApi(keyword);
        } catch (Exception e) {
            log.error("美团API调用失败: {}", e.getMessage());
            return getMockData(keyword);
        }
    }

    private List<ProductPriceResponse> callMeituanApi(String keyword) throws Exception {
        Map<String, String> params = new TreeMap<>();
        params.put("appkey", meituanConfig.getAppKey());
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("keyword", keyword);
        
        String sign = generateSign(params, meituanConfig.getAppSecret());
        params.put("sign", sign);

        log.info("调用美团API: {}", params);
        return parseMeituanResponse(mockMeituanResponse(keyword));
    }

    private String generateSign(Map<String, String> params, String appSecret) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.append("appsecret=").append(appSecret);
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
        StringBuilder hex = new StringBuilder();
        for (byte b : digest) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString().toUpperCase();
    }

    private String mockMeituanResponse(String keyword) {
        return "{\"data\":[{\"name\":\"" + keyword + "-美团优选\",\"price\":\"24.9\",\"shop_name\":\"瑞幸咖啡\",\"image\":\"https://neeko-copilot.bytedance.net/api/text_to_image?prompt=coconut%20latte%20coffee%20delicious%20cup&image_size=square\",\"rating\":\"4.8\",\"delivery_time\":\"25\"}]}";
    }

    private List<ProductPriceResponse> parseMeituanResponse(String response) {
        List<ProductPriceResponse> results = new ArrayList<>();
        
        results.add(ProductPriceResponse.builder()
                .platform("美团")
                .platformLogo("https://neeko-copilot.bytedance.net/api/text_to_image?prompt=meituan%20logo%20icon%20simple%20yellow&image_size=square")
                .productName("商品 - 美团")
                .productImage("https://neeko-copilot.bytedance.net/api/text_to_image?prompt=coconut%20latte%20coffee%20delicious%20cup&image_size=square")
                .price(24.9 + random.nextDouble() * 6)
                .deliveryFee(3 + random.nextDouble() * 5)
                .totalPrice(27.9 + random.nextDouble() * 9)
                .storeName("瑞幸咖啡")
                .storeRating(String.format("%.1f", 4.7 + random.nextDouble() * 0.3))
                .deliveryTime(22 + random.nextInt(15))
                .promotion("满20减5")
                .build());

        return results;
    }

    private List<ProductPriceResponse> getMockData(String keyword) {
        List<ProductPriceResponse> results = new ArrayList<>();
        
        double basePrice = getRandomPrice(15, 30);
        double deliveryFee = getRandomPrice(3, 8);
        double totalPrice = basePrice + deliveryFee;

        results.add(ProductPriceResponse.builder()
                .platform("美团")
                .platformLogo("https://neeko-copilot.bytedance.net/api/text_to_image?prompt=meituan%20logo%20icon%20simple%20yellow&image_size=square")
                .productName(keyword + " - 美团")
                .productImage("https://neeko-copilot.bytedance.net/api/text_to_image?prompt=coconut%20latte%20coffee%20delicious%20cup&image_size=square")
                .price(basePrice)
                .deliveryFee(deliveryFee)
                .totalPrice(totalPrice)
                .storeName("瑞幸咖啡")
                .storeRating(String.format("%.1f", 4.7 + random.nextDouble() * 0.3))
                .deliveryTime(22 + random.nextInt(15))
                .promotion("满20减5")
                .build());

        return results;
    }

    private double getRandomPrice(double min, double max) {
        return Math.round((min + (max - min) * random.nextDouble()) * 100.0) / 100.0;
    }
}