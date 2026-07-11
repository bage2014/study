package com.bage.ai.pipeline.api.activity;

import com.bage.ai.pipeline.api.dto.activity.PrCreationInput;
import com.bage.ai.pipeline.api.dto.activity.PrCreationResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface PrCreationActivity {

    @ActivityMethod(name = "PrCreation")
    PrCreationResult createPr(PrCreationInput input);
}