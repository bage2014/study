package com.bage.ai.pipeline.agent.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Component
public class FileWriteTool implements AgentTool {

    @Override
    public String toolName() { return "file-write"; }

    @Value("${agent.project-base-path:}")
    private String projectBasePath;

    private volatile String activePath;

    private final Map<String, String> writtenFiles = new HashMap<>();

    public void setActivePath(String path) {
        this.activePath = path;
        writtenFiles.clear();
    }

    public Map<String, String> getWrittenFiles() {
        return Map.copyOf(writtenFiles);
    }

    @Tool("Write or overwrite a file in the target project with the given content.")
    public String writeFile(
            @P("relative file path from project root") String relativePath,
            @P("complete new content of the file") String content) {
        try {
            Path base = resolveBase();
            Path target = base.resolve(relativePath).normalize();
            if (!target.startsWith(base)) {
                return "ERROR: Path traversal detected";
            }
            Files.createDirectories(target.getParent());
            Files.writeString(target, content);
            writtenFiles.put(relativePath, content);
            return "OK: written " + relativePath;
        } catch (IOException e) {
            return "ERROR: " + e.getMessage();
        }
    }

    private Path resolveBase() {
        String base = activePath != null ? activePath : projectBasePath;
        return Paths.get(base).toAbsolutePath().normalize();
    }
}