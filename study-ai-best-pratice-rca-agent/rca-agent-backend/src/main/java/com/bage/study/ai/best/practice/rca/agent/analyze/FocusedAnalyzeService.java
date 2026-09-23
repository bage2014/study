package com.bage.study.ai.best.practice.rca.agent.analyze;

import com.bage.study.ai.best.practice.rca.agent.model.Evidence;
import com.bage.study.ai.best.practice.rca.agent.model.Hypothesis;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * FocusedAnalyze：对下钻假设用 BROAD+FOCUSED 全量证据重新裁决并刷新置信度。
 */
@Service
public class FocusedAnalyzeService {

    private final EvidenceEvaluator evaluator;
    private final HypothesisScorer scorer;

    public FocusedAnalyzeService(EvidenceEvaluator evaluator, HypothesisScorer scorer) {
        this.evaluator = evaluator;
        this.scorer = scorer;
    }

    public void analyze(List<Hypothesis> hypotheses, List<Evidence> allEvidences) {
        Map<String, List<Evidence>> byHypothesis = allEvidences.stream()
                .collect(Collectors.groupingBy(Evidence::getHypothesisId));
        for (Hypothesis h : hypotheses) {
            if (!h.isFocused()) {
                continue;
            }
            List<Evidence> full = byHypothesis.getOrDefault(h.getId(), List.of());
            evaluator.evaluate(h, full);
            h.setConfidence(scorer.score(h, full));
            h.setRationale(BroadAnalyzeService.templateRationale(h, full) + "；已完成 FocusedScan 下钻复核");
        }
    }
}
