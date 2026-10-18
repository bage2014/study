package com.bage.ai.pipeline.core.activity;

import com.bage.ai.pipeline.core.dto.activity.CompileVerifyInput;
import com.bage.ai.pipeline.core.dto.activity.CompileVerifyResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface CompileVerifyActivity {

    @ActivityMethod(name = "CompileVerify")
    CompileVerifyResult verify(CompileVerifyInput input);
}
