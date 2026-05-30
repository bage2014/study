package com.bage.study.ai.best.practice.exception.analysis.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class AnalysisResponse {

    private String analysisId;
    private String appId;
    private String alarmDescription;
    private LocalDateTime analysisTime;
    private RootCause rootCause;
    private List<Evidence> evidences;
    private List<String> suggestions;

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

    public String getAlarmDescription() {
        return alarmDescription;
    }

    public void setAlarmDescription(String alarmDescription) {
        this.alarmDescription = alarmDescription;
    }

    public LocalDateTime getAnalysisTime() {
        return analysisTime;
    }

    public void setAnalysisTime(LocalDateTime analysisTime) {
        this.analysisTime = analysisTime;
    }

    public RootCause getRootCause() {
        return rootCause;
    }

    public void setRootCause(RootCause rootCause) {
        this.rootCause = rootCause;
    }

    public List<Evidence> getEvidences() {
        return evidences;
    }

    public void setEvidences(List<Evidence> evidences) {
        this.evidences = evidences;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public static class RootCause {
        private String type;
        private String description;
        private String confidence;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getConfidence() {
            return confidence;
        }

        public void setConfidence(String confidence) {
            this.confidence = confidence;
        }
    }

    public static class Evidence {
        private String source;
        private String content;
        private String relevance;

        public Evidence() {
        }

        public Evidence(String source, String content, String relevance) {
            this.source = source;
            this.content = content;
            this.relevance = relevance;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getRelevance() {
            return relevance;
        }

        public void setRelevance(String relevance) {
            this.relevance = relevance;
        }
    }
}
