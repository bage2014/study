package com.bage.study.best.practice.trial.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Redis性能测试控制器
 */
@RequestMapping("/redis")
@RestController
@Slf4j
public class RedisPerformanceController {

    @Autowired
    private RedisPerformanceService redisPerformanceService;

    /**
     * 单线程写入测试
     */
    @RequestMapping("/performance/single/write")
    public Object singleThreadWrite(@RequestParam("count") int count) {
        long start = System.currentTimeMillis();
        RedisPerformanceService.PerformanceResult result = redisPerformanceService.singleThreadWrite(count);
        long end = System.currentTimeMillis();
        log.info("RedisPerformanceController singleThreadWrite cost = {}, result = {}", (end - start), result);
        return result;
    }

    /**
     * 多线程写入测试
     */
    @RequestMapping("/performance/multi/write")
    public Object multiThreadWrite(@RequestParam("count") int count, @RequestParam("threads") int threads) {
        long start = System.currentTimeMillis();
        RedisPerformanceService.PerformanceResult result = redisPerformanceService.multiThreadWrite(count, threads);
        long end = System.currentTimeMillis();
        log.info("RedisPerformanceController multiThreadWrite cost = {}, result = {}", (end - start), result);
        return result;
    }

    /**
     * 单线程读取测试
     */
    @RequestMapping("/performance/single/read")
    public Object singleThreadRead(@RequestParam("count") int count) {
        long start = System.currentTimeMillis();
        RedisPerformanceService.PerformanceResult result = redisPerformanceService.singleThreadRead(count);
        long end = System.currentTimeMillis();
        log.info("RedisPerformanceController singleThreadRead cost = {}, result = {}", (end - start), result);
        return result;
    }

    /**
     * 多线程读取测试
     */
    @RequestMapping("/performance/multi/read")
    public Object multiThreadRead(@RequestParam("count") int count, @RequestParam("threads") int threads) {
        long start = System.currentTimeMillis();
        RedisPerformanceService.PerformanceResult result = redisPerformanceService.multiThreadRead(count, threads);
        long end = System.currentTimeMillis();
        log.info("RedisPerformanceController multiThreadRead cost = {}, result = {}", (end - start), result);
        return result;
    }

    /**
     * 混合读写测试
     */
    @RequestMapping("/performance/mixed/read/write")
    public Object mixedReadWrite(@RequestParam("count") int count, @RequestParam("threads") int threads, @RequestParam("readRatio") double readRatio) {
        long start = System.currentTimeMillis();
        RedisPerformanceService.PerformanceResult result = redisPerformanceService.mixedReadWrite(count, threads, readRatio);
        long end = System.currentTimeMillis();
        log.info("RedisPerformanceController mixedReadWrite cost = {}, result = {}", (end - start), result);
        return result;
    }

    /**
     * 生成大key
     */
    @RequestMapping("/performance/big/key/generate")
    public Object generateBigKey(@RequestParam("size") int size) {
        long start = System.currentTimeMillis();
        String key = redisPerformanceService.generateBigKey(size);
        long end = System.currentTimeMillis();
        log.info("RedisPerformanceController generateBigKey cost = {}, key = {}", (end - start), key);
        return "Generated big key: " + key;
    }

    /**
     * 生成大List
     */
    @RequestMapping("/performance/big/list/generate")
    public Object generateBigListKey(@RequestParam("size") int size) {
        long start = System.currentTimeMillis();
        String key = redisPerformanceService.generateBigListKey(size);
        long end = System.currentTimeMillis();
        log.info("RedisPerformanceController generateBigListKey cost = {}, key = {}", (end - start), key);
        return "Generated big list key: " + key;
    }

    /**
     * 模拟热点key访问
     */
    @RequestMapping("/performance/hot/key")
    public Object simulateHotKey(@RequestParam("count") int count, @RequestParam("threads") int threads) {
        long start = System.currentTimeMillis();
        RedisPerformanceService.PerformanceResult result = redisPerformanceService.simulateHotKey(count, threads);
        long end = System.currentTimeMillis();
        log.info("RedisPerformanceController simulateHotKey cost = {}, result = {}", (end - start), result);
        return result;
    }

    /**
     * 测试大key性能
     */
    @RequestMapping("/performance/big/key/test")
    public Object bigKeyPerformance(@RequestParam("size") int size) {
        long start = System.currentTimeMillis();
        RedisPerformanceService.PerformanceResult result = redisPerformanceService.bigKeyPerformance(size);
        long end = System.currentTimeMillis();
        log.info("RedisPerformanceController bigKeyPerformance cost = {}, result = {}", (end - start), result);
        return result;
    }

    /**
     * 清理测试数据
     */
    @RequestMapping("/performance/cleanup")
    public Object cleanup() {
        long start = System.currentTimeMillis();
        redisPerformanceService.cleanup();
        long end = System.currentTimeMillis();
        log.info("RedisPerformanceController cleanup cost = {}", (end - start));
        return "Cleanup completed";
    }

}