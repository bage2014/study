package com.bage.ai.ops.core.activity;

import com.bage.ai.ops.core.dto.activity.TaskSplitInput;
import com.bage.ai.ops.core.dto.activity.TaskSplitResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface TaskSplitActivity {
    @ActivityMethod(name = "TaskSplit")
    TaskSplitResult split(TaskSplitInput input);
}