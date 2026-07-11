package com.bage.ai.pipeline.agent.service;

import com.bage.ai.pipeline.core.enums.ProjectType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestGenAgentService {

    public Map<String, String> generateTests(String projectPath, ProjectType projectType,
                                              List<String> generatedFilePaths) {
        Map<String, String> testFiles = new HashMap<>();

        for (String filePath : generatedFilePaths) {
            if (filePath.endsWith("Controller.java")) {
                String testPath = filePath.replace("src/main/java", "src/test/java")
                        .replace(".java", "Test.java");
                testFiles.put(testPath, generateMockControllerTest());
            }
        }

        return testFiles;
    }

    private String generateMockControllerTest() {
        return """
                package com.bage.demo.controller;
                
                import org.junit.jupiter.api.Test;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
                import org.springframework.test.web.servlet.MockMvc;
                import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
                import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
                import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
                
                @WebMvcTest(HealthController.class)
                public class HealthControllerTest {
                
                    @Autowired
                    private MockMvc mockMvc;
                
                    @Test
                    void health_shouldReturnUpStatus() throws Exception {
                        mockMvc.perform(get("/api/health"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("UP"));
                    }
                }
                """;
    }
}