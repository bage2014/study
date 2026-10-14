package com.bage.ai.pipeline.core.activity;

import com.bage.ai.pipeline.core.dto.activity.CodeReviewInput;
import com.bage.ai.pipeline.core.dto.activity.CodeReviewResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface CodeReviewActivity {

    @ActivityMethod(name = "CodeReview")
    CodeReviewResult review(CodeReviewInput input);
}