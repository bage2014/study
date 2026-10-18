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
                .dedupKey(dedupKey)
                .build();
    }

    // ── Dashboard 数据聚合 ────────────────────────────────────

    /** 指定天数内的总览数据 */
    public Map<String, Object> overview(int days) {
        Instant now = Instant.now();
        Instant start = now.minus(days, ChronoUnit.DAYS);

        Map<String, Object> result = new LinkedHashMap<>();

        // 总调用次数
        long total = repo.countByTsBetween(start, now);
        result.put("totalCalls", total);

        // 涉及的 Skill 数量
        Set<String> skills = repo.findByTsBetweenOrderByTsAsc(start, now).stream()
                .map(SkillUsage::getSkillName).collect(Collectors.toSet());
        result.put("uniqueSkills", skills.size());

        // 错误次数
        long errors = repo.findByTsBetweenOrderByTsAsc(start, now).stream()
                .filter(u -> "error".equals(u.getStatus())).count();
        result.put("errorCount", errors);
        result.put("errorRate", total > 0 ? String.format("%.1f%%", errors * 100.0 / total) : "0%");

        // 时间范围
        result.put("timeRange", Map.of("start", start.toString(), "end", now.toString()));

        return result;
    }

    /** Top N Skill 使用量 */
    public List<Map<String, Object>> topSkills(int days, int topN) {
        Instant now = Instant.now();
        Instant start = now.minus(days, ChronoUnit.DAYS);

        List<Object[]> rows = repo.topSkills(start, now, PageRequest.of(0, topN));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("skillName", row[0]);
            m.put("count", row[1]);
            result.add(m);
        }
        return result;
    }

    /** 每日调用趋势 */
    public List<Map<String, Object>> dailyTrend(int days) {
        Instant now = Instant.now();
        Instant start = now.minus(days, ChronoUnit.DAYS);

        List<Object[]> rows = repo.dailyTotalTrend(start, now);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("date", row[0]);
            m.put("count", row[1]);
            result.add(m);
        }
        return result;
    }

    /** 触发方式分布 */
    public List<Map<String, Object>> triggerDistribution(int days) {
        Instant now = Instant.now();
        Instant start = now.minus(days, ChronoUnit.DAYS);

        List<Object[]> rows = repo.triggerDistribution(start, now);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("triggerType", row[0]);
            m.put("count", row[1]);
            result.add(m);
        }
        return result;
    }

    /** 客户端分布 */
    public List<Map<String, Object>> clientDistribution(int days) {
        Instant now = Instant.now();
        Instant start = now.minus(days, ChronoUnit.DAYS);

        List<Object[]> rows = repo.clientDistribution(start, now);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("client", row[0]);
            m.put("count", row[1]);
            result.add(m);
        }
        return result;
    }

    /** 最新 N 条记录（按时间倒序） */
    public List<SkillUsage> recent(int limit) {
        return repo.findAll(PageRequest.of(0, limit, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "ts"))).getContent();
    }
}
