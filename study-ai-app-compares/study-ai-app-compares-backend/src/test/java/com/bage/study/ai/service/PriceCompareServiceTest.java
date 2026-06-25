package com.bage.study.ai.service;

import com.bage.study.ai.dto.response.CompareResultResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PriceCompareServiceTest {

    @Autowired
    private PriceCompareService priceCompareService;

    @Test
    void testComparePricesWithKeyword() {
        CompareResultResponse result = priceCompareService.comparePrices("生椰拿铁", null);
        
        assertNotNull(result);
        assertEquals("生椰拿铁", result.getProductName());
        assertNotNull(result.getPrices());
        assertTrue(result.getPrices().size() >= 3);
        assertNotNull(result.getBestRecommendation());
    }

    @Test
    void testComparePricesWithAddress() {
        CompareResultResponse result = priceCompareService.comparePrices("汉堡", "1");
        
        assertNotNull(result);
        assertEquals("汉堡", result.getProductName());
        assertTrue(result.getAddress().contains("北京市"));
    }

    @Test
    void testComparePricesWithEmptyKeyword() {
        assertThrows(Exception.class, () -> {
            priceCompareService.comparePrices("", null);
        });
    }
}