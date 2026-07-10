package com.bage.ai.pipeline.agent.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileReadTool implements AgentTool {

    @Override
    public String toolName() { return "file-read"; }

    @Value("${agent.project-base-path:}")
    private String projectBasePath;

    private volatile String activePath;

    public void setActivePath(String path) {
        this.activePath = path;
    }

    @Tool("Read the content of a file in the target project. Returns the file content as a string.")
    public String readFile(@P("relative file path from project root, e.g. src/main/java/com/example/Foo.java") String relativePath) {
        try {
            Path resolved = resolveBase().resolve(relativePath).normalize();
            if (!resolved.startsWith(resolveBase())) {
                return "ERROR: Path traversal detected";
            }
            if (!Files.exists(resolved)) {
                return "ERROR: File not found: " + relativePath;
            }
            return Files.readString(resolved);
        } catch (IOException e) {
            return "ERROR: " + e.getMessage();
        }
    }

    @Tool("List files and directories under a given path in the target project (max 2 levels deep).")
    public String listDirectory(@P("relative directory path, use '.' for root") String relativePath) {
        try {
            Path base = resolveBase();
            Path target = base.resolve(relativePath).normalize();
            if (!target.startsWith(base)) {
                return "ERROR: Path traversal detected";
            }
            StringBuilder sb = new StringBuilder();
            listRecursive(target, base, sb, 0, 2);
            return sb.toString();
        } catch (IOException e) {
            return "ERROR: " + e.getMessage();
        }
    }

    private void listRecursive(Path dir, Path base, StringBuilder sb, int depth, int maxDepth) throws IOException {
        if (depth > maxDepth || !Files.isDirectory(dir)) return;
        try (var stream = Files.list(dir)) {
            for (Path p : stream.sorted().toList()) {
                String relative = base.relativize(p).toString();
                sb.append("  ".repeat(depth)).append(Files.isDirectory(p) ? "[D] " : "[F] ").append(relative).append("\n");
                if (Files.isDirectory(p)) {
                    listRecursive(p, base, sb, depth + 1, maxDepth);
                }
            }
        }
    }

    private Path resolveBase() {
        String base = activePath != null ? activePath : projectBasePath;
        return Paths.get(base).toAbsolutePath().normalize();
    }
}