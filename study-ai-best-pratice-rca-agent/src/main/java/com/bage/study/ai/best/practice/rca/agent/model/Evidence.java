package com.bage.study.ai.best.practice.rca.agent.model;

import java.time.LocalDateTime;

/**
 * 证据：一次工具采集对某个假设的裁决材料。结论必须锚定 evidenceId，禁止裸结论。
 */
public class Evidence {

    private String id;
    private String hypothesisId;
    private Phase phase;
    private String toolName;
    private String layer;
    private String targetName;
    private double value;
    private double baseline;
    private double deviationRatio;
    private boolean anomaly;
    private LocalDateTime observedAt;
    private String summary;
    private Verdict verdict = Verdict.NEUTRAL;
    private double weight;

    public enum Phase {
        BROAD,
        FOCUSED
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHypothesisId() {
        return hypothesisId;
    }

    public void setHypothesisId(String hypothesisId) {
        this.hypothesisId = hypothesisId;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getBaseline() {
        return baseline;
    }

    public void setBaseline(double baseline) {
        this.baseline = baseline;
    }

    public double getDeviationRatio() {
        return deviationRatio;
    }

    public void setDeviationRatio(double deviationRatio) {
        this.deviationRatio = deviationRatio;
    }

    public boolean isAnomaly() {
        return anomaly;
    }

    public void setAnomaly(boolean anomaly) {
        this.anomaly = anomaly;
    }

    public LocalDateTime getObservedAt() {
        return observedAt;
    }

    public void setObservedAt(LocalDateTime observedAt) {
        this.observedAt = observedAt;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Verdict getVerdict() {
        return verdict;
    }

    public void setVerdict(Verdict verdict) {
        this.verdict = verdict;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
