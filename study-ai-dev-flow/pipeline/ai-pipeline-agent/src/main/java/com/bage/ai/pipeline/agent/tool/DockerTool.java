package com.bage.ai.pipeline.agent.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class DockerTool implements AgentTool {

    @Override
    public String toolName() { return "docker"; }

    @Tool("Build a Docker image")
    public String build(@P("directory path") String dirPath, @P("image tag") String tag) {
        Path base = Paths.get(dirPath).toAbsolutePath().normalize();
        return runCommand(List.of("build", "-t", tag, "."), base);
    }

    @Tool("Run a Docker container")
    public String run(@P("image name") String imageName, @P("image tag") String tag,
                      @P("container name") String containerName,
                      @P("host port") int hostPort, @P("container port") int containerPort) {
        return runCommand("run", "-d", "--name", containerName,
                "-p", hostPort + ":" + containerPort,
                imageName + ":" + tag);
    }

    @Tool("Stop a container")
    public String stop(@P("container name") String containerName) {
        return runCommand("stop", containerName);
    }

    @Tool("Remove a container")
    public String remove(@P("container name") String containerName) {
        return runCommand("rm", "-f", containerName);
    }

    @Tool("Stop and remove a container")
    public String stopAndRemove(@P("container name") String containerName) {
        stop(containerName);
        return remove(containerName);
    }

    @Tool("List running containers")
    public String ps() {
        return runCommand("ps");
    }

    @Tool("List all containers")
    public String psAll() {
        return runCommand("ps", "-a");
    }

    private String runCommand(String... args) {
        return runCommand(List.of(args), Paths.get(".").toAbsolutePath());
    }

    private String runCommand(List<String> args, Path workingDir) {
        try {
            List<String> cmd = new ArrayList<>();
            cmd.add(isWindows() ? "docker.exe" : "docker");
            cmd.addAll(args);

            ProcessBuilder pb = new ProcessBuilder(cmd)
                    .directory(workingDir.toFile())
                    .redirectErrorStream(true);

            Process process = pb.start();
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            int exitCode = process.waitFor();
            return exitCode == 0 ? output.toString().trim() : "ERROR: " + output;
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return "ERROR: " + e.getMessage();
        }
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}