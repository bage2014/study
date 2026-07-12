package com.bage.ai.pipeline.api.activity;

import com.bage.ai.pipeline.core.dto.activity.TaskSplitInput;
import com.bage.ai.pipeline.core.dto.activity.TaskSplitResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface TaskSplitActivity {
    @ActivityMethod(name = "TaskSplit")
    TaskSplitResult split(TaskSplitInput input);
}