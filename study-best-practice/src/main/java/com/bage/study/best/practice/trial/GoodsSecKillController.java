package com.bage.study.best.practice.trial;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * wrk -t10 -c100 -d5s -T3s --latency http://localhost:8000/goods/buy
 *
 */
@RequestMapping("/goods")
@RestController
@Slf4j
public class GoodsSecKillController {

    private Map<Long, Integer> goodsMap = new ConcurrentHashMap<>();
    private int max = 1000;
    private Random random = new Random();

    @RequestMapping("/buy")
    public Object buy(@RequestParam(value = "userId", required = false) Long userId) {
        userId = userId == null ? random.nextLong(100000,999999) : userId;
        if (max > 0) {
            // 可以购买
            log.info("GoodsController buy ok = {}", userId);
            goodsMap.put(userId, goodsMap.getOrDefault(userId, 0) + 1);
            max--;
            return 1;
        }
        log.warn("GoodsController buy sold out = {}", userId);
        throw new RuntimeException("sold out");
    }


    @RequestMapping("/query/all")
    public Object queryAll() {
        log.info("GoodsController queryAll max = {}", max);
        log.info("GoodsController queryAll size = {}", goodsMap.size());
        log.info("GoodsController queryAll goodsMap = {}", goodsMap.toString());
        return goodsMap;
    }


}
