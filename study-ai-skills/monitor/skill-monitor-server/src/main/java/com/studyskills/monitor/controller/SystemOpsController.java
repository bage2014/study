package com.studyskills.monitor.controller;

import com.studyskills.monitor.service.SystemOpsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 系统运维 API：状态查看 + 关闭/重启/Cron 操作（Dashboard 按钮调用）
 */
@RestController
@RequestMapping("/api/skill-usage/system")
@RequiredArgsConstructor
public class SystemOpsController {

    private final SystemOpsService systemOps;

    /** 系统状态：server / cron / script / data pipeline */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        return ResponseEntity.ok(systemOps.status());
    }

    /** 关闭监控服务（detached，先返回响应再停止） */
    @PostMapping("/stop")
    public ResponseEntity<Map<String, Object>> stop() {
        return ResponseEntity.ok(systemOps.stopServer());
    }

    /** 重启监控服务（detached，预计 20-40s 恢复） */
    @PostMapping("/restart")
    public ResponseEntity<Map<String, Object>> restart() {
        return ResponseEntity.ok(systemOps.restartServer());
    }

    /** 安装 Cron（launchd 60s） */
    @PostMapping("/cron/start")
    public ResponseEntity<Map<String, Object>> cronStart() {
        return ResponseEntity.ok(systemOps.cronControl(true));
    }

    /** 卸载 Cron */
    @PostMapping("/cron/stop")
    public ResponseEntity<Map<String, Object>> cronStop() {
        return ResponseEntity.ok(systemOps.cronControl(false));
    }
}
