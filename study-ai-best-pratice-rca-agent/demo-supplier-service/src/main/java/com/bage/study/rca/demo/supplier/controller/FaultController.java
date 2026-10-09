package com.bage.study.rca.demo.supplier.controller;

import com.bage.study.rca.demo.supplier.fault.FaultInjector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 故障注入接口：
 * GET  /fault/oom?mb=10              一次性泄漏堆内存
 * POST /fault/oom/leak/start?mbPerSecond=5  持续泄漏
 * POST /fault/oom/leak/stop
 * POST /fault/oom/clear
 * GET  /fault/cpu?seconds=10         CPU 忙等
 * GET  /fault/threads?count=200      线程泄漏
 * POST /fault/reset                  清理全部注入故障
 */
@RestController
@RequestMapping("/fault")
public class FaultController {

    private final FaultInjector faultInjector;

    public FaultController(FaultInjector faultInjector) {
        this.faultInjector = faultInjector;
    }

    @GetMapping("/oom")
    public Map<String, Object> oom(@RequestParam(defaultValue = "10") int mb) {
        int blocks = faultInjector.leakOnce(mb);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("action", "leak-once");
        result.put("mb", mb);
        result.put("blocksHeld", blocks);
        result.put("hint", "重复调用持续堆积，直到 java.lang.OutOfMemoryError: Java heap space");
        return result;
    }

    @PostMapping("/oom/leak/start")
    public Map<String, Object> startLeak(@RequestParam(defaultValue = "5") int mbPerSecond) {
        boolean started = faultInjector.startLeak(mbPerSecond);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("action", "leak-start");
        result.put("started", started);
        result.put("mbPerSecond", mbPerSecond);
        result.put("hint", started ? "每秒持续泄漏，观察堆使用量爬升与 GC 频率" : "泄漏任务已在运行");
        return result;
    }

    @PostMapping("/oom/leak/stop")
    public Map<String, Object> stopLeak() {
        return Map.of("action", "leak-stop", "stopped", faultInjector.stopLeak());
    }

    @PostMapping("/oom/clear")
    public Map<String, Object> clearMemory() {
        return Map.of("action", "leak-clear", "blocksCleared", faultInjector.clearMemory());
    }

    @GetMapping("/cpu")
    public Map<String, Object> cpu(@RequestParam(defaultValue = "10") int seconds) {
        long start = System.currentTimeMillis();
        long blackhole = faultInjector.burnCpu(seconds);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("action", "cpu-burn");
        result.put("seconds", seconds);
        result.put("elapsedMs", System.currentTimeMillis() - start);
        result.put("blackhole", blackhole);
        return result;
    }

    @GetMapping("/threads")
    public Map<String, Object> threads(@RequestParam(defaultValue = "200") int count) {
        int total = faultInjector.spawnThreads(count);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("action", "thread-leak");
        result.put("requested", count);
        result.put("totalLeakedThreads", total);
        result.put("hint", "访问 /actuator/threaddump 观察 fault-leaked-thread-* 线程堆积");
        return result;
    }

    @PostMapping("/reset")
    public Map<String, Object> reset() {
        faultInjector.reset();
        return Map.of("action", "reset", "status", "all injected faults cleared");
    }
}
