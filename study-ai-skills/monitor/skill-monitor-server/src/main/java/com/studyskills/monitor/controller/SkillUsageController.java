package com.studyskills.monitor.controller;

import com.studyskills.monitor.entity.SkillUsage;
import com.studyskills.monitor.service.SkillUsageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/skill-usage")
@RequiredArgsConstructor
public class SkillUsageController {

    private final SkillUsageService service;

    /**
     * 批量上传 JSONL（客户端 curl POST 这个端点）
     * Body: 纯文本 JSONL，一行一条
     */
    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchUpload(
            @RequestBody String jsonl,
            @RequestHeader(value = "X-Client-Token", required = false) String token) {

        Map<String, Object> resp = new HashMap<>();

        // 解析 JSONL
        List<SkillUsage> records = service.parseJsonl(jsonl);
        if (records.isEmpty()) {
            resp.put("status", "skipped");
            resp.put("message", "没有有效 JSONL 数据");
            return ResponseEntity.ok(resp);
        }

        // 批量保存
        int saved = service.batchSave(records);
        resp.put("status", "ok");
        resp.put("received", records.size());
        resp.put("saved", saved);
        resp.put("skipped", records.size() - saved);

        return ResponseEntity.ok(resp);
    }

    /** 总览数据 */
    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> overview(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String project) {
        return ResponseEntity.ok(service.overview(days, norm(user), norm(project)));
    }

    /** Top N Skill */
    @GetMapping("/top")
    public ResponseEntity<List<Map<String, Object>>> top(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "10") int topN,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String project) {
        return ResponseEntity.ok(service.topSkills(days, topN, norm(user), norm(project)));
    }

    /** Top N 项目 */
    @GetMapping("/top-projects")
    public ResponseEntity<List<Map<String, Object>>> topProjects(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "10") int topN,
            @RequestParam(required = false) String user) {
        return ResponseEntity.ok(service.topProjects(days, topN, norm(user)));
    }

    /** Top N 用户 */
    @GetMapping("/top-users")
    public ResponseEntity<List<Map<String, Object>>> topUsers(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "10") int topN,
            @RequestParam(required = false) String project) {
        return ResponseEntity.ok(service.topUsers(days, topN, norm(project)));
    }

    /** 每日趋势 */
    @GetMapping("/daily-trend")
    public ResponseEntity<List<Map<String, Object>>> dailyTrend(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String project) {
        return ResponseEntity.ok(service.dailyTrend(days, norm(user), norm(project)));
    }

    /** 触发方式分布 */
    @GetMapping("/trigger-dist")
    public ResponseEntity<List<Map<String, Object>>> triggerDist(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String project) {
        return ResponseEntity.ok(service.triggerDistribution(days, norm(user), norm(project)));
    }

    /** 客户端分布 */
    @GetMapping("/client-dist")
    public ResponseEntity<List<Map<String, Object>>> clientDist(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String project) {
        return ResponseEntity.ok(service.clientDistribution(days, norm(user), norm(project)));
    }

    /** 最新记录 */
    @GetMapping("/recent")
    public ResponseEntity<List<SkillUsage>> recent(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String project) {
        return ResponseEntity.ok(service.recent(limit, norm(user), norm(project)));
    }

    /** 筛选下拉选项 */
    @GetMapping("/filters")
    public ResponseEntity<Map<String, Object>> filters() {
        return ResponseEntity.ok(service.filterOptions());
    }

    /** 最新记录（按用户分组） */
    @GetMapping("/recent-by-user")
    public ResponseEntity<List<Map<String, Object>>> recentByUser(
            @RequestParam(defaultValue = "30") int limit,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String project) {
        return ResponseEntity.ok(service.recentByUser(limit, norm(user), norm(project)));
    }

    /** 错误率趋势 */
    @GetMapping("/error-trend")
    public ResponseEntity<List<Map<String, Object>>> errorTrend(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String project) {
        return ResponseEntity.ok(service.errorTrend(days, norm(user), norm(project)));
    }

    /** 延迟百分位统计 */
    @GetMapping("/latency-stats")
    public ResponseEntity<Map<String, Object>> latencyStats(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String project) {
        return ResponseEntity.ok(service.latencyStats(days, norm(user), norm(project)));
    }

    /** Session 列表 */
    @GetMapping("/sessions")
    public ResponseEntity<List<Map<String, Object>>> sessionList(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String project) {
        return ResponseEntity.ok(service.sessionList(days, norm(user), norm(project), limit));
    }

    /** Session 详情 */
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<Map<String, Object>> sessionDetail(@PathVariable String sessionId) {
        return ResponseEntity.ok(service.sessionDetail(sessionId));
    }

    /** 时段热力图 */
    @GetMapping("/hourly-heatmap")
    public ResponseEntity<List<Map<String, Object>>> hourlyHeatmap(
            @RequestParam(defaultValue = "30") int days,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String project) {
        return ResponseEntity.ok(service.hourlyHeatmap(days, norm(user), norm(project)));
    }

    /** 对比视图（当前周期 vs 上一周期） */
    @GetMapping("/comparison")
    public ResponseEntity<Map<String, Object>> comparison(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String project) {
        return ResponseEntity.ok(service.comparison(days, norm(user), norm(project)));
    }

    /** 空串归一为 null */
    private String norm(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    /** 健康检查 */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("status", "ok");
        resp.put("time", System.currentTimeMillis());
        return ResponseEntity.ok(resp);
    }
}
