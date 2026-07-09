package com.bage.study.ai.service.impl;

import com.bage.study.ai.config.RpaConfig;
import com.bage.study.ai.dto.response.ProductPriceResponse;
import com.bage.study.ai.service.RpaService;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class JdRpaServiceImpl implements RpaService {

    private final RpaConfig rpaConfig;
    private final Playwright playwright;
    private final BrowserContext context;
    private final Random random = new Random();

    private static final String PLATFORM_NAME = "京东外卖";
    private static final String SEARCH_URL = "https://search.jd.com/Search?keyword=";

    @Override
    public List<ProductPriceResponse> search(String keyword) {
        if (!isAvailable()) {
            log.warn("京东RPA服务不可用，返回模拟数据");
            return getMockData(keyword);
        }

        List<ProductPriceResponse> results = new ArrayList<>();
        Page page = null;
        try {
            page = context.newPage();
            page.setDefaultTimeout(rpaConfig.getTimeout());

            String url = SEARCH_URL + java.net.URLEncoder.encode(keyword, "UTF-8");
            log.info("京东RPA开始搜索: {}", keyword);

            page.navigate(url);

            page.waitForSelector(".gl-item", new Page.WaitForSelectorOptions()
                    .setTimeout(rpaConfig.getTimeout()));

            Thread.sleep(2000 + random.nextInt(2000));

            String content = page.content();
            results = parseSearchResults(content, keyword);

            log.info("京东RPA搜索完成，获取到 {} 条结果", results.size());

        } catch (Exception e) {
            log.error("京东RPA搜索失败: {}", e.getMessage());
            results = getMockData(keyword);
        } finally {
            if (page != null) {
                try {
                    page.close();
                } catch (Exception e) {
                    log.error("关闭页面失败: {}", e.getMessage());
                }
            }
        }

        return results;
    }

    private List<ProductPriceResponse> parseSearchResults(String content, String keyword) {
        List<ProductPriceResponse> results = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(content);
            Elements items = doc.select(".gl-item");

            for (Element item : items) {
                try {
                    String title = item.select(".p-name em").text();
                    String priceStr = item.select(".p-price i").text();
                    String shopName = item.select(".p-shop a").text();

                    if (title.isEmpty() || priceStr.isEmpty()) {
                        continue;
                    }

                    double price = parsePrice(priceStr);
                    double deliveryFee = getRandomDeliveryFee();
                    double totalPrice = price + deliveryFee;

                    results.add(ProductPriceResponse.builder()
                            .platform(PLATFORM_NAME)
                            .platformLogo("https://neeko-copilot.bytedance.net/api/text_to_image?prompt=jd%20logo%20icon%20simple%20red&image_size=square")
                            .productName(title.length() > 50 ? title.substring(0, 50) + "..." : title)
                            .productImage("https://neeko-copilot.bytedance.net/api/text_to_image?prompt=coconut%20latte%20coffee%20cup%20premium&image_size=square")
                            .price(price)
                            .deliveryFee(deliveryFee)
                            .totalPrice(totalPrice)
                            .storeName(shopName.isEmpty() ? "京东店铺" : shopName)
                            .storeRating(String.format("%.1f", 4.5 + random.nextDouble() * 0.5))
                            .deliveryTime(20 + random.nextInt(15))
                            .promotion("新用户立减10")
                            .build());

                    if (results.size() >= 3) {
                        break;
                    }
                } catch (Exception e) {
                    log.debug("解析商品失败: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("解析搜索结果失败: {}", e.getMessage());
        }

        return results.isEmpty() ? getMockData(keyword) : results;
    }

    private double parsePrice(String priceStr) {
        try {
            String cleanPrice = priceStr.replaceAll("[^0-9.]", "");
            return BigDecimal.valueOf(Double.parseDouble(cleanPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (Exception e) {
            return getRandomPrice(16, 32);
        }
    }

    private double getRandomPrice(double min, double max) {
        return BigDecimal.valueOf(min + (max - min) * random.nextDouble())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private double getRandomDeliveryFee() {
        return BigDecimal.valueOf(2 + random.nextDouble() * 4)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Override
    public String getPlatformName() {
        return PLATFORM_NAME;
    }

    @Override
    public boolean isAvailable() {
        return rpaConfig.isRpaEnabled() && playwright != null && context != null;
    }

    private List<ProductPriceResponse> getMockData(String keyword) {
        List<ProductPriceResponse> results = new ArrayList<>();

        double basePrice = getRandomPrice(16, 32);
        double deliveryFee = getRandomDeliveryFee();
        double totalPrice = basePrice + deliveryFee;

        results.add(ProductPriceResponse.builder()
                .platform(PLATFORM_NAME)
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
}
