package com.bage.study.ai.best.practice.exception.analysis.controller;

import com.bage.study.ai.best.practice.exception.analysis.dto.request.AnalysisRequest;
import com.bage.study.ai.best.practice.exception.analysis.dto.response.AnalysisResponse;
import com.bage.study.ai.best.practice.exception.analysis.service.AnalysisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResponse> analyze(@Valid @RequestBody AnalysisRequest request) {
        AnalysisResponse response = analysisService.analyze(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Mock分析接口，支持不同场景参数
     * @param scene 场景类型：
     *   - high_error: 高错误率场景
     *   - mq_backlog: MQ积压场景
     *   - cpu_overload: CPU过载场景
     *   - memory_full: 内存不足场景
     *   - deployment_issue: 发布问题场景
     *   - module_exception: 模块异常场景
     *   - disturbance: 干扰项场景（包含不相关模块错误）
     *   - default: 默认场景（下单模块告警）
     */
    @GetMapping("/analyze/mock")
    public ResponseEntity<AnalysisResponse> analyzeMock(
            @RequestParam(value = "scene", defaultValue = "default") String scene) {
        AnalysisRequest mockRequest = buildMockRequest(scene);
        AnalysisResponse response = analysisService.analyze(mockRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "exception-analysis");
        return ResponseEntity.ok(health);
    }

    private AnalysisRequest buildMockRequest(String scene) {
        AnalysisRequest request = new AnalysisRequest();
        request.setAppId("app-mock-001");
        request.setAlarmUrl("https://monitor.example.com/alarm/mock-" + scene);
        request.setAlarmTime(LocalDateTime.now().minusMinutes(30));
        request.setScene(scene);

        switch (scene) {
            case "high_error":
                request.setAlarmDescription("下单模块用户下单失败率突然升高，超过阈值");
                break;
            case "mq_backlog":
                request.setAlarmDescription("下单模块用户下单失败率突然升高，MQ消息处理延迟");
                break;
            case "cpu_overload":
                request.setAlarmDescription("订单中心服务CPU使用率超过90%，响应缓慢");
                break;
            case "memory_full":
                request.setAlarmDescription("订单中心服务内存使用率超过95%，可能存在内存泄漏");
                break;
            case "deployment_issue":
                request.setAlarmDescription("订单中心服务发布新版本后出现异常");
                break;
            case "module_exception":
                request.setAlarmDescription("下单模块下单接口调用超时");
                break;
            case "disturbance":
                request.setAlarmDescription("下单模块用户下单失败率突然升高，超过阈值");
                break;
            case "payment_mq_backlog":
                request.setAlarmDescription("下单模块用户下单失败率突然升高");
                break;
            default:
                request.setAlarmDescription("下单模块用户下单失败率突然升高，超过阈值");
                break;
        }

        return request;
    }
}