package com.bage.study.ai.best.practice.rca.agent.scan;

import com.bage.study.ai.best.practice.rca.agent.dto.RcaRequest;
import com.bage.study.ai.best.practice.rca.agent.model.Evidence;
import com.bage.study.ai.best.practice.rca.agent.model.Hypothesis;
import com.bage.study.ai.best.practice.rca.agent.playbook.ScanPlaybook;
import com.bage.study.ai.best.practice.rca.agent.tool.ToolQuery;
import com.bage.study.ai.best.practice.rca.agent.tool.ToolRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * FocusedScan：BroadAnalyze 后对 topN 假设递归下钻，工具集来自 FOCUSED_PLAYBOOK。
 */
@Service
public class FocusedScanService {

    private static final Logger log = LoggerFactory.getLogger(FocusedScanService.class);

    private final ToolRegistry registry;
    private final ScanPlaybook playbook;

    public FocusedScanService(ToolRegistry registry, ScanPlaybook playbook) {
        this.registry = registry;
        this.playbook = playbook;
    }

    public List<Evidence> scan(RcaRequest request, List<Hypothesis> hypotheses,
                               ScanBundle broadBundle, int topN, int idStartSeq) {
        List<Hypothesis> targets = hypotheses.stream()
                .sorted(Comparator.comparingDouble(Hypothesis::getConfidence).reversed())
                .limit(topN)
                .toList();

        List<Evidence> focused = new ArrayList<>();
        int seq = idStartSeq;
        for (Hypothesis h : targets) {
            h.setFocused(true);
            Set<String> already = new HashSet<>(
                    broadBundle.collectedToolsByHypothesis().getOrDefault(h.getId(), List.of()));
            List<String> tools = playbook.focusedTools(h, already);
            ToolQuery query = new ToolQuery(request.getAppId(), h.getTargetName(),
                    request.getAlarmTime(), request.getScene());
            for (String toolName : tools) {
                if (registry.get(toolName) == null) {
                    continue;
                }
                registry.get(toolName).execute(query).snapshots().forEach(snapshot -> {
                    Evidence e = BroadScanService.toEvidence(snapshot, h.getId());
                    e.setPhase(Evidence.Phase.FOCUSED);
                    focused.add(e);
                });
                broadBundle.collectedToolsByHypothesis()
                        .computeIfAbsent(h.getId(), k -> new ArrayList<>()).add(toolName);
            }
        }
        for (Evidence e : focused) {
            e.setId("e" + seq++);
        }
        log.info("FocusedScan done: focusedHypotheses={}, focusedEvidences={}",
                targets.size(), focused.size());
        return focused;
    }
}
