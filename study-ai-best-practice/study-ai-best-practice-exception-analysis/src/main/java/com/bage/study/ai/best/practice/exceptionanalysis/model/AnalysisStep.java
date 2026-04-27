package com.bage.study.ai.best.practice.exceptionanalysis.model;

import java.time.LocalDateTime;

public record AnalysisStep(
    String id,
    String type,
    String description,
    String status,
    String result,
    LocalDateTime startTime,
    LocalDateTime endTime
) {
    public static AnalysisStep start(String id, String type, String description) {
        return new AnalysisStep(id, type, description, "STARTED", null, 
            LocalDateTime.now(), null);
    }

    public static AnalysisStep success(String id, String type, String description, String result) {
        return new AnalysisStep(id, type, description, "SUCCESS", result, 
            LocalDateTime.now(), LocalDateTime.now());
    }

    public static AnalysisStep failure(String id, String type, String description, String result) {
        return new AnalysisStep(id, type, description, "FAILURE", result, 
            LocalDateTime.now(), LocalDateTime.now());
    }
}
