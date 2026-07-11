package com.bage.ai.pipeline.core.activity;

import com.bage.ai.pipeline.api.dto.activity.RequirementAnalysisInput;
import com.bage.ai.pipeline.api.dto.activity.RequirementAnalysisResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface RequirementAnalysisActivity {

    @ActivityMethod(name = "RequirementAnalysis")
    RequirementAnalysisResult analyze(RequirementAnalysisInput input);
}