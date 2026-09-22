package com.bage.study.ai.best.practice.rca.agent.dto;

import com.bage.study.ai.best.practice.rca.agent.model.Conclusion;
import com.bage.study.ai.best.practice.rca.agent.model.Evidence;
import com.bage.study.ai.best.practice.rca.agent.model.Hypothesis;
import com.bage.study.ai.best.practice.rca.agent.model.StageTrace;
import com.bage.study.ai.best.practice.rca.agent.model.Suggestions;
import com.bage.study.ai.best.practice.rca.agent.model.TimelineEvent;

import java.time.LocalDateTime;
import java.util.List;

/**
 * RCA 分析报告：假设全景 + 时间线 + 证据 + 排序结论 + 三层建议。
 */
public class RcaResponse {

    private String analysisId;
    private String appId;
    private String scene;
    private String alarmDescription;
    private LocalDateTime alarmTime;
    private LocalDateTime analysisTime;
    private boolean llmUsed;
    /** CONFIRMED / SUSPECTED / INCONCLUSIVE */
    private String resultLevel;
    private List<StageTrace> stages;
    private List<Hypothesis> hypotheses;
    private List<TimelineEvent> timeline;
    private List<Evidence> evidences;
    private List<Conclusion> conclusions;
    private Suggestions suggestions;

    public String getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getAlarmDescription() {
        return alarmDescription;
    }

    public void setAlarmDescription(String alarmDescription) {
        this.alarmDescription = alarmDescription;
    }

    public LocalDateTime getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(LocalDateTime alarmTime) {
        this.alarmTime = alarmTime;
    }

    public LocalDateTime getAnalysisTime() {
        return analysisTime;
    }

    public void setAnalysisTime(LocalDateTime analysisTime) {
        this.analysisTime = analysisTime;
    }

    public boolean isLlmUsed() {
        return llmUsed;
    }

    public void setLlmUsed(boolean llmUsed) {
        this.llmUsed = llmUsed;
    }

    public String getResultLevel() {
        return resultLevel;
    }

    public void setResultLevel(String resultLevel) {
        this.resultLevel = resultLevel;
    }

    public List<StageTrace> getStages() {
        return stages;
    }

    public void setStages(List<StageTrace> stages) {
        this.stages = stages;
    }

    public List<Hypothesis> getHypotheses() {
        return hypotheses;
    }

    public void setHypotheses(List<Hypothesis> hypotheses) {
        this.hypotheses = hypotheses;
    }

    public List<TimelineEvent> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<TimelineEvent> timeline) {
        this.timeline = timeline;
    }

    public List<Evidence> getEvidences() {
        return evidences;
    }

    public void setEvidences(List<Evidence> evidences) {
        this.evidences = evidences;
    }

    public List<Conclusion> getConclusions() {
        return conclusions;
    }

    public void setConclusions(List<Conclusion> conclusions) {
        this.conclusions = conclusions;
    }

    public Suggestions getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(Suggestions suggestions) {
        this.suggestions = suggestions;
    }
}
