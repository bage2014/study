package com.bage.ai.pipeline.core.activity;

import com.bage.ai.pipeline.api.dto.activity.ApprovalWaitInput;
import com.bage.ai.pipeline.api.dto.activity.ApprovalWaitResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface ApprovalWaitActivity {

    @ActivityMethod(name = "ApprovalWait")
    ApprovalWaitResult waitForApproval(ApprovalWaitInput input);
}