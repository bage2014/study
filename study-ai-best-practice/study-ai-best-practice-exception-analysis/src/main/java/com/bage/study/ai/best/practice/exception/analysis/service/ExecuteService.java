package com.bage.study.ai.best.practice.exception.analysis.service;

import com.bage.study.ai.best.practice.exception.analysis.model.AnalysisStep;
import com.bage.study.ai.best.practice.exception.analysis.mcp.McpToolManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExecuteService {

    private final McpToolManager mcpToolManager;

    public ExecuteService(McpToolManager mcpToolManager) {
        this.mcpToolManager = mcpToolManager;
    }

    public List<AnalysisStep> executePlan(List<AnalysisStep> planSteps) {
        List<AnalysisStep> executedSteps = new ArrayList<>();

        for (AnalysisStep step : planSteps) {
            AnalysisStep executedStep = executeStep(step);
            executedSteps.add(executedStep);
        }

        return executedSteps;
    }

    private AnalysisStep executeStep(AnalysisStep step) {
        switch (step.type()) {
            case "LOG_ANALYSIS":
                return executeLogAnalysis(step);
            case "CODE_ANALYSIS":
                return executeCodeAnalysis(step);
            case "TRACE_ANALYSIS":
                return executeTraceAnalysis(step);
            case "BUSINESS_KNOWLEDGE":
                return executeBusinessKnowledge(step);
            case "INITIAL_ANALYSIS":
            case "ROOT_CAUSE_ANALYSIS":
            case "RECOMMENDATION":
                return executeDefaultStep(step);
            default:
                return executeDefaultStep(step);
        }
    }

    private AnalysisStep executeLogAnalysis(AnalysisStep step) {
        try {
            var result = mcpToolManager.executeTool("query_logs", Map.of(
                "service", "user-service",
                "startTime", "2026-04-27T00:00:00",
                "endTime", "2026-04-27T23:59:59",
                "keywords", "NullPointerException"
            ));
            return AnalysisStep.success(step.id(), step.type(), step.description(), 
                result.content() != null ? result.content() : "No log data found");
        } catch (Exception e) {
            return AnalysisStep.failure(step.id(), step.type(), step.description(), e.getMessage());
        }
    }

    private AnalysisStep executeCodeAnalysis(AnalysisStep step) {
        try {
            var result = mcpToolManager.executeTool("query_gitlab", Map.of(
                "repository", "user-service",
                "branch", "main",
                "since", "2026-04-26",
                "until", "2026-04-27",
                "filePattern", "**/*.java"
            ));
            return AnalysisStep.success(step.id(), step.type(), step.description(), 
                result.content() != null ? result.content() : "No code changes found");
        } catch (Exception e) {
            return AnalysisStep.failure(step.id(), step.type(), step.description(), e.getMessage());
        }
    }

    private AnalysisStep executeTraceAnalysis(AnalysisStep step) {
        try {
            var result = mcpToolManager.executeTool("query_trace", Map.of(
                "traceId", "1234567890",
                "service", "user-service",
                "operation", "getUser"
            ));
            return AnalysisStep.success(step.id(), step.type(), step.description(), 
                result.content() != null ? result.content() : "No trace data found");
        } catch (Exception e) {
            return AnalysisStep.failure(step.id(), step.type(), step.description(), e.getMessage());
        }
    }

    private AnalysisStep executeBusinessKnowledge(AnalysisStep step) {
        try {
            var result = mcpToolManager.executeTool("query_business", Map.of(
                "domain", "user-management",
                "topic", "authentication"
            ));
            return AnalysisStep.success(step.id(), step.type(), step.description(), 
                result.content() != null ? result.content() : "No business knowledge found");
        } catch (Exception e) {
            return AnalysisStep.failure(step.id(), step.type(), step.description(), e.getMessage());
        }
    }

    private AnalysisStep executeDefaultStep(AnalysisStep step) {
        return AnalysisStep.success(step.id(), step.type(), step.description(), "Analysis completed");
    }
}
