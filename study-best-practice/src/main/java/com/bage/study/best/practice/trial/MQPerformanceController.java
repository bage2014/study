package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.trial.mq.RabbitMQPerformanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/mq/performance")
@RestController
@Slf4j
public class MQPerformanceController {

    @Autowired
    private RabbitMQPerformanceService rabbitMQPerformanceService;

    /**
     * 单线程发送性能测试
     */
    @RequestMapping("/single/send")
    public Object singleThreadSend(
            @RequestParam(value = "count", defaultValue = "1000") int count) {
        log.info("MQPerformanceController singleThreadSend count = {}", count);
        RabbitMQPerformanceService.PerformanceResult result = rabbitMQPerformanceService.singleThreadSend(count);
        log.info("Single thread send result: {}", result);
        return result;
    }

    /**
     * 多线程发送性能测试
     */
    @RequestMapping("/multi/send")
    public Object multiThreadSend(
            @RequestParam(value = "count", defaultValue = "1000") int count,
            @RequestParam(value = "threads", defaultValue = "10") int threadCount) {
        log.info("MQPerformanceController multiThreadSend count = {}, threadCount = {}", count, threadCount);
        RabbitMQPerformanceService.PerformanceResult result = rabbitMQPerformanceService.multiThreadSend(count, threadCount);
        log.info("Multi thread send result: {}", result);
        return result;
    }

    /**
     * 发送并接收性能测试
     */
    @RequestMapping("/send/receive")
    public Object sendAndReceive(
            @RequestParam(value = "count", defaultValue = "100") int count) {
        log.info("MQPerformanceController sendAndReceive count = {}", count);
        RabbitMQPerformanceService.PerformanceResult result = rabbitMQPerformanceService.sendAndReceive(count);
        log.info("Send and receive result: {}", result);
        return result;
    }
}
