package com.bage.ai.ops.api.activity;

import com.bage.ai.ops.agent.tool.MavenTool;
import com.bage.ai.ops.core.activity.CompileVerifyActivity;
import com.bage.ai.ops.core.dto.activity.CompileVerifyInput;
import com.bage.ai.ops.core.dto.activity.CompileVerifyResult;
import com.bage.ai.ops.core.enums.BuildTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 代码生成沙箱化编译验证：写盘后在目标项目执行 mvn compile，
 * 编译通过才算有效产出（借鉴 Devin/OpenHands 沙箱验证思想）。
 */
@Slf4j
@Component
public class CompileVerifyActivityImpl implements CompileVerifyActivity {

    /** 失败输出截断长度（避免超出 Activity 结果大小限制） */
    private static final int MAX_OUTPUT_LENGTH = 6000;

    private final MavenTool mavenTool;

    public CompileVerifyActivityImpl(MavenTool mavenTool) {
        this.mavenTool = mavenTool;
    }

    @Override
    public CompileVerifyResult verify(CompileVerifyInput input) {
        String projectPath = input.getProjectLocalPath();
        log.info("Compile verification started for run: {}, path: {}", input.getRunId(), projectPath);

        if (input.getBuildTool() != null && input.getBuildTool() != BuildTool.MAVEN) {
            log.info("Compile verification skipped: buildTool={} not supported yet", input.getBuildTool());
            return CompileVerifyResult.builder()
                    .runId(input.getRunId())
                    .success(true)
                    .skipped(true)
                    .output("Skipped: only Maven compile verification is supported")
                    .build();
        }

        try {
            MavenTool.MavenResult result = mavenTool.runSafe(projectPath, "compile", "-DskipTests", "-q");
            boolean success = result.exitCode() == 0;
            String output = result.output() != null ? result.output() : "";
            if (output.length() > MAX_OUTPUT_LENGTH) {
                output = output.substring(output.length() - MAX_OUTPUT_LENGTH);
            }
            log.info("Compile verification finished for run: {}, success: {}", input.getRunId(), success);
            return CompileVerifyResult.builder()
                    .runId(input.getRunId())
                    .success(success)
                    .skipped(false)
                    .output(output)
                    .build();
        } catch (Exception e) {
            log.error("Compile verification error for run: {}", input.getRunId(), e);
            return CompileVerifyResult.builder()
                    .runId(input.getRunId())
                    .success(false)
                    .skipped(false)
                    .output("Compile verification failed to execute: " + e.getMessage())
                    .build();
        }
    }
}
