package com.bage.ai.pipeline.api.strategy;

import com.bage.ai.pipeline.api.dto.activity.PrCreationInput;
import com.bage.ai.pipeline.api.dto.activity.PrCreationResult;

public interface PrCreationStrategy {

    PrCreationResult createPr(PrCreationInput input);

    String getName();
}