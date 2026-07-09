package com.bage.study.ai.service.impl;

import com.bage.study.ai.dto.response.ProductPriceResponse;
import com.bage.study.ai.service.RpaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RpaAggregateServiceImpl {

    private final TaobaoRpaServiceImpl taobaoRpaService;
    private final JdRpaServiceImpl jdRpaService;
    private final MeituanRpaServiceImpl meituanRpaService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    public List<ProductPriceResponse> search(String keyword) {
        List<ProductPriceResponse> results = new ArrayList<>();

        CompletableFuture<List<ProductPriceResponse>> taobaoFuture = CompletableFuture.supplyAsync(
                () -> taobaoRpaService.search(keyword), executorService);

        CompletableFuture<List<ProductPriceResponse>> jdFuture = CompletableFuture.supplyAsync(
                () -> jdRpaService.search(keyword), executorService);

        CompletableFuture<List<ProductPriceResponse>> meituanFuture = CompletableFuture.supplyAsync(
                () -> meituanRpaService.search(keyword), executorService);

        CompletableFuture.allOf(taobaoFuture, jdFuture, meituanFuture).join();

        try {
            results.addAll(taobaoFuture.get());
        } catch (Exception e) {
            log.error("获取淘宝RPA结果失败: {}", e.getMessage());
        }

        try {
            results.addAll(jdFuture.get());
        } catch (Exception e) {
            log.error("获取京东RPA结果失败: {}", e.getMessage());
        }

        try {
            results.addAll(meituanFuture.get());
        } catch (Exception e) {
            log.error("获取美团RPA结果失败: {}", e.getMessage());
        }

        log.info("RPA聚合搜索完成，共获取 {} 条结果", results.size());
        return results;
    }

    public boolean isAnyRpaAvailable() {
        return taobaoRpaService.isAvailable() || jdRpaService.isAvailable() || meituanRpaService.isAvailable();
    }

    public List<String> getAvailablePlatforms() {
        List<String> platforms = new ArrayList<>();
        if (taobaoRpaService.isAvailable()) {
            platforms.add(taobaoRpaService.getPlatformName());
        }
        if (jdRpaService.isAvailable()) {
            platforms.add(jdRpaService.getPlatformName());
        }
        if (meituanRpaService.isAvailable()) {
            platforms.add(meituanRpaService.getPlatformName());
        }
        return platforms;
    }
}
