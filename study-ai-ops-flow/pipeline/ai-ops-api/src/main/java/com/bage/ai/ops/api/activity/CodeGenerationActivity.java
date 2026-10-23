package com.bage.ai.ops.api.activity;

import com.bage.ai.ops.core.dto.activity.CodeGenInput;
import com.bage.ai.ops.core.dto.activity.CodeGenResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface CodeGenerationActivity {

    @ActivityMethod(name = "CodeGeneration")
    CodeGenResult generate(CodeGenInput input);

    @ActivityMethod(name = "CodeFix")
    CodeGenResult fixCode(CodeGenInput input);
}