package com.bage.ai.ops.api.activity;

import com.bage.ai.ops.core.dto.activity.WriteAndCommitInput;
import com.bage.ai.ops.core.dto.activity.WriteAndCommitResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface WriteAndCommitActivity {

    @ActivityMethod(name = "WriteAndCommit")
    WriteAndCommitResult writeAndCommit(WriteAndCommitInput input);
}