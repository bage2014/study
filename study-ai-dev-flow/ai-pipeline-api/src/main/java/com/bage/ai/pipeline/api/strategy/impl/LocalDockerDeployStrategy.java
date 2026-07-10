package com.bage.ai.pipeline.api.strategy.impl;

import com.bage.ai.pipeline.agent.tool.DockerTool;
import com.bage.ai.pipeline.core.dto.activity.DeployInput;
import com.bage.ai.pipeline.core.dto.activity.DeployResult;
import com.bage.ai.pipeline.api.strategy.DeployStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LocalDockerDeployStrategy implements DeployStrategy {

    private final DockerTool dockerTool;

    public LocalDockerDeployStrategy(DockerTool dockerTool) {
        this.dockerTool = dockerTool;
    }

    @Override
    public DeployResult deploy(DeployInput input) {
        try {
            String projectPath = input.getProjectPath();
            String imageName = input.getImageName();
            String imageTag = input.getImageTag() != null ? input.getImageTag() : "latest";
            String containerName = input.getContainerName();
            int hostPort = input.getHostPort();
            int containerPort = input.getContainerPort();

            String fullImageName = imageName + ":" + imageTag;
            String buildOutput = dockerTool.build(projectPath, fullImageName);
            String runOutput = dockerTool.run(imageName, imageTag, containerName, hostPort, containerPort);

            log.info("Local Docker deployment completed for project: {}", projectPath);
            return DeployResult.builder()
                    .runId(input.getRunId())
                    .success(true)
                    .containerId(runOutput)
                    .accessUrl("http://localhost:" + hostPort)
                    .deployedUrl("http://localhost:" + hostPort)
                    .deployInfo("Docker deployment: " + buildOutput + "\n" + runOutput)
                    .output(buildOutput + "\n" + runOutput)
                    .build();
        } catch (Exception e) {
            log.error("Local Docker deployment failed: {}", e.getMessage());
            return DeployResult.builder()
                    .runId(input.getRunId())
                    .success(false)
                    .deployInfo("Deployment failed: " + e.getMessage())
                    .output("Deployment failed: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public String getName() {
        return "local-docker";
    }
}