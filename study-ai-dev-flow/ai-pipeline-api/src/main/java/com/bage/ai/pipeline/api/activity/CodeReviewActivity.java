package com.bage.ai.pipeline.api.activity;

import com.bage.ai.pipeline.api.dto.activity.CodeReviewInput;
import com.bage.ai.pipeline.api.dto.activity.CodeReviewResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface CodeReviewActivity {

    @ActivityMethod(name = "CodeReview")
    CodeReviewResult review(CodeReviewInput input);
}