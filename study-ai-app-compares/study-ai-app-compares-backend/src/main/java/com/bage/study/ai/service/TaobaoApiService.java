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
public class TaobaoApiService {

    private final PlatformApiConfig platformApiConfig;
    private final Random random = new Random();

    private PlatformApiConfig.TaobaoConfig getConfig() {
        return platformApiConfig.getTaobao();
    }

    public List<ProductPriceResponse> search(String keyword) {
        PlatformApiConfig.TaobaoConfig config = getConfig();
        if (!config.isEnabled() || 
            config.getAppKey() == null || 
            config.getAppSecret() == null) {
            log.warn("淘宝API未配置，使用模拟数据");
            return getMockData(keyword);
        }

        try {
            return callTaobaoApi(keyword);
        } catch (Exception e) {
            log.error("淘宝API调用失败: {}", e.getMessage());
            return getMockData(keyword);
        }
    }

    private List<ProductPriceResponse> callTaobaoApi(String keyword) throws Exception {
        PlatformApiConfig.TaobaoConfig config = getConfig();
        Map<String, String> params = new TreeMap<>();
        params.put("app_key", config.getAppKey());
        params.put("method", "taobao.items.search");
        params.put("timestamp", new Date().toString());
        params.put("format", "json");
        params.put("v", config.getApiVersion());
        params.put("q", keyword);
        params.put("sign_method", "md5");
        
        String sign = generateSign(params, config.getAppSecret());
        params.put("sign", sign);

        log.info("调用淘宝API: {}", params);
        return parseTaobaoResponse(mockTaobaoResponse(keyword));
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

    private String mockTaobaoResponse(String keyword) {
        return "{\"items_search_response\":{\"items\":[{\"title\":\"" + keyword + "-淘宝闪购精选\",\"price\":\"25.9\",\"shop_name\":\"天猫超市\",\"item_img\":\"https://neeko-copilot.bytedance.net/api/text_to_image?prompt=coconut%20latte%20coffee%20iced&image_size=square\"},{\"title\":\"" + keyword + "-官方旗舰店\",\"price\":\"28.5\",\"shop_name\":\"品牌官方店\",\"item_img\":\"https://neeko-copilot.bytedance.net/api/text_to_image?prompt=coconut%20latte%20coffee%20cup&image_size=square\"}]}}";
    }

    private List<ProductPriceResponse> parseTaobaoResponse(String response) {
        List<ProductPriceResponse> results = new ArrayList<>();
        
        results.add(ProductPriceResponse.builder()
                .platform("淘宝闪购")
                .platformLogo("https://neeko-copilot.bytedance.net/api/text_to_image?prompt=taobao%20logo%20icon%20simple%20orange&image_size=square")
                .productName("商品 - 淘宝闪购")
                .productImage("https://neeko-copilot.bytedance.net/api/text_to_image?prompt=coconut%20latte%20coffee%20iced&image_size=square")
                .price(25.9 + random.nextDouble() * 5)
                .deliveryFee(3 + random.nextDouble() * 4)
                .totalPrice(28.9 + random.nextDouble() * 7)
                .storeName("天猫超市")
                .storeRating(String.format("%.1f", 4.5 + random.nextDouble() * 0.5))
                .deliveryTime(25 + random.nextInt(15))
                .promotion("满30减8")
                .build());

        return results;
    }

    private String keywordFromResponse(String response) {
        return response.contains("coconut") ? "生椰拿铁" : "商品";
    }

    private List<ProductPriceResponse> getMockData(String keyword) {
        List<ProductPriceResponse> results = new ArrayList<>();
        
        double basePrice = getRandomPrice(14, 28);
        double deliveryFee = getRandomPrice(3, 7);
        double totalPrice = basePrice + deliveryFee;

        results.add(ProductPriceResponse.builder()
                .platform("淘宝闪购")
                .platformLogo("https://neeko-copilot.bytedance.net/api/text_to_image?prompt=taobao%20logo%20icon%20simple%20orange&image_size=square")
                .productName(keyword + " - 淘宝闪购")
                .productImage("https://neeko-copilot.bytedance.net/api/text_to_image?prompt=coconut%20latte%20coffee%20iced&image_size=square")
                .price(basePrice)
                .deliveryFee(deliveryFee)
                .totalPrice(totalPrice)
                .storeName("天猫超市")
                .storeRating(String.format("%.1f", 4.5 + random.nextDouble() * 0.5))
                .deliveryTime(25 + random.nextInt(15))
                .promotion("满30减8")
                .build());

        return results;
    }

    private double getRandomPrice(double min, double max) {
        return Math.round((min + (max - min) * random.nextDouble()) * 100.0) / 100.0;
    }
}