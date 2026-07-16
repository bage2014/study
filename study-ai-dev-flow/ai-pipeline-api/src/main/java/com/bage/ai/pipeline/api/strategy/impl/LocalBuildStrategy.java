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

            MavenTool.MavenResult mavenResult;
            if (buildTool == null || buildTool == BuildTool.MAVEN) {
                mavenResult = mavenTool.runSafe(projectPath, "clean", "package", "-DskipTests");
            } else if (buildTool == BuildTool.GRADLE) {
                mavenResult = mavenTool.runSafe(projectPath, "./gradlew", "build");
            } else {
                String npmOutput = npmTool.run(projectPath, "run", "build");
                boolean npmSuccess = npmOutput != null && !npmOutput.contains("ERROR");
                log.info("Local npm build completed for project: {}, success: {}", projectPath, npmSuccess);
                return BuildResult.builder()
                        .runId(input.getRunId())
                        .success(npmSuccess)
                        .buildOutput(npmOutput)
                        .imageName(input.getImageName())
                        .imageTag("latest")
                        .build();
            }

            boolean success = mavenResult.exitCode() == 0;
            log.info("Local build completed for project: {}, exitCode: {}, success: {}", 
                    projectPath, mavenResult.exitCode(), success);
            return BuildResult.builder()
                    .runId(input.getRunId())
                    .success(success)
                    .buildOutput(mavenResult.output())
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
