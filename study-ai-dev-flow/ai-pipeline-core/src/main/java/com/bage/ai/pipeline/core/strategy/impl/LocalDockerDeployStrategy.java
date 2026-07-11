package com.bage.ai.pipeline.core.strategy.impl;

import com.bage.ai.pipeline.agent.tool.DockerTool;
import com.bage.ai.pipeline.api.dto.activity.DeployInput;
import com.bage.ai.pipeline.api.dto.activity.DeployResult;
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
        log.info("Local deployment started for project: {}", input.getProjectPath());
        return deployDirectly(input.getProjectPath(), input.getContainerName(), input.getHostPort());
    }

    private DeployResult deployDirectly(String projectPath, String containerName, int hostPort) {
        try {
            log.info("Deploying directly using Java runtime");
            java.io.File targetDir = new java.io.File(projectPath, "target");
            java.io.File[] jarFiles = targetDir.listFiles((dir, name) -> name.endsWith(".jar"));
            
            if (jarFiles == null || jarFiles.length == 0) {
                throw new RuntimeException("No JAR file found in target directory");
            }
            
            String jarPath = jarFiles[0].getAbsolutePath();
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", jarPath, "--server.port=" + hostPort)
                    .directory(new java.io.File(projectPath))
                    .redirectErrorStream(true);
            
            Process process = pb.start();
            log.info("Direct deployment started, process ID: {}", process.pid());
            
            Thread.sleep(5000);
            
            if (process.isAlive()) {
                log.info("Local deployment completed successfully");
                return DeployResult.builder()
                        .success(true)
                        .containerId(String.valueOf(process.pid()))
                        .accessUrl("http://localhost:" + hostPort)
                        .deployedUrl("http://localhost:" + hostPort)
                        .deployInfo("Direct Java deployment successful")
                        .output("Process started with PID: " + process.pid())
                        .build();
            } else {
                throw new RuntimeException("Process exited immediately");
            }
        } catch (Exception e) {
            log.error("Direct deployment failed: {}", e.getMessage());
            return DeployResult.builder()
                    .success(false)
                    .deployInfo("Direct deployment failed: " + e.getMessage())
                    .output("Direct deployment failed: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public String getName() {
        return "local-docker";
    }
}