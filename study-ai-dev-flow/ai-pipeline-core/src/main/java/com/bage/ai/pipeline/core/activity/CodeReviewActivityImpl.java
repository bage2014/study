package com.bage.ai.pipeline.core.activity;

import com.bage.ai.pipeline.agent.service.CodeReviewAgentService;
import com.bage.ai.pipeline.api.dto.activity.CodeReviewInput;
import com.bage.ai.pipeline.api.dto.activity.CodeReviewResult;
import com.bage.ai.pipeline.core.activity.CodeReviewActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CodeReviewActivityImpl implements CodeReviewActivity {

    private final CodeReviewAgentService codeReviewAgentService;

    public CodeReviewActivityImpl(CodeReviewAgentService codeReviewAgentService) {
        this.codeReviewAgentService = codeReviewAgentService;
    }

    @Override
    public CodeReviewResult review(CodeReviewInput input) {
        log.info("Starting code review for run: {}", input.getRunId());
        CodeReviewAgentService.ReviewResult result = codeReviewAgentService.review(input.getGeneratedFiles());
        log.info("Code review completed, has critical issues: {}", result.hasCriticalIssues());
        return CodeReviewResult.builder()
                .runId(input.getRunId())
                .hasCriticalIssues(result.hasCriticalIssues())
                .issues(result.issues())
                .build();
    }
}