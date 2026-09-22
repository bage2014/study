package com.bage.study.ai.best.practice.rca.agent.analyze;

import com.bage.study.ai.best.practice.rca.agent.model.Evidence;
import com.bage.study.ai.best.practice.rca.agent.model.Hypothesis;
import com.bage.study.ai.best.practice.rca.agent.model.SymptomPattern;
import com.bage.study.ai.best.practice.rca.agent.model.Verdict;
import com.bage.study.ai.best.practice.rca.agent.playbook.ScanPlaybook;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 证据裁决器：把工具采集结果统一裁决为对假设的 SUPPORT / REFUTE / NEUTRAL。
 *
 * 规则：
 * - 决定性信号（Playbook 按症状声明）异常 → SUPPORT，权重随偏离倍数增强
 * - 非决定性信号异常 → SUPPORT（弱权重，旁证）
 * - 决定性信号正常 → REFUTE（弱权重，反事实含义：本应看到却没看到）
 * - 其余正常信号 → NEUTRAL（噪声/干扰项）
 * FOCUSED 阶段证据权重 ×1.5。
 */
@Component
public class EvidenceEvaluator {

    private final ScanPlaybook playbook;

    public EvidenceEvaluator(ScanPlaybook playbook) {
        this.playbook = playbook;
    }

    public void evaluate(Hypothesis hypothesis, List<Evidence> hypothesisEvidences) {
        Set<String> decisive = playbook.decisiveTools(hypothesis.getSymptom());
        hypothesis.getSupportEvidenceIds().clear();
        hypothesis.getRefuteEvidenceIds().clear();

        for (Evidence e : hypothesisEvidences) {
            double phaseFactor = e.getPhase() == Evidence.Phase.FOCUSED ? 1.5 : 1.0;
            if (e.isAnomaly()) {
                if (decisive.contains(e.getToolName())) {
                    e.setVerdict(Verdict.SUPPORT);
                    e.setWeight(clampWeight(deviationWeight(e.getDeviationRatio()) * phaseFactor));
                } else {
                    e.setVerdict(Verdict.SUPPORT);
                    e.setWeight(clampWeight(0.4 * phaseFactor));
                }
                hypothesis.getSupportEvidenceIds().add(e.getId());
            } else if (decisive.contains(e.getToolName())) {
                e.setVerdict(Verdict.REFUTE);
                e.setWeight(clampWeight(0.3 * phaseFactor));
                hypothesis.getRefuteEvidenceIds().add(e.getId());
            } else {
                e.setVerdict(Verdict.NEUTRAL);
                e.setWeight(0);
            }
        }
    }

    /** 偏离倍数 → 0.5~1 的权重：偏离 5x 即接近满权 */
    public static double deviationWeight(double deviationRatio) {
        return Math.min(1.0, 0.4 + deviationRatio * 0.12);
    }

    private static double clampWeight(double w) {
        return Math.max(0, Math.min(1.5, w));
    }
}
