package com.bage.ai.pipeline.core.activity;

import com.bage.ai.pipeline.agent.service.CodeGenAgentService;
import com.bage.ai.pipeline.api.dto.activity.AtomicTask;
import com.bage.ai.pipeline.api.dto.activity.CodeGenInput;
import com.bage.ai.pipeline.api.dto.activity.CodeGenResult;
import com.bage.ai.pipeline.core.activity.CodeGenerationActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CodeGenerationActivityImpl implements CodeGenerationActivity {

    private final CodeGenAgentService codeGenAgentService;

    public CodeGenerationActivityImpl(CodeGenAgentService codeGenAgentService) {
        this.codeGenAgentService = codeGenAgentService;
    }

    @Override
    public CodeGenResult generate(CodeGenInput input) {
        log.info("Starting code generation for pipeline: {}", input.getPipelineId());

        Map<String, String> generatedFiles = codeGenAgentService.generateCode(
                input.getProjectPath(),
                input.getParsedRequirementJson(),
                input.getProjectType(),
                input.getBuildTool(),
                input.getTestFailureContext(),
                input.getCurrentTask()
        );

        log.info("Code generation completed: {} files generated", generatedFiles.size());
        return CodeGenResult.builder()
                .success(true)
                .generatedFiles(generatedFiles.isEmpty() ? new HashMap<>() : generatedFiles)
                .build();
    }

    @Override
    public CodeGenResult fixCode(CodeGenInput input) {
        log.info("Starting code fix for pipeline: {}", input.getPipelineId());

        Map<String, String> fixedFiles = codeGenAgentService.generateCode(
                input.getProjectPath(),
                input.getParsedRequirementJson(),
                input.getProjectType(),
                input.getBuildTool(),
                input.getTestFailureContext(),
                input.getCurrentTask()
        );

        log.info("Code fix completed: {} files fixed", fixedFiles.size());
        return CodeGenResult.builder()
                .success(true)
                .generatedFiles(fixedFiles.isEmpty() ? new HashMap<>() : fixedFiles)
                .build();
    }
}