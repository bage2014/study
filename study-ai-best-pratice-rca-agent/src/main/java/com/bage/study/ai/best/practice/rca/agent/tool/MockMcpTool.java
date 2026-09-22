package com.bage.study.ai.best.practice.rca.agent.tool;

import com.bage.study.ai.best.practice.rca.agent.model.MetricSnapshot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 确定性 Mock 指标工具：按场景表返回基线值或异常值。
 * 异常判定与偏差倍数在输出中显式给出，供下游做基线对比编码。
 */
public class MockMcpTool implements McpTool {

    /**
     * @param abnormalScenes 命中哪些 scene 时返回异常值
     * @param higherIsBad    true=值越高越坏（延迟/错误率）；false=值越低越坏（成功率/命中率）
     */
    public record Spec(
            String name,
            String layer,
            String description,
            String unit,
            double normalValue,
            double threshold,
            boolean higherIsBad,
            double abnormalValue,
            Set<String> abnormalScenes,
            String normalDetail,
            String abnormalDetail
    ) {
    }

    private final Spec spec;

    public MockMcpTool(Spec spec) {
        this.spec = spec;
    }

    @Override
    public String name() {
        return spec.name();
    }

    @Override
    public String description() {
        return spec.description();
    }

    @Override
    public String layer() {
        return spec.layer();
    }

    @Override
    public ToolResult execute(ToolQuery query) {
        String scene = query.scene() == null ? "default" : query.scene();
        boolean abnormal = spec.abnormalScenes().contains(scene);
        double value = abnormal ? spec.abnormalValue() : spec.normalValue();
        boolean anomaly = spec.higherIsBad()
                ? value > spec.threshold()
                : value < spec.threshold();
        double deviationRatio = spec.higherIsBad()
                ? value / Math.max(spec.normalValue(), 0.0001)
                : spec.normalValue() / Math.max(value, 0.0001);
        // 正常取整为 1.0，避免 99.6/99.6 之类的噪声偏差
        if (!abnormal) {
            deviationRatio = 1.0;
        }
        LocalDateTime observedAt = query.alarmTime() != null
                ? query.alarmTime().minusMinutes(anomaly ? 1 : 0).minusSeconds(anomaly ? 0 : 40)
                : LocalDateTime.now();
        String detail = abnormal ? spec.abnormalDetail() : spec.normalDetail();

        MetricSnapshot snapshot = new MetricSnapshot(
                spec.name(),
                spec.layer(),
                query.targetName(),
                round(value),
                round(spec.normalValue()),
                round(spec.threshold()),
                spec.unit(),
                spec.higherIsBad(),
                anomaly,
                round(deviationRatio),
                observedAt,
                detail
        );
        return ToolResult.metrics(spec.name(), List.of(snapshot));
    }

    private static double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
