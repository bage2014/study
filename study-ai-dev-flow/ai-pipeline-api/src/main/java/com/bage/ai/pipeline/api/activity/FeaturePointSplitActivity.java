package com.bage.ai.pipeline.api.activity;

import com.bage.ai.pipeline.api.dto.activity.FeaturePointSplitInput;
import com.bage.ai.pipeline.api.dto.activity.FeaturePointSplitResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface FeaturePointSplitActivity {

    @ActivityMethod(name = "FeaturePointSplit")
    FeaturePointSplitResult split(FeaturePointSplitInput input);
}