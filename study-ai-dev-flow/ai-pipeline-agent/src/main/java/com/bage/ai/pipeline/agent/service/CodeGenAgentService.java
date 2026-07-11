package com.bage.ai.pipeline.agent.service;

import com.bage.ai.pipeline.api.dto.activity.AtomicTask;
import com.bage.ai.pipeline.api.enums.BuildTool;
import com.bage.ai.pipeline.api.enums.ProjectType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CodeGenAgentService {

    public Map<String, String> generateCode(String projectPath, String parsedRequirementJson,
                                             ProjectType projectType, BuildTool buildTool) {
        return generateCode(projectPath, parsedRequirementJson, projectType, buildTool, null, null);
    }

    public Map<String, String> generateCode(String projectPath, String parsedRequirementJson,
                                             ProjectType projectType, BuildTool buildTool,
                                             String testFailureContext) {
        return generateCode(projectPath, parsedRequirementJson, projectType, buildTool,
                testFailureContext, null);
    }

    public Map<String, String> generateCode(String projectPath, String parsedRequirementJson,
                                             ProjectType projectType, BuildTool buildTool,
                                             String testFailureContext, AtomicTask currentTask) {
        Map<String, String> generatedFiles = new HashMap<>();

        if (currentTask != null && currentTask.getTargetFiles() != null) {
            for (String targetFile : currentTask.getTargetFiles()) {
                generatedFiles.put(targetFile, generateMockControllerCode());
            }
        } else {
            generatedFiles.put("src/main/java/com/bage/demo/controller/HealthController.java", generateMockControllerCode());
        }

        return generatedFiles;
    }

    private String generateMockControllerCode() {
        return """
                package com.bage.demo.controller;
                
                import org.springframework.http.ResponseEntity;
                import org.springframework.web.bind.annotation.GetMapping;
                import org.springframework.web.bind.annotation.RequestMapping;
                import org.springframework.web.bind.annotation.RestController;
                import java.time.LocalDateTime;
                import java.util.Map;
                
                @RestController
                @RequestMapping("/api/health")
                public class HealthController {
                
                    @GetMapping
                    public ResponseEntity<Map<String, Object>> health() {
                        return ResponseEntity.ok(Map.of(
                            "status", "UP",
                            "timestamp", LocalDateTime.now().toString(),
                            "service", "demo-backend"
                        ));
                    }
                }
                """;
    }
}