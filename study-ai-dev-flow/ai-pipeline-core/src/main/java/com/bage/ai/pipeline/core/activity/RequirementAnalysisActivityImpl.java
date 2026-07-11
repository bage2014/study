package com.bage.ai.pipeline.core.activity;

import com.bage.ai.pipeline.agent.service.RequirementAgentService;
import com.bage.ai.pipeline.api.dto.activity.RequirementAnalysisInput;
import com.bage.ai.pipeline.api.dto.activity.RequirementAnalysisResult;
import com.bage.ai.pipeline.core.activity.RequirementAnalysisActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RequirementAnalysisActivityImpl implements RequirementAnalysisActivity {

    private final RequirementAgentService requirementAgentService;

    public RequirementAnalysisActivityImpl(RequirementAgentService requirementAgentService) {
        this.requirementAgentService = requirementAgentService;
    }

    @Override
    public RequirementAnalysisResult analyze(RequirementAnalysisInput input) {
        log.info("Starting requirement analysis for run: {}", input.getRunId());
        String parsedJson = requirementAgentService.analyze(input.getRequirementMd());
        log.info("Requirement analysis completed for run: {}", input.getRunId());
        return RequirementAnalysisResult.builder()
                .runId(input.getRunId())
                .parsedRequirementJson(parsedJson)
                .message("Requirement analysis completed")
                .build();
    }
}