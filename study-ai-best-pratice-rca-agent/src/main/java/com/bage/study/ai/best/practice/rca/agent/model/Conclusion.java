package com.bage.study.ai.best.practice.rca.agent.model;

import java.util.List;

/**
 * 最终结论：排序后的假设裁决结果，携带因果链、正反证据与反事实验证结论。
 */
public record Conclusion(
        String hypothesisId,
        int rank,
        String title,
        TargetType targetType,
        String targetName,
        SymptomPattern symptom,
        double confidence,
        boolean confirmed,
        String rationale,
        String counterfactual,
        List<String> causalChain,
        List<String> supportEvidenceIds,
        List<String> refuteEvidenceIds
) {
}
