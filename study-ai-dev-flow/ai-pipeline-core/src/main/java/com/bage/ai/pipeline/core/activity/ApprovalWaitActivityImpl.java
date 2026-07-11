package com.bage.ai.pipeline.core.activity;

import com.bage.ai.pipeline.api.entity.ApprovalEntity;
import com.bage.ai.pipeline.api.repository.ApprovalRepository;
import com.bage.ai.pipeline.api.dto.activity.ApprovalWaitInput;
import com.bage.ai.pipeline.api.dto.activity.ApprovalWaitResult;
import com.bage.ai.pipeline.core.activity.ApprovalWaitActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApprovalWaitActivityImpl implements ApprovalWaitActivity {

    private final ApprovalRepository approvalRepository;

    public ApprovalWaitActivityImpl(ApprovalRepository approvalRepository) {
        this.approvalRepository = approvalRepository;
    }

    @Override
    public ApprovalWaitResult waitForApproval(ApprovalWaitInput input) {
        log.info("Waiting for approval for pipeline: {}, stage: {}", input.getPipelineId(), input.getStage());

        return approvalRepository.findByPipelineIdAndStage(input.getPipelineId(), input.getStage())
                .map(approval -> ApprovalWaitResult.builder()
                        .approved(approval.getApproved())
                        .reviewer(approval.getReviewer())
                        .comment(approval.getComment())
                        .build())
                .orElse(ApprovalWaitResult.builder()
                        .approved(false)
                        .comment("No approval found")
                        .build());
    }
}