package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.trial.redis.RedisPerformanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/redis/performance")
@RestController
@Slf4j
public class RedisPerformanceController {

    @Autowired
    private RedisPerformanceService redisPerformanceService;

    /**
     * 单线程写入性能测试
     */
    @RequestMapping("/single/write")
    public Object singleThreadWrite(
            @RequestParam(value = "count", defaultValue = "1000") int count) {
        log.info("RedisPerformanceController singleThreadWrite count = {}", count);
        RedisPerformanceService.PerformanceResult result = redisPerformanceService.singleThreadWrite(count);
        log.info("Single thread write result: {}", result);
        return result;
    }

    /**
     * 多线程写入性能测试
     */
    @RequestMapping("/multi/write")
    public Object multiThreadWrite(
            @RequestParam(value = "count", defaultValue = "1000") int count,
            @RequestParam(value = "threads", defaultValue = "10") int threadCount) {
        log.info("RedisPerformanceController multiThreadWrite count = {}, threadCount = {}", count, threadCount);
        RedisPerformanceService.PerformanceResult result = redisPerformanceService.multiThreadWrite(count, threadCount);
        log.info("Multi thread write result: {}", result);
        return result;
    }

    /**
     * 单线程读取性能测试
     */
    @RequestMapping("/single/read")
    public Object singleThreadRead(
            @RequestParam(value = "count", defaultValue = "1000") int count) {
        log.info("RedisPerformanceController singleThreadRead count = {}", count);
        RedisPerformanceService.PerformanceResult result = redisPerformanceService.singleThreadRead(count);
        log.info("Single thread read result: {}", result);
        return result;
    }

    /**
     * 多线程读取性能测试
     */
    @RequestMapping("/multi/read")
    public Object multiThreadRead(
            @RequestParam(value = "count", defaultValue = "1000") int count,
            @RequestParam(value = "threads", defaultValue = "10") int threadCount) {
        log.info("RedisPerformanceController multiThreadRead count = {}, threadCount = {}", count, threadCount);
        RedisPerformanceService.PerformanceResult result = redisPerformanceService.multiThreadRead(count, threadCount);
        log.info("Multi thread read result: {}", result);
        return result;
    }

    /**
     * 混合读写性能测试
     */
    @RequestMapping("/mixed/read/write")
    public Object mixedReadWrite(
            @RequestParam(value = "count", defaultValue = "1000") int count,
            @RequestParam(value = "threads", defaultValue = "10") int threadCount,
            @RequestParam(value = "readRatio", defaultValue = "0.7") double readRatio) {
        log.info("RedisPerformanceController mixedReadWrite count = {}, threadCount = {}, readRatio = {}", 
                count, threadCount, readRatio);
        RedisPerformanceService.PerformanceResult result = redisPerformanceService.mixedReadWrite(count, threadCount, readRatio);
        log.info("Mixed read/write result: {}", result);
        return result;
    }

    /**
     * 清理测试数据
     */
    @RequestMapping("/cleanup")
    public Object cleanup() {
        log.info("RedisPerformanceController cleanup");
        redisPerformanceService.cleanup();
        return "Cleanup completed";
    }
}
