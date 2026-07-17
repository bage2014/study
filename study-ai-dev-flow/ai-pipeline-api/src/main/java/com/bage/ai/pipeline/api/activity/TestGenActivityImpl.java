package com.bage.ai.pipeline.api.activity;

import com.bage.ai.pipeline.agent.service.TestGenAgentService;
import com.bage.ai.pipeline.core.dto.activity.TestGenInput;
import com.bage.ai.pipeline.core.dto.activity.TestGenResult;
import com.bage.ai.pipeline.core.activity.TestGenActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TestGenActivityImpl implements TestGenActivity {

    private final TestGenAgentService testGenAgentService;

    public TestGenActivityImpl(TestGenAgentService testGenAgentService) {
        this.testGenAgentService = testGenAgentService;
    }

    @Override
    public TestGenResult generate(TestGenInput input) {
        log.info("Starting test generation for pipeline: {}", input.getPipelineId());

        List<String> filePaths = input.getGeneratedFilePaths() != null
                ? input.getGeneratedFilePaths()
                : input.getGeneratedFiles().keySet().stream().toList();

        String projectPath = input.getProjectPath() != null ? input.getProjectPath() : input.getProjectLocalPath();
        
        Map<String, String> testFiles = testGenAgentService.generateTests(
                projectPath,
                input.getProjectType(),
                filePaths
        );

        log.info("Test generation completed: {} test files generated", testFiles.size());
        return TestGenResult.builder()
                .success(true)
                .testFiles(testFiles.isEmpty() ? new HashMap<>() : testFiles)
                .build();
    }
}