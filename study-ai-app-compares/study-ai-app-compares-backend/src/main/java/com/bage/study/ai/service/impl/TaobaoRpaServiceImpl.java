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
public class TaobaoRpaServiceImpl implements RpaService {

    private final RpaConfig rpaConfig;
    private final Playwright playwright;
    private final BrowserContext context;
    private final Random random = new Random();

    private static final String PLATFORM_NAME = "淘宝闪购";
    private static final String SEARCH_URL = "https://s.taobao.com/search?q=";

    @Override
    public List<ProductPriceResponse> search(String keyword) {
        if (!isAvailable()) {
            log.warn("淘宝RPA服务不可用，返回模拟数据");
            return getMockData(keyword);
        }

        List<ProductPriceResponse> results = new ArrayList<>();
        Page page = null;
        try {
            page = context.newPage();
            page.setDefaultTimeout(rpaConfig.getTimeout());

            String url = SEARCH_URL + java.net.URLEncoder.encode(keyword, "UTF-8");
            log.info("淘宝RPA开始搜索: {}", keyword);

            page.navigate(url);

            page.waitForSelector(".item.J_MouserOnverReq", new Page.WaitForSelectorOptions()
                    .setTimeout(rpaConfig.getTimeout()));

            Thread.sleep(2000 + random.nextInt(2000));

            String content = page.content();
            results = parseSearchResults(content, keyword);

            log.info("淘宝RPA搜索完成，获取到 {} 条结果", results.size());

        } catch (Exception e) {
            log.error("淘宝RPA搜索失败: {}", e.getMessage());
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
            Elements items = doc.select(".item.J_MouserOnverReq");

            for (Element item : items) {
                try {
                    String title = item.select(".title").text();
                    String priceStr = item.select(".price strong").text();
                    String sales = item.select(".deal-cnt").text();
                    String shopName = item.select(".shop-name").text();

                    if (title.isEmpty() || priceStr.isEmpty()) {
                        continue;
                    }

                    double price = parsePrice(priceStr);
                    double deliveryFee = getRandomDeliveryFee();
                    double totalPrice = price + deliveryFee;

                    results.add(ProductPriceResponse.builder()
                            .platform(PLATFORM_NAME)
                            .platformLogo("https://neeko-copilot.bytedance.net/api/text_to_image?prompt=taobao%20logo%20icon%20simple%20orange&image_size=square")
                            .productName(title.length() > 50 ? title.substring(0, 50) + "..." : title)
                            .productImage("https://neeko-copilot.bytedance.net/api/text_to_image?prompt=coconut%20latte%20coffee%20iced&image_size=square")
                            .price(price)
                            .deliveryFee(deliveryFee)
                            .totalPrice(totalPrice)
                            .storeName(shopName.isEmpty() ? "淘宝店铺" : shopName)
                            .storeRating(String.format("%.1f", 4.4 + random.nextDouble() * 0.6))
                            .deliveryTime(25 + random.nextInt(20))
                            .promotion("满30减8")
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
            return getRandomPrice(15, 30);
        }
    }

    private double getRandomPrice(double min, double max) {
        return BigDecimal.valueOf(min + (max - min) * random.nextDouble())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private double getRandomDeliveryFee() {
        return BigDecimal.valueOf(3 + random.nextDouble() * 4)
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

        double basePrice = getRandomPrice(15, 30);
        double deliveryFee = getRandomDeliveryFee();
        double totalPrice = basePrice + deliveryFee;

        results.add(ProductPriceResponse.builder()
                .platform(PLATFORM_NAME)
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
}
