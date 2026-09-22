package com.bage.study.ai.best.practice.rca.agent.planner;

import com.bage.study.ai.best.practice.rca.agent.model.Hypothesis;

import java.util.List;

public record PlanResult(
        List<Hypothesis> hypotheses,
        boolean llmUsed,
        String note
) {
}
