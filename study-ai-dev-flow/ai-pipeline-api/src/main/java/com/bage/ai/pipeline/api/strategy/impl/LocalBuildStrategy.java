package com.bage.ai.pipeline.api.strategy.impl;

import com.bage.ai.pipeline.agent.tool.MavenTool;
import com.bage.ai.pipeline.agent.tool.NpmTool;
import com.bage.ai.pipeline.core.dto.activity.BuildInput;
import com.bage.ai.pipeline.core.dto.activity.BuildResult;
import com.bage.ai.pipeline.core.enums.BuildTool;
import com.bage.ai.pipeline.core.strategy.BuildStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LocalBuildStrategy implements BuildStrategy {

    private final MavenTool mavenTool;
    private final NpmTool npmTool;

    public LocalBuildStrategy(MavenTool mavenTool, NpmTool npmTool) {
        this.mavenTool = mavenTool;
        this.npmTool = npmTool;
    }

    @Override
    public BuildResult build(BuildInput input) {
        try {
            String projectPath = input.getProjectLocalPath();
            BuildTool buildTool = input.getBuildTool();

            String buildOutput;
            if (buildTool == null || buildTool == BuildTool.MAVEN) {
                buildOutput = mavenTool.build(projectPath);
            } else if (buildTool == BuildTool.GRADLE) {
                buildOutput = mavenTool.executeCommand(projectPath, "./gradlew build");
            } else {
                buildOutput = npmTool.run(projectPath, "run", "build");
            }

            boolean success = !buildOutput.contains("BUILD FAILURE") &&
                    !buildOutput.contains("BUILD FAILED") &&
                    !buildOutput.contains("Error") &&
                    !buildOutput.contains("error");

            log.info("Local build completed for project: {}, success: {}", projectPath, success);
            return BuildResult.builder()
                    .runId(input.getRunId())
                    .success(success)
                    .buildOutput(buildOutput)
                    .imageName(input.getImageName())
                    .imageTag("latest")
                    .build();
        } catch (Exception e) {
            log.error("Local build failed: {}", e.getMessage());
            return BuildResult.builder()
                    .runId(input.getRunId())
                    .success(false)
                    .buildOutput("Build failed: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public String getName() {
        return "local";
    }
}
