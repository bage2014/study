package com.bage.ai.ops.core.activity;

import com.bage.ai.ops.core.dto.activity.ApprovalWaitInput;
import com.bage.ai.ops.core.dto.activity.ApprovalWaitResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface ApprovalWaitActivity {

    @ActivityMethod(name = "ApprovalWait")
    ApprovalWaitResult waitForApproval(ApprovalWaitInput input);
}