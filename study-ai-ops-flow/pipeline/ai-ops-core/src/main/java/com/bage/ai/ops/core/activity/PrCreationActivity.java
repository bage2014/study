package com.bage.ai.ops.core.activity;

import com.bage.ai.ops.core.dto.activity.PrCreationInput;
import com.bage.ai.ops.core.dto.activity.PrCreationResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface PrCreationActivity {

    @ActivityMethod(name = "PrCreation")
    PrCreationResult createPr(PrCreationInput input);
}