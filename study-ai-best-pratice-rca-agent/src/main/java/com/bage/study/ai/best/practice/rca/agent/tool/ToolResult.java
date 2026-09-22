package com.bage.study.ai.best.practice.rca.agent.tool;

import com.bage.study.ai.best.practice.rca.agent.model.MetricSnapshot;
import com.bage.study.ai.best.practice.rca.agent.model.TimelineEvent;

import java.util.List;

/**
 * 工具执行结果：多数工具返回指标快照；变更类工具返回时间线事件。
 */
public record ToolResult(
        String toolName,
        boolean success,
        String error,
        List<MetricSnapshot> snapshots,
        List<TimelineEvent> changeEvents
) {
    public static ToolResult metrics(String toolName, List<MetricSnapshot> snapshots) {
        return new ToolResult(toolName, true, null, snapshots, List.of());
    }

    public static ToolResult changes(String toolName, List<TimelineEvent> events) {
        return new ToolResult(toolName, true, null, List.of(), events);
    }

    public static ToolResult error(String toolName, String error) {
        return new ToolResult(toolName, false, error, List.of(), List.of());
    }
}
