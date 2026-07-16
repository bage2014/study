package com.bage.ai.pipeline.agent.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class MavenTool implements AgentTool {

    @Override
    public String toolName() { return "maven"; }

    @Value("${agent.project-base-path:}")
    private String projectBasePath;

    @Value("${agent.maven-path:/Users/bage/bage/software/apache-maven-3.8.5/bin/mvn}")
    private String mavenPath;

    private volatile String activePath;

    public void setActivePath(String path) {
        this.activePath = path;
    }

    private String getMavenCommand() {
        if (isWindows()) {
            return "mvn.cmd";
        }
        java.io.File mavenFile = new java.io.File(mavenPath);
        if (mavenFile.exists()) {
            return mavenFile.getAbsolutePath();
        }
        return "mvn";
    }

    @Tool("Run maven command in the project directory")
    public String run(@P("project path") String projectPath, @P("maven command") String... args) {
        Path base = Paths.get(projectPath).toAbsolutePath().normalize();
        try {
            List<String> cmd = new ArrayList<>();
            cmd.add(getMavenCommand());
            cmd.addAll(List.of(args));

            ProcessBuilder pb = new ProcessBuilder(cmd)
                    .directory(base.toFile())
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
            output.append("\nExit code: ").append(exitCode);
            return exitCode == 0 ? output.toString() : "ERROR: " + output;
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return "ERROR: " + e.getMessage();
        }
    }

    public MavenResult runSafe(String projectPath, String... args) {
        Path base = Paths.get(projectPath).toAbsolutePath().normalize();
        try {
            List<String> cmd = new ArrayList<>();
            cmd.add(getMavenCommand());
            cmd.addAll(List.of(args));

            ProcessBuilder pb = new ProcessBuilder(cmd)
                    .directory(base.toFile())
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
            return new MavenResult(exitCode, output.toString());
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return new MavenResult(-1, e.getMessage());
        }
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    public record MavenResult(int exitCode, String output) {}

    @Tool("Build maven project")
    public String build(@P("project path") String projectPath) {
        return run(projectPath, "clean", "package", "-DskipTests");
    }

    @Tool("Execute maven command")
    public String executeCommand(@P("project path") String projectPath, @P("command") String command) {
        return run(projectPath, command.split(" "));
    }
}