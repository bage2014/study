package com.bage.study.ai.best.practice.rca.agent.scan;

import com.bage.study.ai.best.practice.rca.agent.model.Evidence;
import com.bage.study.ai.best.practice.rca.agent.model.TimelineEvent;

import java.util.List;
import java.util.Map;

/**
 * BroadScan 产物：证据列表 + 变更事件 + 每个假设已采集的工具集（供 FocusedScan 去重）。
 */
public record ScanBundle(
        List<Evidence> evidences,
        List<TimelineEvent> changeEvents,
        Map<String, List<String>> collectedToolsByHypothesis
) {
}
