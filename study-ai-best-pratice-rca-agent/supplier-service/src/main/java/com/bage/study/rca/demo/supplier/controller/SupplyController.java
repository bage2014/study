package com.bage.study.rca.demo.supplier.controller;

import com.bage.study.rca.demo.supplier.model.SupplyCheckResult;
import com.bage.study.rca.demo.supplier.service.SupplyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 供应服务对外接口：
 * /api/supply/check 正常快速接口；
 * /api/supply/slow  人为慢响应（RCA：上游请求超时根因点）；
 * /api/supply/flaky 随机 500（RCA：下游错误率升高 / 级联失败）。
 */
@RestController
@RequestMapping("/api/supply")
public class SupplyController {

    private final SupplyService supplyService;

    public SupplyController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    @GetMapping("/check")
    public SupplyCheckResult check(@RequestParam String skuId,
                                   @RequestParam(defaultValue = "1") int quantity) {
        return supplyService.check(skuId, quantity);
    }

    /**
     * 模拟慢 SQL / 慢下游：服务端 sleep 指定毫秒数（封顶 60s）。
     * 订单服务 read-timeout=2s，millis 大于 2000 时上游必然超时。
     */
    @GetMapping("/slow")
    public Map<String, Object> slow(@RequestParam(defaultValue = "3000") long millis) throws InterruptedException {
        long requested = Math.min(millis, 60_000L);
        long start = System.currentTimeMillis();
        Thread.sleep(requested);
        return Map.of(
                "requestedSleepMs", requested,
                "elapsedMs", System.currentTimeMillis() - start,
                "thread", Thread.currentThread().toString()
        );
    }

    /**
     * 模拟不稳定节点：约 50% 概率抛 500。
     */
    @GetMapping("/flaky")
    public Map<String, Object> flaky() {
        if (ThreadLocalRandom.current().nextInt(100) < 50) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "simulated supply failure");
        }
        return Map.of("ok", true);
    }
}
