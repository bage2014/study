package com.bage.study.ai.best.practice.rca.agent.controller;

import com.bage.study.ai.best.practice.rca.agent.dto.RcaRequest;
import com.bage.study.ai.best.practice.rca.agent.dto.RcaResponse;
import com.bage.study.ai.best.practice.rca.agent.service.RcaOrchestrator;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/rca")
public class RcaController {

    private final RcaOrchestrator orchestrator;

    public RcaController(RcaOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    /**
     * 执行 RCA 分析：Plan → BroadScan → BroadAnalyze → FocusedScan → FocusedAnalyze → GlobalReact。
     */
    @PostMapping("/analyze")
    public ResponseEntity<RcaResponse> analyze(@Valid @RequestBody RcaRequest request) {
        return ResponseEntity.ok(orchestrator.analyze(request));
    }

    /**
     * Mock 场景便捷接口。scene 可选：
     * default / db_slow_query / mq_lag / app_resource / supplier_failure / high_error / redis
     */
    @GetMapping("/analyze/mock")
    public ResponseEntity<RcaResponse> analyzeMock(
            @RequestParam(value = "scene", defaultValue = "default") String scene,
            @RequestParam(value = "enableLlm", defaultValue = "false") boolean enableLlm) {
        return ResponseEntity.ok(orchestrator.analyze(buildMockRequest(scene, enableLlm)));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "rca-agent");
        return ResponseEntity.ok(health);
    }

    private RcaRequest buildMockRequest(String scene, boolean enableLlm) {
        RcaRequest request = new RcaRequest();
        request.setAppId("order-service");
        request.setScene(scene);
        request.setAlarmTime(LocalDateTime.now());
        request.setEnableLlm(enableLlm);
        request.setAlarmDescription(switch (scene) {
            case "db_slow_query" -> "下单接口大量超时，P99 延迟飙升，疑似慢查询";
            case "mq_lag" -> "订单状态更新延迟，监控显示 MQ 消息堆积，消费者 lag 持续增长";
            case "app_resource" -> "订单中心服务 CPU 使用率超过 90%，线程池打满，频繁 Full GC";
            case "supplier_failure" -> "支付环节调用供应商接口大量失败，外部依赖不可用";
            case "high_error" -> "下单接口错误率突增，日志中大量空指针异常";
            case "redis" -> "热点查询延迟升高，Redis 命中率下降，内存接近上限";
            default -> "下单模块用户下单失败率突然升高，超过阈值";
        });
        return request;
    }
}
