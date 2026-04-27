package com.bage.study.ai.best.practice.exceptionanalysis.service;

import com.bage.study.ai.best.practice.exceptionanalysis.model.ProblemAnalysisRequest;
import com.bage.study.ai.best.practice.exceptionanalysis.model.ProblemAnalysisResponse;
import com.bage.study.ai.best.practice.exceptionanalysis.model.AnalysisStep;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReactService {

    private final ChatClient chatClient;

    public ReactService(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel)
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .build();
    }

    public ProblemAnalysisResponse analyzeResults(ProblemAnalysisRequest request, List<AnalysisStep> executedSteps) {
        String analysisId = UUID.randomUUID().toString();

        Map<String, String> stepResults = executedSteps.stream()
            .collect(Collectors.toMap(AnalysisStep::type, AnalysisStep::result));

        String prompt = buildAnalysisPrompt(request, stepResults);

        String aiResponse = chatClient.prompt()
            .user(prompt)
            .call()
            .content();

        return generateAnalysisResponse(analysisId, executedSteps, aiResponse);
    }

    private String buildAnalysisPrompt(ProblemAnalysisRequest request, Map<String, String> stepResults) {
        return "# 问题分析任务\n" +
            "\n## 问题描述\n" +
            request.problemDescription() + "\n" +
            (request.errorMessage() != null ? "\n## 错误信息\n" + request.errorMessage() : "") + "\n" +
            "\n## 分析结果\n" +
            stepResults.entrySet().stream()
                .map(entry -> "### " + entry.getKey() + "\n" + entry.getValue())
                .collect(Collectors.joining("\n")) + "\n" +
            "\n## 任务\n" +
            "基于以上信息，完成以下分析：\n" +
            "1. 识别可能的根本原因（最多3个）\n" +
            "2. 生成具体的修复建议\n" +
            "3. 给出最终结论\n" +
            "\n请以结构化格式输出，使用中文。\n" +
            "格式示例：\n" +
            "结论：...\n" +
            "可能的根本原因：\n" +
            "1. ...\n" +
            "2. ...\n" +
            "3. ...\n" +
            "建议的修复方案：\n" +
            "1. ...\n" +
            "2. ...\n" +
            "3. ...\n";
    }

    private ProblemAnalysisResponse generateAnalysisResponse(String analysisId, 
                                                          List<AnalysisStep> executedSteps, 
                                                          String aiResponse) {
        List<String> possibleRootCauses = new ArrayList<>();
        List<String> recommendedActions = new ArrayList<>();
        String conclusion = "";

        String[] lines = aiResponse.split("\\n");
        boolean inRootCauses = false;
        boolean inActions = false;

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("结论：")) {
                conclusion = line.substring(3).trim();
            } else if (line.startsWith("可能的根本原因：")) {
                inRootCauses = true;
                inActions = false;
            } else if (line.startsWith("建议的修复方案：")) {
                inRootCauses = false;
                inActions = true;
            } else if (inRootCauses && (line.startsWith("1.") || line.startsWith("2.") || line.startsWith("3."))) {
                possibleRootCauses.add(line.substring(2).trim());
            } else if (inActions && (line.startsWith("1.") || line.startsWith("2.") || line.startsWith("3."))) {
                recommendedActions.add(line.substring(2).trim());
            }
        }

        Map<String, Object> evidence = Map.of(
            "aiAnalysis", aiResponse,
            "executedSteps", executedSteps.size(),
            "successfulSteps", executedSteps.stream().filter(step -> "SUCCESS".equals(step.status())).count()
        );

        return ProblemAnalysisResponse.success(
            analysisId,
            conclusion.isEmpty() ? "分析完成" : conclusion,
            possibleRootCauses.isEmpty() ? List.of("未识别到具体原因") : possibleRootCauses,
            recommendedActions.isEmpty() ? List.of("需要进一步分析") : recommendedActions,
            evidence,
            executedSteps
        );
    }

    public ProblemAnalysisResponse reanalyzeWithFeedback(ProblemAnalysisRequest request, 
                                                       ProblemAnalysisResponse previousResponse, 
                                                       String feedback) {
        String analysisId = UUID.randomUUID().toString();

        String prompt = "# 重新分析任务\n" +
            "\n## 原始问题\n" +
            request.problemDescription() + "\n" +
            (request.errorMessage() != null ? "\n## 错误信息\n" + request.errorMessage() : "") + "\n" +
            "\n## 之前的分析结果\n" +
            "结论：" + previousResponse.conclusion() + "\n" +
            "可能的根本原因：\n" +
            java.util.stream.IntStream.range(0, previousResponse.possibleRootCauses().size())
                .mapToObj(i -> (i + 1) + ". " + previousResponse.possibleRootCauses().get(i))
                .collect(Collectors.joining("\n")) + "\n" +
            "建议的修复方案：\n" +
            java.util.stream.IntStream.range(0, previousResponse.recommendedActions().size())
                .mapToObj(i -> (i + 1) + ". " + previousResponse.recommendedActions().get(i))
                .collect(Collectors.joining("\n")) + "\n" +
            "\n## 用户反馈\n" +
            feedback + "\n" +
            "\n## 任务\n" +
            "基于用户反馈，重新分析并更新结论、根因和修复建议。\n" +
            "请以与之前相同的结构化格式输出。\n";

        String aiResponse = chatClient.prompt()
            .user(prompt)
            .call()
            .content();

        return generateAnalysisResponse(analysisId, previousResponse.steps(), aiResponse);
    }
}
