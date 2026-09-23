package com.bage.study.ai.best.practice.rca.agent.scan;

import com.bage.study.ai.best.practice.rca.agent.dto.RcaRequest;
import com.bage.study.ai.best.practice.rca.agent.model.Evidence;
import com.bage.study.ai.best.practice.rca.agent.model.Hypothesis;
import com.bage.study.ai.best.practice.rca.agent.model.MetricSnapshot;
import com.bage.study.ai.best.practice.rca.agent.model.TimelineEvent;
import com.bage.study.ai.best.practice.rca.agent.playbook.ScanPlaybook;
import com.bage.study.ai.best.practice.rca.agent.tool.ChangeEventTool;
import com.bage.study.ai.best.practice.rca.agent.tool.McpTool;
import com.bage.study.ai.best.practice.rca.agent.tool.ToolQuery;
import com.bage.study.ai.best.practice.rca.agent.tool.ToolRegistry;
import com.bage.study.ai.best.practice.rca.agent.tool.ToolResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * BroadScan：按假设并发采集（rca.md BroadScan）。
 * 假设间并发、同一假设的工具间并发；工具路由完全由 Playbook 决定，无 LLM 参与。
 */
@Service
public class BroadScanService {

    private static final Logger log = LoggerFactory.getLogger(BroadScanService.class);

    private final ToolRegistry registry;
    private final ScanPlaybook playbook;

    public BroadScanService(ToolRegistry registry, ScanPlaybook playbook) {
        this.registry = registry;
        this.playbook = playbook;
    }

    public ScanBundle scan(RcaRequest request, List<Hypothesis> hypotheses) {
        // 假设维度并发取数
        Map<String, List<Evidence>> byHypothesis = hypotheses.parallelStream()
                .collect(Collectors.toMap(
                        Hypothesis::getId,
                        h -> scanOneHypothesis(request, h),
                        (a, b) -> a,
                        LinkedHashMap::new));

        // 按假设顺序、工具顺序展平并分配确定性证据 ID
        List<Evidence> all = new ArrayList<>();
        Map<String, List<String>> collected = new LinkedHashMap<>();
        int seq = 1;
        for (Hypothesis h : hypotheses) {
            List<Evidence> list = byHypothesis.getOrDefault(h.getId(), List.of());
            List<String> toolNames = new ArrayList<>();
            for (Evidence e : list) {
                e.setId("e" + seq++);
                toolNames.add(e.getToolName());
                all.add(e);
            }
            collected.put(h.getId(), toolNames);
        }

        // 变更事件全量拉取一次（时间线重建/变更感知的原料）
        List<TimelineEvent> changeEvents = List.of();
        McpTool changeTool = registry.get(ChangeEventTool.NAME);
        if (changeTool != null) {
            ToolResult result = changeTool.execute(new ToolQuery(
                    request.getAppId(), request.getAppId(), request.getAlarmTime(), request.getScene()));
            changeEvents = result.changeEvents();
        }

        log.info("BroadScan done: hypotheses={}, evidences={}, changeEvents={}",
                hypotheses.size(), all.size(), changeEvents.size());
        return new ScanBundle(all, changeEvents, collected);
    }

    private List<Evidence> scanOneHypothesis(RcaRequest request, Hypothesis hypothesis) {
        List<String> toolNames = playbook.broadTools(hypothesis);
        ToolQuery query = new ToolQuery(request.getAppId(), hypothesis.getTargetName(),
                request.getAlarmTime(), request.getScene());
        // 同一假设的多个工具并发调用
        return toolNames.parallelStream()
                .map(registry::get)
                .filter(java.util.Objects::nonNull)
                .map(tool -> tool.execute(query))
                .filter(ToolResult::success)
                .flatMap(r -> r.snapshots().stream())
                .map(snapshot -> toEvidence(snapshot, hypothesis.getId()))
                .collect(Collectors.toList());
    }

    static Evidence toEvidence(MetricSnapshot snapshot, String hypothesisId) {
        Evidence e = new Evidence();
        e.setHypothesisId(hypothesisId);
        e.setPhase(Evidence.Phase.BROAD);
        e.setToolName(snapshot.toolName());
        e.setLayer(snapshot.layer());
        e.setTargetName(snapshot.targetName());
        e.setValue(snapshot.value());
        e.setBaseline(snapshot.baseline());
        e.setDeviationRatio(snapshot.deviationRatio());
        e.setAnomaly(snapshot.anomaly());
        e.setObservedAt(snapshot.observedAt());
        e.setSummary(String.format("%s[%s] 当前=%.2f%s 基线=%.2f%s 偏离=%.2fx —— %s",
                snapshot.toolName(), snapshot.targetName(),
                snapshot.value(), snapshot.unit(),
                snapshot.baseline(), snapshot.unit(),
                snapshot.deviationRatio(), snapshot.detail()));
        return e;
    }
}
