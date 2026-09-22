package com.bage.study.ai.best.practice.rca.agent.tool;

import java.time.LocalDateTime;

/**
 * 工具调用入参，所有工具统一通过假设的 targetName + 告警时间窗取数。
 */
public record ToolQuery(
        String appId,
        String targetName,
        LocalDateTime alarmTime,
        String scene
) {
}
