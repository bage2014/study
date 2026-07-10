package com.bage.ai.pipeline.core.activity;

import com.bage.ai.pipeline.core.dto.activity.FeaturePointSplitInput;
import com.bage.ai.pipeline.core.dto.activity.FeaturePointSplitResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface FeaturePointSplitActivity {

    @ActivityMethod(name = "FeaturePointSplit")
    FeaturePointSplitResult split(FeaturePointSplitInput input);
}