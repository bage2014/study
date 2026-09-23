package com.bage.study.ai.best.practice.rca.agent.analyze;

import com.bage.study.ai.best.practice.rca.agent.dto.RcaRequest;
import com.bage.study.ai.best.practice.rca.agent.model.Conclusion;
import com.bage.study.ai.best.practice.rca.agent.model.Evidence;
import com.bage.study.ai.best.practice.rca.agent.model.Hypothesis;
import com.bage.study.ai.best.practice.rca.agent.model.StageTrace;
import com.bage.study.ai.best.practice.rca.agent.model.Suggestions;
import com.bage.study.ai.best.practice.rca.agent.model.TimelineEvent;
import com.bage.study.ai.best.practice.rca.agent.model.Verdict;
import com.bage.study.ai.best.practice.rca.agent.playbook.ScanPlaybook;
import com.bage.study.ai.best.practice.rca.agent.scan.TimelineService;
import com.bage.study.ai.best.practice.rca.agent.topology.TopologyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * GlobalReact 全局收口（rca.md 验证与校准）：
 * 1. 拓扑感知：±N 跳外目标置信度打折（干扰项裁剪）；
 * 2. 变更感知：故障时间窗内同目标变更对假设加权；
 * 3. 反事实验证："若根因是 X，决定性信号应异常"，覆盖率不足则下调；
 * 4. 排序、分级、组装因果链与三层建议。
 */
@Service
public class GlobalReactService {

    private static final Logger log = LoggerFactory.getLogger(GlobalReactService.class);

    static final double CONFIRMED_THRESHOLD = 0.60;
    static final double SUSPECTED_THRESHOLD = 0.35;
    private static final double CHANGE_BOOST = 0.12;
    private static final double COUNTERFACTUAL_PENALTY = 0.15;
    private static final double OUT_OF_TOPOLOGY_FACTOR = 0.7;

    private final ScanPlaybook playbook;
    private final TimelineService timelineService;
    private final TopologyService topologyService;
    private final SuggestionService suggestionService;

    @Value("${rca.scan.change-window-minutes:30}")
    private int changeWindowMinutes;

    @Value("${rca.scan.topology-hops:2}")
    private int topologyHops;

    public GlobalReactService(ScanPlaybook playbook, TimelineService timelineService,
                              TopologyService topologyService, SuggestionService suggestionService) {
        this.playbook = playbook;
        this.timelineService = timelineService;
        this.topologyService = topologyService;
        this.suggestionService = suggestionService;
    }

    public ReactResult react(RcaRequest request, List<Hypothesis> hypotheses,
                             List<Evidence> evidences, List<TimelineEvent> changeEvents) {
        Map<String, List<Evidence>> byHypothesis = evidences.stream()
                .collect(Collectors.groupingBy(Evidence::getHypothesisId));

        for (Hypothesis h : hypotheses) {
            List<Evidence> mine = byHypothesis.getOrDefault(h.getId(), List.of());

            // 1) 拓扑因子
            boolean inTopology = topologyService.withinHops(
                    request.getAppId(), h.getTargetName(), topologyHops);
            double topologyFactor = inTopology ? 1.0 : OUT_OF_TOPOLOGY_FACTOR;

            // 2) 变更加权
            LocalDateTime earliestSignal = mine.stream()
                    .filter(e -> e.getVerdict() == Verdict.SUPPORT)
                    .map(Evidence::getObservedAt)
                    .filter(java.util.Objects::nonNull)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);
            TimelineEvent trigger = timelineService.findTriggerChange(
                    changeEvents, h.getTargetName(), earliestSignal,
                    request.getAlarmTime(), changeWindowMinutes);

            // 3) 反事实验证
            Counterfactual cf = counterfactual(h, mine);

            double adjusted = h.getConfidence() * topologyFactor
                    + (trigger != null ? CHANGE_BOOST : 0)
                    - (cf.penalized() ? COUNTERFACTUAL_PENALTY : 0);
            h.setConfidence(Math.max(0.01, Math.min(0.99, adjusted)));

            String cfText = buildCounterfactualText(h, trigger, inTopology, cf);
            h.setCounterfactual(cfText);

            log.info("GlobalReact[{}] conf={} topology={} change={} counterfactualCoverage={}",
                    h.getId(), String.format("%.2f", h.getConfidence()),
                    inTopology, trigger != null, String.format("%.2f", cf.coverage()));
        }

