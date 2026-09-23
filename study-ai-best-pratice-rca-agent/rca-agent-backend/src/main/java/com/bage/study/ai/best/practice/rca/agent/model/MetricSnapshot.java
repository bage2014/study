package com.bage.study.ai.best.practice.rca.agent.model;

import java.time.LocalDateTime;

/**
 * 指标快照：喂给分析层的是"偏差量"（deviationRatio，相对基线偏离倍数），
 * 而不是绝对值（rca.md 基线对比编码）。
 */
public record MetricSnapshot(
        String toolName,
        String layer,
        String targetName,
        double value,
        double baseline,
        double threshold,
        String unit,
        boolean higherIsBad,
        boolean anomaly,
        double deviationRatio,
        LocalDateTime observedAt,
        String detail
) {
}
