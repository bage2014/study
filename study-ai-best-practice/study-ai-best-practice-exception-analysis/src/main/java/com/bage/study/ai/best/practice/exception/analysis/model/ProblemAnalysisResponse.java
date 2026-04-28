package com.bage.study.ai.best.practice.exception.analysis.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ProblemAnalysisResponse(
    String analysisId,
    String status,
    String conclusion,
    List<String> possibleRootCauses,
    List<String> recommendedActions,
    Map<String, Object> evidence,
    List<AnalysisStep> steps,
    LocalDateTime startTime,
    LocalDateTime endTime,
    String error
) {
    public static ProblemAnalysisResponse success(String analysisId, String conclusion, 
                                               List<String> possibleRootCauses, 
                                               List<String> recommendedActions, 
                                               Map<String, Object> evidence, 
                                               List<AnalysisStep> steps) {
        return new ProblemAnalysisResponse(
            analysisId, "COMPLETED", conclusion, possibleRootCauses, 
            recommendedActions, evidence, steps, 
            LocalDateTime.now(), LocalDateTime.now(), null
        );
    }

    public static ProblemAnalysisResponse inProgress(String analysisId, List<AnalysisStep> steps) {
        return new ProblemAnalysisResponse(
            analysisId, "IN_PROGRESS", null, null, null, 
            null, steps, LocalDateTime.now(), null, null
        );
    }

    public static ProblemAnalysisResponse error(String analysisId, String error) {
        return new ProblemAnalysisResponse(
            analysisId, "FAILED", null, null, null, 
            null, null, LocalDateTime.now(), LocalDateTime.now(), error
        );
    }

    public boolean isSuccess() {
        return "COMPLETED".equals(status);
    }

    public boolean isInProgress() {
        return "IN_PROGRESS".equals(status);
    }

    public boolean isFailed() {
        return "FAILED".equals(status);
    }
}
