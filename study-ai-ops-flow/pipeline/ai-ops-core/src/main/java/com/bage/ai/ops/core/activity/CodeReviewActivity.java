package com.bage.ai.ops.core.activity;

import com.bage.ai.ops.core.dto.activity.CodeReviewInput;
import com.bage.ai.ops.core.dto.activity.CodeReviewResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface CodeReviewActivity {

    @ActivityMethod(name = "CodeReview")
    CodeReviewResult review(CodeReviewInput input);
}