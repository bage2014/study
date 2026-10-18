package com.studyskills.monitor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Skill 使用记录
 */
@Entity
@Table(name = "skill_usage", indexes = {
    @Index(name = "idx_skill_name", columnList = "skillName"),
    @Index(name = "idx_ts", columnList = "ts"),
    @Index(name = "idx_event", columnList = "eventType"),
    @Index(name = "idx_skill_ts", columnList = "skillName, ts")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 原始 timestamp（ISO 8601），用于去重唯一索引 */
    @Column(nullable = false)
    private Instant ts;

    /** Skill 名称 */
    @Column(nullable = false, length = 100)
    private String skillName;

    /** 事件类型：tool_load / script_log / validation */
    @Column(nullable = false, length = 30)
    private String eventType;

    /** 客户端：trae-macos / script / trae-* */
    @Column(nullable = false, length = 50)
    private String client;

    /** 会话 ID */
    @Column(length = 100)
    private String sessionId;

    /** 耗时（毫秒） */
    @Column
    private Long durationMs;

    /** 状态：ok / error */
    @Column(nullable = false, length = 10)
    private String status;

    /** 触发方式：manual / auto */
    @Column(nullable = false, length = 10)
    private String triggerType;

    /** 用户标识 */
    @Column(length = 50)
    private String userId;

    /** 项目标识（git 仓库根目录名或当前目录名） */
    @Column(length = 100)
    private String projectName;

    /** 唯一键：防止重复上传 */
    @Column(length = 100, unique = true)
    private String dedupKey;

    @PrePersist
    public void generateDedupKey() {
        if (dedupKey == null) {
            dedupKey = ts + "|" + skillName + "|" + client + "|" + eventType + "|" + sessionId;
        }
    }
}
