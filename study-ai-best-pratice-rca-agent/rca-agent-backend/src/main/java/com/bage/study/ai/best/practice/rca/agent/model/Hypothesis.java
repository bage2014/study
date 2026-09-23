package com.bage.study.ai.best.practice.rca.agent.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 根因假设：Plan 阶段生成，后续各阶段持续更新置信度与证据。
 * 字段结构与 rca.md 中 Hypothesis record 对齐。
 */
public class Hypothesis {

    private String id;
    private String desc;
    private TargetType targetType;
    private String targetName;
    private SymptomPattern symptom;
    private double priorConfidence;
    private double confidence;
    private boolean focused;
    private String rationale;
    private String counterfactual;
    private int rank;
    private final List<String> supportEvidenceIds = new ArrayList<>();
    private final List<String> refuteEvidenceIds = new ArrayList<>();

    public Hypothesis() {
    }

    public Hypothesis(String id, String desc, TargetType targetType, String targetName,
                      SymptomPattern symptom, double priorConfidence) {
        this.id = id;
        this.desc = desc;
        this.targetType = targetType;
        this.targetName = targetName;
        this.symptom = symptom;
        this.priorConfidence = priorConfidence;
        this.confidence = priorConfidence;
    }

    public String playbookKey() {
        return targetType + ":" + symptom;
    }

    public String getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public String getTargetName() {
        return targetName;
    }

    public SymptomPattern getSymptom() {
        return symptom;
    }

    public double getPriorConfidence() {
        return priorConfidence;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public String getRationale() {
        return rationale;
    }

    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    public String getCounterfactual() {
        return counterfactual;
    }

    public void setCounterfactual(String counterfactual) {
        this.counterfactual = counterfactual;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public List<String> getSupportEvidenceIds() {
        return supportEvidenceIds;
    }

    public List<String> getRefuteEvidenceIds() {
        return refuteEvidenceIds;
    }
}
