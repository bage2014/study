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
public class NpmTool implements AgentTool {

    @Override
    public String toolName() { return "npm"; }

    @Tool("Run npm command in the given directory")
    public String run(@P("directory path") String dirPath, @P("npm command") String... args) {
        Path base = Paths.get(dirPath).toAbsolutePath().normalize();
        try {
            List<String> cmd = new ArrayList<>();
            cmd.add(isWindows() ? "npm.cmd" : "npm");
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

    @Tool("Start npm dev server")
    public String startDev(@P("directory path") String dirPath) {
        return run(dirPath, "run", "dev");
    }

    @Tool("Start npm preview server")
    public void startPreview(String dirPath, int port) throws IOException {
        Path base = Paths.get(dirPath).toAbsolutePath().normalize();
        List<String> cmd = new ArrayList<>();
        cmd.add(isWindows() ? "npm.cmd" : "npm");
        cmd.addAll(List.of("run", "preview", "--", "--port", String.valueOf(port)));

        new ProcessBuilder(cmd)
                .directory(base.toFile())
                .redirectErrorStream(true)
                .start();
    }

    @Tool("Install npm dependencies")
    public String install(@P("directory path") String dirPath) {
        return run(dirPath, "install");
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}