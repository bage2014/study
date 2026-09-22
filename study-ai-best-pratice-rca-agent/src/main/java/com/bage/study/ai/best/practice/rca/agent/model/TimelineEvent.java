package com.bage.study.ai.best.practice.rca.agent.model;

import java.time.LocalDateTime;

/**
 * 统一时间线上的事件：告警 / 变更 / 异常信号，按时间排序暴露"变更 → 故障"的时序因果。
 */
public record TimelineEvent(
        LocalDateTime time,
        Kind kind,
        String source,
        String relatedTarget,
        String description
) {
    public enum Kind {
        ALERT,
        CHANGE,
        SIGNAL
    }
}
