package com.bage.ai.ops.api.activity;

import com.bage.ai.ops.core.dto.activity.RequirementAnalysisInput;
import com.bage.ai.ops.core.dto.activity.RequirementAnalysisResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface RequirementAnalysisActivity {

    @ActivityMethod(name = "RequirementAnalysis")
    RequirementAnalysisResult analyze(RequirementAnalysisInput input);
}