        // 4) 排序分级
        hypotheses.sort(Comparator.comparingDouble(Hypothesis::getConfidence).reversed());
        List<Conclusion> conclusions = new ArrayList<>();
        for (int i = 0; i < hypotheses.size(); i++) {
            Hypothesis h = hypotheses.get(i);
            List<Evidence> mine = byHypothesis.getOrDefault(h.getId(), List.of());
            h.setRank(i + 1);
            boolean confirmed = h.getConfidence() >= CONFIRMED_THRESHOLD;
            conclusions.add(new Conclusion(
                    h.getId(), i + 1, h.getDesc(), h.getTargetType(), h.getTargetName(),
                    h.getSymptom(), round(h.getConfidence()), confirmed,
                    h.getRationale(), h.getCounterfactual(),
                    buildCausalChain(h, mine, changeEvents, request),
                    List.copyOf(h.getSupportEvidenceIds()),
                    List.copyOf(h.getRefuteEvidenceIds())));
        }

        String resultLevel;
        if (conclusions.get(0).confidence() >= CONFIRMED_THRESHOLD) {
            resultLevel = "CONFIRMED";
        } else if (conclusions.get(0).confidence() >= SUSPECTED_THRESHOLD) {
            resultLevel = "SUSPECTED";
        } else {
            resultLevel = "INCONCLUSIVE";
        }

        Hypothesis topHypothesis = "CONFIRMED".equals(resultLevel) ? hypotheses.get(0)
                : "SUSPECTED".equals(resultLevel) ? hypotheses.get(0) : null;
        Suggestions suggestions = topHypothesis != null
                ? suggestionService.forHypothesis(topHypothesis)
                : suggestionService.inconclusive();

        StageTrace trace = StageTrace.of("GlobalReact", "SUCCESS", false,
                "拓扑裁剪+变更感知+反事实验证完成，结果分级=" + resultLevel);
        return new ReactResult(conclusions, suggestions, resultLevel, trace);
    }

    private Counterfactual counterfactual(Hypothesis h, List<Evidence> mine) {
        Set<String> decisive = playbook.decisiveTools(h.getSymptom());
        Set<String> observedTools = mine.stream().map(Evidence::getToolName).collect(Collectors.toSet());
        Set<String> presentDecisive = observedTools.stream()
                .filter(decisive::contains).collect(Collectors.toSet());
        if (presentDecisive.isEmpty()) {
            return new Counterfactual(1.0, false, List.of());
        }
        Set<String> supportedDecisive = mine.stream()
                .filter(e -> e.getVerdict() == Verdict.SUPPORT)
                .map(Evidence::getToolName)
                .filter(decisive::contains)
                .collect(Collectors.toSet());
        double coverage = (double) supportedDecisive.size() / presentDecisive.size();
        List<String> missing = presentDecisive.stream()
                .filter(t -> !supportedDecisive.contains(t))
                .sorted()
                .toList();
        return new Counterfactual(coverage, coverage < 0.5, missing);
    }

    private String buildCounterfactualText(Hypothesis h, TimelineEvent trigger,
                                           boolean inTopology, Counterfactual cf) {
        List<String> parts = new ArrayList<>();
        if (!inTopology) {
            parts.add("目标不在告警应用 " + topologyHops + " 跳拓扑范围内，置信度打折");
        }
        if (trigger != null) {
            parts.add(String.format("时间窗内存在同目标变更（%s），变更与故障时序吻合", trigger.description()));
        }
        if (cf.penalized()) {
            parts.add(String.format("反事实验证未通过：若根因成立，%s 等决定性信号应异常但实际正常（覆盖率 %.0f%%），已下调置信度",
                    cf.missingSignals(), cf.coverage() * 100));
        } else {
            parts.add(String.format("反事实验证通过：决定性信号覆盖率 %.0f%%", cf.coverage() * 100));
        }
        return String.join("；", parts);
    }

    private List<String> buildCausalChain(Hypothesis h, List<Evidence> mine,
                                          List<TimelineEvent> changeEvents, RcaRequest request) {
        List<String> chain = new ArrayList<>();
        changeEvents.stream()
                .filter(e -> h.getTargetName().equals(e.relatedTarget()))
                .sorted(Comparator.comparing(TimelineEvent::time))
                .forEach(e -> chain.add("[变更] " + e.time() + " " + e.description()));

        mine.stream()
                .filter(e -> e.getVerdict() == Verdict.SUPPORT)
                .sorted(Comparator.comparing(Evidence::getObservedAt,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .forEach(e -> chain.add(String.format("[信号] %s %s 在 %s 出现异常（偏离基线 %.1fx）",
                        e.getObservedAt(), e.getToolName(), e.getTargetName(), e.getDeviationRatio())));

        chain.add(String.format("[传导] %s（%s）异常沿依赖传导，最终表现为：%s",
                h.getTargetName(), h.getTargetType(), request.getAlarmDescription()));
        chain.add(String.format("[告警] %s 告警触发", request.getAlarmTime()));
        return chain;
    }

    private static double round(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }

    private record Counterfactual(double coverage, boolean penalized, List<String> missingSignals) {
    }

    public record ReactResult(
            List<Conclusion> conclusions,
            Suggestions suggestions,
            String resultLevel,
            StageTrace trace
    ) {
    }
}
