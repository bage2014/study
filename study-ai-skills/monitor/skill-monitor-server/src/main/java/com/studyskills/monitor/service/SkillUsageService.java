package com.studyskills.monitor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyskills.monitor.entity.SkillUsage;
import com.studyskills.monitor.repository.SkillUsageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkillUsageService {

    private final SkillUsageRepository repo;
    private final ObjectMapper mapper;

    /**
     * 批量保存（去重）
     * @return 成功条数（跳过重复的不计）
     */
    @Transactional
    public int batchSave(List<SkillUsage> records) {
        int saved = 0;
        for (SkillUsage r : records) {
            if (!repo.existsByDedupKey(r.getDedupKey())) {
                repo.save(r);
                saved++;
            }
        }
        log.info("批量上传: 收到 {} 条, 新增 {} 条, 跳过重复 {}", records.size(), saved, records.size() - saved);
        return saved;
    }

    /** 解析 JSONL 为 SkillUsage 列表 */
    public List<SkillUsage> parseJsonl(String jsonl) {
        List<SkillUsage> result = new ArrayList<>();
        for (String line : jsonl.split("\n")) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = mapper.readValue(line, Map.class);
                result.add(toEntity(map));
            } catch (JsonProcessingException e) {
                log.warn("跳过无效 JSONL 行: {}", line);
            }
        }
        return result;
    }

    private SkillUsage toEntity(Map<String, Object> map) {
        Instant ts = Instant.parse((String) map.getOrDefault("ts", Instant.now().toString()));
        String skillName = (String) map.getOrDefault("skill", "unknown");
        String eventType = (String) map.getOrDefault("event", "tool_load");
        String client = (String) map.getOrDefault("client", "trae-macos");
        String sessionId = (String) map.getOrDefault("session", "no-session");
        String status = (String) map.getOrDefault("status", "ok");
        String triggerType = (String) map.getOrDefault("trigger", "manual");
        String userId = (String) map.getOrDefault("user", "unknown");
        String projectName = (String) map.getOrDefault("project", "unknown");
        Long durationMs = map.get("duration_ms") != null
                ? ((Number) map.get("duration_ms")).longValue()
                : 0L;

        String dedupKey = ts + "|" + skillName + "|" + client + "|" + eventType + "|" + sessionId;

        return SkillUsage.builder()
                .ts(ts)
                .skillName(skillName)
                .eventType(eventType)
                .client(client)
                .sessionId(sessionId)
                .durationMs(durationMs)
                .status(status)
                .triggerType(triggerType)
                .userId(userId)
                .projectName(projectName)
                .dedupKey(dedupKey)
                .build();
    }

    // ── Dashboard 数据聚合（user/project 为可选过滤条件，null 表示不过滤）────

    /** 总览数据 */
    public Map<String, Object> overview(int days, String user, String project) {
        Instant now = Instant.now();
        Instant start = now.minus(days, ChronoUnit.DAYS);

        List<SkillUsage> records = repo.findFiltered(start, now, user, project);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalCalls", records.size());
        result.put("uniqueSkills", records.stream().map(SkillUsage::getSkillName).collect(Collectors.toSet()).size());
        result.put("uniqueUsers", records.stream().map(SkillUsage::getUserId).filter(Objects::nonNull).collect(Collectors.toSet()).size());
        result.put("uniqueProjects", records.stream().map(SkillUsage::getProjectName).filter(Objects::nonNull).collect(Collectors.toSet()).size());

        long errors = records.stream().filter(u -> "error".equals(u.getStatus())).count();
        result.put("errorCount", errors);
        result.put("errorRate", !records.isEmpty() ? String.format("%.1f%%", errors * 100.0 / records.size()) : "0%");
        result.put("timeRange", Map.of("start", start.toString(), "end", now.toString()));
        return result;
    }

    /** Top N Skill 使用量 */
    public List<Map<String, Object>> topSkills(int days, int topN, String user, String project) {
        Instant now = Instant.now();
        Instant start = now.minus(days, ChronoUnit.DAYS);
        return toNameCount(repo.topSkills(start, now, user, project, PageRequest.of(0, topN)), "skillName");
    }

    /** Top N 项目（次数 + 涉及 Skill 数） */
    public List<Map<String, Object>> topProjects(int days, int topN, String user) {
        Instant now = Instant.now();
        Instant start = now.minus(days, ChronoUnit.DAYS);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : repo.topProjects(start, now, user, PageRequest.of(0, topN))) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("projectName", row[0]);
            m.put("count", row[1]);
            m.put("skills", row[2]);
            result.add(m);
        }
        return result;
    }

    /** Top N 用户（次数 + 涉及项目数） */
    public List<Map<String, Object>> topUsers(int days, int topN, String project) {
        Instant now = Instant.now();
        Instant start = now.minus(days, ChronoUnit.DAYS);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : repo.topUsers(start, now, project, PageRequest.of(0, topN))) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("userId", row[0]);
            m.put("count", row[1]);
            m.put("projects", row[2]);
            result.add(m);
        }
        return result;
    }

    /** 每日调用趋势（零填充：无数据的日期返回 0，保证图表连续） */
    public List<Map<String, Object>> dailyTrend(int days, String user, String project) {
        Instant now = Instant.now();
        Instant start = now.minus(days, ChronoUnit.DAYS);

        Map<String, Long> byDate = new HashMap<>();
        for (Object[] row : repo.dailyTotalTrend(start, now, user, project)) {
            byDate.put((String) row[0], ((Number) row[1]).longValue());
        }

        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        for (int i = days - 1; i >= 0; i--) {
            String date = today.minusDays(i).toString();
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("date", date);
            m.put("count", byDate.getOrDefault(date, 0L));
            result.add(m);
        }
        return result;
    }

    /** 触发方式分布 */
    public List<Map<String, Object>> triggerDistribution(int days, String user, String project) {
        Instant now = Instant.now();
        Instant start = now.minus(days, ChronoUnit.DAYS);
        return toNameCount(repo.triggerDistribution(start, now, user, project), "triggerType");
    }

    /** 客户端分布 */
    public List<Map<String, Object>> clientDistribution(int days, String user, String project) {
        Instant now = Instant.now();
        Instant start = now.minus(days, ChronoUnit.DAYS);
        return toNameCount(repo.clientDistribution(start, now, user, project), "client");
    }

    private List<Map<String, Object>> toNameCount(List<Object[]> rows, String nameKey) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put(nameKey, row[0]);
            m.put("count", row[1]);
            result.add(m);
        }
        return result;
    }

    /** 筛选下拉选项（全部去重用户 + 项目） */
    public Map<String, Object> filterOptions() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("users", repo.distinctUsers());
        result.put("projects", repo.distinctProjects());
        return result;
    }

    /** 最新 N 条记录（按时间倒序，可选用户/项目过滤） */
    public List<SkillUsage> recent(int limit, String user, String project) {
        return repo.recentFiltered(user, project, PageRequest.of(0, limit));
    }

    /** 最新记录按用户分组（时间倒序，每组内保持时间倒序） */
    public List<Map<String, Object>> recentByUser(int limit, String user, String project) {
        List<SkillUsage> records = recent(limit, user, project);
        Map<String, List<SkillUsage>> grouped = new LinkedHashMap<>();
        for (SkillUsage r : records) {
            String u = r.getUserId() != null ? r.getUserId() : "unknown";
            grouped.computeIfAbsent(u, k -> new ArrayList<>()).add(r);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<SkillUsage>> e : grouped.entrySet()) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("userId", e.getKey());
            m.put("records", e.getValue());
            result.add(m);
        }
        return result;
    }

    /** 错误率趋势（按天，返回日期/总数/错误数/错误率） */
    public List<Map<String, Object>> errorTrend(int days, String user, String project) {
        Instant now = Instant.now();
        Instant start = now.minus(days, ChronoUnit.DAYS);

        Map<String, long[]> byDate = new HashMap<>();
        for (Object[] row : repo.errorTrend(start, now, user, project)) {
            String date = (String) row[0];
            long total = ((Number) row[1]).longValue();
            long errors = ((Number) row[2]).longValue();
            byDate.put(date, new long[]{total, errors});
        }

        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        for (int i = days - 1; i >= 0; i--) {
            String date = today.minusDays(i).toString();
            long[] counts = byDate.getOrDefault(date, new long[]{0, 0});
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("date", date);
            m.put("total", counts[0]);
            m.put("errors", counts[1]);
            m.put("errorRate", counts[0] > 0 ? String.format("%.1f%%", counts[1] * 100.0 / counts[0]) : "0%");
            result.add(m);
        }
        return result;
    }

    /** 延迟百分位统计（P50/P95/P99/Avg） */
    public Map<String, Object> latencyStats(int days, String user, String project) {
        Instant now = Instant.now();
        Instant start = now.minus(days, ChronoUnit.DAYS);

        List<Long> values = repo.latencyValues(start, now, user, project);
        Map<String, Object> result = new LinkedHashMap<>();

        if (values.isEmpty()) {
            result.put("count", 0);
            result.put("p50", 0);
            result.put("p95", 0);
            result.put("p99", 0);
            result.put("avg", 0);
            result.put("max", 0);
            result.put("min", 0);
            return result;
        }

        int n = values.size();
        result.put("count", n);
        result.put("p50", values.get((int) (n * 0.50)));
        result.put("p95", values.get((int) (n * 0.95)));
        result.put("p99", values.get((int) Math.min(n * 0.99, n - 1)));
        result.put("avg", values.stream().mapToLong(Long::longValue).average().orElse(0));
        result.put("max", values.get(n - 1));
        result.put("min", values.get(0));
        return result;
    }

    /** Session 详情（按 sessionId 返回事件列表） */
    public Map<String, Object> sessionDetail(String sessionId) {
        List<SkillUsage> records = repo.findBySessionId(sessionId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sessionId", sessionId);
        result.put("eventCount", records.size());
        result.put("startTime", records.isEmpty() ? null : records.get(0).getTs().toString());
        result.put("endTime", records.isEmpty() ? null : records.get(records.size() - 1).getTs().toString());
        result.put("durationMs", records.size() >= 2
                ? Duration.between(records.get(0).getTs(), records.get(records.size() - 1).getTs()).toMillis()
                : 0);
        result.put("events", records);
        return result;
    }

    /** Session 列表（去重 sessionId + 基本信息） */
    public List<Map<String, Object>> sessionList(int days, String user, String project, int limit) {
        Instant now = Instant.now();
        Instant start = now.minus(days, ChronoUnit.DAYS);

        List<String> sessions = repo.distinctSessions(start, now, user, project);
        List<Map<String, Object>> result = new ArrayList<>();

        for (String sessionId : sessions.stream().limit(limit).collect(Collectors.toList())) {
            List<SkillUsage> records = repo.findBySessionId(sessionId);
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("sessionId", sessionId);
            m.put("eventCount", records.size());
            m.put("startTime", records.isEmpty() ? null : records.get(0).getTs().toString());
            m.put("skills", records.stream().map(SkillUsage::getSkillName).distinct().collect(Collectors.toList()));
            m.put("users", records.stream().map(SkillUsage::getUserId).filter(Objects::nonNull).distinct().collect(Collectors.toList()));
            result.add(m);
        }
        return result;
    }

    /** 时段热力图（返回 hour × dayOfWeek 的调用次数） */
    public List<Map<String, Object>> hourlyHeatmap(int days, String user, String project) {
        Instant now = Instant.now();
        Instant start = now.minus(days, ChronoUnit.DAYS);

        Map<String, Long> heatmap = new HashMap<>();
        for (Instant ts : repo.allTimestamps(start, now, user, project)) {
            LocalDate date = ts.atZone(ZoneOffset.UTC).toLocalDate();
            int hour = ts.atZone(ZoneOffset.UTC).getHour();
            int dayOfWeek = date.getDayOfWeek().getValue(); // 1=Monday, 7=Sunday
            // Convert to 1=Sunday, 2=Monday, ..., 7=Saturday for display
            int displayDay = dayOfWeek == 7 ? 1 : dayOfWeek + 1;
            heatmap.merge(hour + "-" + displayDay, 1L, Long::sum);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (int day = 1; day <= 7; day++) {
            for (int hour = 0; hour < 24; hour++) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("hour", hour);
                m.put("dayOfWeek", day);
                m.put("count", heatmap.getOrDefault(hour + "-" + day, 0L));
                result.add(m);
            }
        }
        return result;
    }

    /** 对比视图（当前周期 vs 上一周期） */
    public Map<String, Object> comparison(int days, String user, String project) {
        Instant now = Instant.now();
        Instant currentStart = now.minus(days, ChronoUnit.DAYS);
        Instant previousStart = currentStart.minus(days, ChronoUnit.DAYS);

        List<SkillUsage> currentRecords = repo.findFiltered(currentStart, now, user, project);
        List<SkillUsage> previousRecords = repo.findFiltered(previousStart, currentStart, user, project);

        Map<String, Object> current = new LinkedHashMap<>();
        current.put("totalCalls", currentRecords.size());
        current.put("uniqueSkills", currentRecords.stream().map(SkillUsage::getSkillName).collect(Collectors.toSet()).size());
        current.put("uniqueUsers", currentRecords.stream().map(SkillUsage::getUserId).filter(Objects::nonNull).collect(Collectors.toSet()).size());
        current.put("uniqueProjects", currentRecords.stream().map(SkillUsage::getProjectName).filter(Objects::nonNull).collect(Collectors.toSet()).size());
        long currentErrors = currentRecords.stream().filter(u -> "error".equals(u.getStatus())).count();
        current.put("errorCount", currentErrors);
        current.put("errorRate", !currentRecords.isEmpty() ? String.format("%.1f%%", currentErrors * 100.0 / currentRecords.size()) : "0%");

        Map<String, Object> previous = new LinkedHashMap<>();
        previous.put("totalCalls", previousRecords.size());
        previous.put("uniqueSkills", previousRecords.stream().map(SkillUsage::getSkillName).collect(Collectors.toSet()).size());
        previous.put("uniqueUsers", previousRecords.stream().map(SkillUsage::getUserId).filter(Objects::nonNull).collect(Collectors.toSet()).size());
        previous.put("uniqueProjects", previousRecords.stream().map(SkillUsage::getProjectName).filter(Objects::nonNull).collect(Collectors.toSet()).size());
        long previousErrors = previousRecords.stream().filter(u -> "error".equals(u.getStatus())).count();
        previous.put("errorCount", previousErrors);
        previous.put("errorRate", !previousRecords.isEmpty() ? String.format("%.1f%%", previousErrors * 100.0 / previousRecords.size()) : "0%");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("current", current);
        result.put("previous", previous);
        result.put("days", days);
        return result;
    }
}
