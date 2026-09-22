package com.bage.study.ai.best.practice.rca.agent.analyze;

import com.bage.study.ai.best.practice.rca.agent.model.Evidence;
import com.bage.study.ai.best.practice.rca.agent.model.Hypothesis;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 置信度更新（确定性，LLM 不参与打分）：
 *
 * belief = support / (support + 1.2 * refute + 0.5)
 * confidence = 0.35 * prior + 0.65 * belief
 *
 * 先验占小头、证据占大头；无证据时 belief→0，假设自然下沉。
 */
@Component
public class HypothesisScorer {

    public double score(Hypothesis hypothesis, List<Evidence> evidences) {
        double support = evidences.stream()
                .filter(e -> e.getVerdict() == com.bage.study.ai.best.practice.rca.agent.model.Verdict.SUPPORT)
                .mapToDouble(Evidence::getWeight)
                .sum();
        double refute = evidences.stream()
                .filter(e -> e.getVerdict() == com.bage.study.ai.best.practice.rca.agent.model.Verdict.REFUTE)
                .mapToDouble(Evidence::getWeight)
                .sum();

        double belief = support / (support + 1.2 * refute + 0.5);
        double confidence = 0.35 * hypothesis.getPriorConfidence() + 0.65 * belief;
        return Math.max(0.01, Math.min(0.99, confidence));
    }
}
