package com.bage.ai.pipeline.core.strategy;

import com.bage.ai.pipeline.core.dto.activity.BuildInput;
import com.bage.ai.pipeline.core.dto.activity.BuildResult;

public interface BuildStrategy {

    BuildResult build(BuildInput input);

    String getName();
}