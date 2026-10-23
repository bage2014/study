package com.bage.ai.ops.core.dto.activity;

import com.bage.ai.ops.core.enums.BuildTool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompileVerifyInput {
    private String runId;
    private String projectLocalPath;
    private BuildTool buildTool;
}
