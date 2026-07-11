package com.bage.ai.pipeline.api.strategy;

import com.bage.ai.pipeline.api.dto.activity.BuildInput;
import com.bage.ai.pipeline.api.dto.activity.BuildResult;

public interface BuildStrategy {

    BuildResult build(BuildInput input);

    String getName();
}