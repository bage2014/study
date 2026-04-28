package com.bage.study.ai.best.practice.exception.analysis.service;

import com.bage.study.ai.best.practice.exception.analysis.model.ProblemAnalysisRequest;
import com.bage.study.ai.best.practice.exception.analysis.model.ProblemAnalysisResponse;
import com.bage.study.ai.best.practice.exception.analysis.model.AnalysisStep;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReactService {

    private final Optional<ChatModel> chatModel;

    public ReactService(@Autowired(required = false) ChatModel chatModel) {
        this.chatModel = Optional.ofNullable(chatModel);
    }

    private ChatClient getChatClient() {
        return chatModel.map(model -> ChatClient.builder(model)
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .build()).orElse(null);
    }

    public ProblemAnalysisResponse analyzeResults(ProblemAnalysisRequest request, List<AnalysisStep> executedSteps) {
        String analysisId = UUID.randomUUID().toString();

        Map<String, String> stepResults = executedSteps.stream()
            .collect(Collectors.toMap(AnalysisStep::type, AnalysisStep::result));

        String aiResponse;
        ChatClient client = getChatClient();

        if (client != null) {
            String prompt = buildAnalysisPrompt(request, stepResults);
            aiResponse = client.prompt()
                .user(prompt)
                .call()
                .content();
        } else {
            aiResponse = generateMockAnalysis(request, stepResults);
        }

        return generateAnalysisResponse(analysisId, executedSteps, aiResponse);
    }

    private String generateMockAnalysis(ProblemAnalysisRequest request, Map<String, String> stepResults) {
        StringBuilder sb = new StringBuilder();
        sb.append("结论：基于收集的信息，初步判断问题与以下因素相关\n\n");
        sb.append("可能的根本原因：\n");
        sb.append("1. 日志分析发现 NullPointerException，可能存在空指针检查缺失\n");
        sb.append("2. 代码变更检查显示近期有相关代码修改\n");
        sb.append("3. 业务知识表明该场景需要更完善的异常处理\n\n");
        sb.append("建议的修复方案：\n");
        sb.append("1. 在相关方法中添加空指针检查\n");
        sb.append("2. 增加日志输出以便追踪问题\n");
        sb.append("3. 添加防御性编程，提高代码健壮性\n");
        return sb.toString();
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
            "successfulSteps", executedSteps.stream().filter(step -> "SUCCESS".equals(step.status())).count(),
            "aiEnabled", chatModel.isPresent()
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

        String aiResponse;
        ChatClient client = getChatClient();

        if (client != null) {
            String prompt = buildReanalysisPrompt(request, previousResponse, feedback);
            aiResponse = client.prompt()
                .user(prompt)
                .call()
                .content();
        } else {
            aiResponse = generateMockReanalysis(previousResponse, feedback);
        }

        return generateAnalysisResponse(analysisId, previousResponse.steps(), aiResponse);
    }

    private String generateMockReanalysis(ProblemAnalysisResponse previousResponse, String feedback) {
        StringBuilder sb = new StringBuilder();
        sb.append("结论：结合反馈信息，重新分析得出以下结论\n\n");
        sb.append("可能的根本原因：\n");
        for (int i = 0; i < previousResponse.possibleRootCauses().size(); i++) {
            sb.append((i + 1) + ". " + previousResponse.possibleRootCauses().get(i) + "\n");
        }
        sb.append("\n建议的修复方案：\n");
        for (int i = 0; i < previousResponse.recommendedActions().size(); i++) {
            sb.append((i + 1) + ". " + previousResponse.recommendedActions().get(i) + "\n");
        }
        sb.append("\n用户反馈已纳入分析。\n");
        return sb.toString();
    }

    private String buildReanalysisPrompt(ProblemAnalysisRequest request, 
                                         ProblemAnalysisResponse previousResponse, 
                                         String feedback) {
        return "# 重新分析任务\n" +
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
    }

    public boolean isAiEnabled() {
        return chatModel.isPresent();
    }
}
