package com.bage.ai.ops.core.strategy;

import com.bage.ai.ops.core.dto.activity.BuildInput;
import com.bage.ai.ops.core.dto.activity.BuildResult;

public interface BuildStrategy {

    BuildResult build(BuildInput input);

    String getName();
}