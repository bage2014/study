package com.bage.ai.pipeline.agent.tool;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class AstAnalysisTool implements AgentTool {

    @Override
    public String toolName() { return "ast-analysis"; }

    @Value("${agent.project-base-path:}")
    private String projectBasePath;

    private volatile String activePath;

    public void setActivePath(String path) {
        this.activePath = path;
    }

    @Tool("Analyze a Java file and extract class structure, methods, and fields")
    public String analyzeJavaFile(@P("relative file path") String relativePath) {
        try {
            Path filePath = resolveBase().resolve(relativePath).normalize();
            if (!Files.exists(filePath)) {
                return "ERROR: File not found: " + relativePath;
            }

            String content = Files.readString(filePath);
            CompilationUnit cu = new JavaParser().parse(content).getResult().orElse(null);
            if (cu == null) {
                return "ERROR: Failed to parse Java file";
            }

            StringBuilder result = new StringBuilder();
            cu.accept(new JavaFileAnalyzer(), result);
            return result.toString();

        } catch (IOException e) {
            return "ERROR: " + e.getMessage();
        }
    }

    @Tool("List all classes and their methods in a directory")
    public String listAllClasses(@P("relative directory path") String relativePath) {
        try {
            Path dirPath = resolveBase().resolve(relativePath).normalize();
            if (!Files.exists(dirPath)) {
                return "ERROR: Directory not found: " + relativePath;
            }

            StringBuilder result = new StringBuilder();
            Files.walk(dirPath)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String content = Files.readString(p);
                            CompilationUnit cu = new JavaParser().parse(content).getResult().orElse(null);
                            if (cu != null) {
                                cu.accept(new JavaClassLister(), result);
                            }
                        } catch (IOException e) {
                            // ignore
                        }
                    });

            return result.toString();

        } catch (IOException e) {
            return "ERROR: " + e.getMessage();
        }
    }

    private Path resolveBase() {
        String base = activePath != null ? activePath : projectBasePath;
        return Paths.get(base).toAbsolutePath().normalize();
    }

    private static class JavaFileAnalyzer extends VoidVisitorAdapter<StringBuilder> {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, StringBuilder arg) {
            arg.append("Class: ").append(n.getName()).append("\n");
            if (!n.getAnnotations().isEmpty()) {
                arg.append("  Annotations: ").append(n.getAnnotations()).append("\n");
            }
            arg.append("  Fields:\n");
            n.getFields().forEach(f -> {
                f.getVariables().forEach(v ->
                        arg.append("    - ").append(v.getType()).append(" ").append(v.getName()).append("\n"));
            });
            arg.append("  Methods:\n");
            n.getMethods().forEach(m ->
                    arg.append("    - ").append(m.getType()).append(" ").append(m.getName())
                            .append("(").append(m.getParameters()).append(")").append("\n"));
            super.visit(n, arg);
        }
    }

    private static class JavaClassLister extends VoidVisitorAdapter<StringBuilder> {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, StringBuilder arg) {
            arg.append("Class: ").append(n.getName()).append("\n");
            arg.append("  Methods: ");
            arg.append(n.getMethods().stream()
                    .map(m -> m.getNameAsString())
                    .toList());
            arg.append("\n\n");
            super.visit(n, arg);
        }
    }
}