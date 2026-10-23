package com.bage.ai.ops.core.strategy;

import com.bage.ai.ops.core.dto.activity.PrCreationInput;
import com.bage.ai.ops.core.dto.activity.PrCreationResult;

public interface PrCreationStrategy {

    PrCreationResult createPr(PrCreationInput input);

    String getName();
}