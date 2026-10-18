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
}
