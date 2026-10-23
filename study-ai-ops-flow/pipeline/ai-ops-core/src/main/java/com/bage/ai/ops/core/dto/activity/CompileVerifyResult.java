package com.bage.ai.ops.core.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompileVerifyResult {
    private String runId;
    private Boolean success;
    /** 编译输出（失败时含错误信息，供自修复上下文） */
    private String output;
    /** 是否跳过（非 Maven 项目等场景） */
    private boolean skipped;
}
