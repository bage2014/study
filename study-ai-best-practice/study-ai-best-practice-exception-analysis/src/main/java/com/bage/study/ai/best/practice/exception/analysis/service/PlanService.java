package com.bage.study.ai.best.practice.exception.analysis.service;

import com.bage.study.ai.best.practice.exception.analysis.model.ProblemAnalysisRequest;
import com.bage.study.ai.best.practice.exception.analysis.model.AnalysisStep;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PlanService {

    public List<AnalysisStep> generatePlan(ProblemAnalysisRequest request) {
        List<AnalysisStep> steps = new ArrayList<>();

        steps.add(AnalysisStep.start(
            UUID.randomUUID().toString(),
            "INITIAL_ANALYSIS",
            "分析问题描述和错误信息"
        ));

        steps.add(AnalysisStep.start(
            UUID.randomUUID().toString(),
            "LOG_ANALYSIS",
            "查询相关日志信息"
        ));

        steps.add(AnalysisStep.start(
            UUID.randomUUID().toString(),
            "CODE_ANALYSIS",
            "检查相关代码变更"
        ));

        steps.add(AnalysisStep.start(
            UUID.randomUUID().toString(),
            "TRACE_ANALYSIS",
            "分析错误调用链"
        ));

        steps.add(AnalysisStep.start(
            UUID.randomUUID().toString(),
            "BUSINESS_KNOWLEDGE",
            "应用业务领域知识"
        ));

        steps.add(AnalysisStep.start(
            UUID.randomUUID().toString(),
            "ROOT_CAUSE_ANALYSIS",
            "识别根本原因"
        ));

        steps.add(AnalysisStep.start(
            UUID.randomUUID().toString(),
            "RECOMMENDATION",
            "生成修复建议"
        ));

        return steps;
    }

    public List<AnalysisStep> adjustPlan(List<AnalysisStep> currentSteps, String feedback) {
        List<AnalysisStep> adjustedSteps = new ArrayList<>(currentSteps);

        if (feedback.contains("需要更多日志")) {
            adjustedSteps.add(AnalysisStep.start(
                UUID.randomUUID().toString(),
                "LOG_ANALYSIS",
                "深入分析日志信息"
            ));
        }

        if (feedback.contains("需要代码分析")) {
            adjustedSteps.add(AnalysisStep.start(
                UUID.randomUUID().toString(),
                "CODE_ANALYSIS",
                "深入分析相关代码"
            ));
        }

        return adjustedSteps;
    }
}
