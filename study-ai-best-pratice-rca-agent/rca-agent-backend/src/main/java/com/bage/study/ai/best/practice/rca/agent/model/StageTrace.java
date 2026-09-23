package com.bage.study.ai.best.practice.rca.agent.model;

/**
 * 流水线各阶段执行轨迹，便于审计"哪一步用了什么、产出什么"。
 */
public record StageTrace(
        String stage,
        String status,
        boolean llmUsed,
        String summary
) {
    public static StageTrace of(String stage, String status, boolean llmUsed, String summary) {
        return new StageTrace(stage, status, llmUsed, summary);
    }
}
