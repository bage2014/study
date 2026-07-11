package com.bage.ai.pipeline.agent.config;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MockChatLanguageModel implements ChatLanguageModel {

    @Override
    public Response<AiMessage> generate(List<ChatMessage> messages) {
        String userContent = messages.stream()
                .filter(m -> m instanceof UserMessage)
                .map(m -> ((UserMessage) m).text())
                .findFirst()
                .orElse("");

        String systemContent = messages.stream()
                .filter(m -> m instanceof SystemMessage)
                .map(m -> ((SystemMessage) m).text())
                .findFirst()
                .orElse("");

        String response = generateMockResponse(userContent, systemContent);
        log.info("Mock AI response generated for request type: {}", detectRequestType(systemContent));
        return Response.from(AiMessage.from(response));
    }

    private String detectRequestType(String systemContent) {
        if (systemContent.contains("decompose") && systemContent.contains("feature points")) {
            return "feature-point-split";
        }
        if (systemContent.contains("split a feature") || systemContent.contains("atomic implementation tasks")) {
            return "task-split";
        }
        if (systemContent.contains("generate code") || systemContent.contains("implement") || systemContent.contains("writeFile")) {
            return "code-generation";
        }
        if (systemContent.contains("code reviewer") || systemContent.contains("identify issues")) {
            return "code-review";
        }
        if (systemContent.contains("generate.*test") || systemContent.contains("JUnit") || systemContent.contains("pytest")) {
            return "test-generation";
        }
        if (systemContent.contains("requirement") || systemContent.contains("需求")) {
            return "requirement-analysis";
        }
        return "unknown";
    }

    private String generateMockResponse(String userContent, String systemContent) {
        String requestType = detectRequestType(systemContent);

        return switch (requestType) {
            case "requirement-analysis" -> generateMockRequirementAnalysis();
            case "feature-point-split" -> generateMockFeaturePointSplit();
            case "task-split" -> generateMockTaskSplit();
            case "code-generation" -> generateMockCodeGeneration(userContent);
            case "code-review" -> generateMockCodeReview();
            case "test-generation" -> generateMockTestGeneration();
            default -> generateMockDefault();
        };
    }

    private String generateMockRequirementAnalysis() {
        return """
                {
                  "projectType": "SPRING_BOOT",
                  "buildTool": "MAVEN",
                  "techStack": ["Java 21", "Spring Boot 3.3", "JPA", "H2"],
                  "parsedRequirement": {
                    "title": "添加健康检查端点",
                    "description": "为demo-backend添加一个健康检查API端点，用于监控服务状态",
                    "scope": ["新增端点"],
                    "constraints": [],
                    "acceptanceCriteria": ["GET /api/health 返回200状态码", "返回JSON格式的健康状态信息"]
                  }
                }
                """;
    }

    private String generateMockFeaturePointSplit() {
        return """
                [
                  {
                    "id": "fp-1",
                    "title": "健康检查控制器",
                    "description": "创建HealthController，提供健康检查API端点",
                    "acceptanceCriteria": ["GET /api/health 返回200状态码", "返回JSON格式的健康状态信息"],
                    "scope": "backend"
                  }
                ]
                """;
    }

    private String generateMockTaskSplit() {
        return """
                [
                  {
                    "id": "fp-1-task-1",
                    "featurePointId": "fp-1",
                    "title": "创建HealthController",
                    "description": "在demo-backend中创建HealthController类，添加GET /api/health端点",
                    "targetFiles": ["src/main/java/com/bage/demo/controller/HealthController.java"],
                    "type": "BACKEND"
                  }
                ]
                """;
    }

    private String generateMockCodeGeneration(String userContent) {
        return """
                {
                  "success": true,
                  "filePath": "src/main/java/com/bage/demo/controller/HealthController.java",
                  "code": "package com.bage.demo.controller;\\\\n\\\\nimport org.springframework.http.ResponseEntity;\\\\nimport org.springframework.web.bind.annotation.GetMapping;\\\\nimport org.springframework.web.bind.annotation.RequestMapping;\\\\nimport org.springframework.web.bind.annotation.RestController;\\\\nimport java.time.LocalDateTime;\\\\nimport java.util.Map;\\\\n\\\\n@RestController\\\\n@RequestMapping(\\\"/api/health\\\")\\\\npublic class HealthController {\\\\n\\\\n    @GetMapping\\\\n    public ResponseEntity<Map<String, Object>> health() {\\\\n        return ResponseEntity.ok(Map.of(\\\\n            \\\"status\\\", \\\"UP\\\",\\\\n            \\\"timestamp\\\", LocalDateTime.now().toString(),\\\\n            \\\"service\\\", \\\"demo-backend\\\"\\\\n        ));\\\\n    }\\\\n}",
                  "summary": "成功生成健康检查控制器代码"
                }
                """;
    }

    private String generateMockCodeReview() {
        return """
                {
                  "approved": true,
                  "score": 95,
                  "issues": [],
                  "suggestions": ["代码结构良好，符合Spring Boot最佳实践"]
                }
                """;
    }

    private String generateMockTestGeneration() {
        return """
                {
                  "success": true,
                  "testFilePath": "src/test/java/com/bage/demo/controller/HealthControllerTest.java",
                  "testCode": "package com.bage.demo.controller;\\\\n\\\\nimport org.junit.jupiter.api.Test;\\\\nimport org.springframework.beans.factory.annotation.Autowired;\\\\nimport org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;\\\\nimport org.springframework.test.web.servlet.MockMvc;\\\\nimport static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;\\\\nimport static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;\\\\nimport static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;\\\\n\\\\n@WebMvcTest(HealthController.class)\\\\npublic class HealthControllerTest {\\\\n\\\\n    @Autowired\\\\n    private MockMvc mockMvc;\\\\n\\\\n    @Test\\\\n    void health_shouldReturnUpStatus() throws Exception {\\\\n        mockMvc.perform(get(\\\"/api/health\\\"))\\\\n                .andExpect(status().isOk())\\\\n                .andExpect(jsonPath(\\\"$.status\\\").value(\\\"UP\\\"));\\\\n    }\\\\n}",
                  "summary": "成功生成单元测试代码"
                }
                """;
    }

    private String generateMockDefault() {
        return "{\"success\": true, \"message\": \"Mock response generated\"}";
    }
}