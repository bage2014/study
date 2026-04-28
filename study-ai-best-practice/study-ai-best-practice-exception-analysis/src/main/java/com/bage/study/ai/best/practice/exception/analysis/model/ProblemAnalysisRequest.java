package com.bage.study.ai.best.practice.exception.analysis.model;

import java.time.LocalDateTime;
import java.util.Map;

public record ProblemAnalysisRequest(
    String problemDescription,
    String errorMessage,
    String serviceName,
    String environment,
    LocalDateTime startTime,
    LocalDateTime endTime,
    Map<String, String> context
) {
    public ProblemAnalysisRequest {
        if (problemDescription == null || problemDescription.isBlank()) {
            throw new IllegalArgumentException("Problem description is required");
        }
        if (startTime == null) {
            startTime = LocalDateTime.now().minusHours(24);
        }
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }
        if (context == null) {
            context = Map.of();
        }
    }

    public static ProblemAnalysisRequest of(String problemDescription, String errorMessage) {
        return new ProblemAnalysisRequest(problemDescription, errorMessage, null, null, null, null, null);
    }

    public static ProblemAnalysisRequest of(String problemDescription, String errorMessage, String serviceName) {
        return new ProblemAnalysisRequest(problemDescription, errorMessage, serviceName, null, null, null, null);
    }
}
