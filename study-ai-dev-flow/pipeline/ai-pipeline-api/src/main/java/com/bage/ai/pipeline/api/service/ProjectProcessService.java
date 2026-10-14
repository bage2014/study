package com.bage.ai.pipeline.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ProjectProcessService {

    private final Map<Long, Process> runningProcesses = new ConcurrentHashMap<>();
    private final Map<Long, Integer> processPorts = new ConcurrentHashMap<>();

    public Map<String, Object> startProject(Long projectId, String projectPath, String projectType) {
        Map<String, Object> result = new HashMap<>();

        if (runningProcesses.containsKey(projectId)) {
            Process existing = runningProcesses.get(projectId);
            if (existing.isAlive()) {
                result.put("success", false);
                result.put("message", "项目已在运行中");
                result.put("status", "RUNNING");
                result.put("pid", existing.pid());
                result.put("port", processPorts.get(projectId));
                return result;
            } else {
                runningProcesses.remove(projectId);
                processPorts.remove(projectId);
            }
        }

        try {
            File projectDir = new File(projectPath);
            if (!projectDir.exists() || !projectDir.isDirectory()) {
                result.put("success", false);
                result.put("message", "项目路径不存在: " + projectPath);
                return result;
            }

            ProcessBuilder pb;
            int port = detectProjectPort(projectPath, projectType);

            if (isMavenProject(projectPath)) {
                pb = buildMavenStartCommand(projectPath, port);
            } else if (isNpmProject(projectPath)) {
                pb = buildNpmStartCommand(projectPath, port);
            } else if (isGradleProject(projectPath)) {
                pb = buildGradleStartCommand(projectPath, port);
            } else {
                result.put("success", false);
                result.put("message", "无法识别项目类型（需要pom.xml、build.gradle或package.json）");
                return result;
            }

            pb.directory(projectDir);
            pb.redirectErrorStream(true);
            pb.redirectOutput(ProcessBuilder.Redirect.appendTo(
                    new File(projectDir, "project-startup.log")));

            Process process = pb.start();
            runningProcesses.put(projectId, process);
            processPorts.put(projectId, port);

            log.info("项目启动成功 - projectId: {}, path: {}, pid: {}, port: {}",
                    projectId, projectPath, process.pid(), port);

            result.put("success", true);
            result.put("message", "项目启动成功");
            result.put("status", "STARTING");
            result.put("pid", process.pid());
            result.put("port", port);
            result.put("logFile", projectPath + "/project-startup.log");

        } catch (Exception e) {
            log.error("项目启动失败 - projectId: {}, error: {}", projectId, e.getMessage(), e);
            result.put("success", false);
            result.put("message", "项目启动失败: " + e.getMessage());
        }

        return result;
    }

    public Map<String, Object> stopProject(Long projectId) {
        Map<String, Object> result = new HashMap<>();

        Process process = runningProcesses.get(projectId);
        if (process == null) {
            result.put("success", false);
            result.put("message", "项目未在运行");
            result.put("status", "STOPPED");
            return result;
        }

        try {
            long pid = process.pid();
            int port = processPorts.getOrDefault(projectId, 0);

            killProcessTree(pid);

            if (!process.waitFor(10, TimeUnit.SECONDS)) {
                process.destroyForcibly();
            }

            runningProcesses.remove(projectId);
            processPorts.remove(projectId);

            if (port > 0) {
                killPortProcess(port);
            }

            log.info("项目停止成功 - projectId: {}, pid: {}", projectId, pid);
            result.put("success", true);
            result.put("message", "项目已停止");
            result.put("status", "STOPPED");
            result.put("pid", pid);

        } catch (Exception e) {
            log.error("项目停止失败 - projectId: {}, error: {}", projectId, e.getMessage(), e);
            result.put("success", false);
            result.put("message", "项目停止失败: " + e.getMessage());
        }

        return result;
    }

    public Map<String, Object> restartProject(Long projectId, String projectPath, String projectType) {
        Map<String, Object> stopResult = stopProject(projectId);
        log.info("重启 - 先停止: {}", stopResult.get("message"));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return startProject(projectId, projectPath, projectType);
    }

    public Map<String, Object> getProjectStatus(Long projectId) {
        Map<String, Object> result = new HashMap<>();

        Process process = runningProcesses.get(projectId);
        if (process == null || !process.isAlive()) {
            result.put("status", "STOPPED");
            result.put("running", false);
            if (process != null) {
                runningProcesses.remove(projectId);
                processPorts.remove(projectId);
            }
            return result;
        }

        result.put("status", "RUNNING");
        result.put("running", true);
        result.put("pid", process.pid());
        result.put("port", processPorts.get(projectId));
        return result;
    }

    private ProcessBuilder buildMavenStartCommand(String projectPath, int port) {
        ProcessBuilder pb = new ProcessBuilder();
        Map<String, String> env = pb.environment();
        env.put("SERVER_PORT", String.valueOf(port));

        String javaHome = System.getenv("JAVA_HOME");
        String mvnPath = findExecutable("mvn");

        if (mvnPath != null) {
            pb.command(mvnPath, "spring-boot:run",
                    "-Dmaven.test.skip=true",
                    "-Dspring-boot.run.arguments=--server.port=" + port);
        } else if (javaHome != null) {
            File jar = findBuiltJar(projectPath);
            if (jar != null) {
                pb.command(javaHome + "/bin/java", "-jar", jar.getAbsolutePath(),
                        "--server.port=" + port);
            } else {
                pb.command(javaHome + "/bin/java", "-jar",
                        projectPath + "/target/demo-0.0.1-SNAPSHOT.jar",
                        "--server.port=" + port);
            }
        } else {
            pb.command("mvn", "spring-boot:run",
                    "-Dmaven.test.skip=true",
                    "-Dspring-boot.run.arguments=--server.port=" + port);
        }

        return pb;
    }

    private ProcessBuilder buildNpmStartCommand(String projectPath, int port) {
        ProcessBuilder pb = new ProcessBuilder();
        Map<String, String> env = pb.environment();
        env.put("PORT", String.valueOf(port));

        String npmPath = findExecutable("npm");
        if (npmPath != null) {
            pb.command(npmPath, "run", "dev", "--", "--port", String.valueOf(port));
        } else {
            pb.command("npm", "run", "dev", "--", "--port", String.valueOf(port));
        }

        return pb;
    }

    private ProcessBuilder buildGradleStartCommand(String projectPath, int port) {
        ProcessBuilder pb = new ProcessBuilder();
        Map<String, String> env = pb.environment();
        env.put("SERVER_PORT", String.valueOf(port));

        String gradlew = projectPath + "/gradlew";
        if (new File(gradlew).exists()) {
            pb.command(gradlew, "bootRun", "--args=--server.port=" + port);
        } else {
            pb.command("gradle", "bootRun", "--args=--server.port=" + port);
        }

        return pb;
    }

    private int detectProjectPort(String projectPath, String projectType) {
        try {
            Path ymlPath = Paths.get(projectPath, "src/main/resources/application.yml");
            if (Files.exists(ymlPath)) {
                String content = Files.readString(ymlPath);
                String[] lines = content.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    if (lines[i].trim().startsWith("port:")) {
                        String portStr = lines[i].trim().replace("port:", "").trim();
                        return Integer.parseInt(portStr);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("检测端口失败，使用默认端口: {}", e.getMessage());
        }
        return 8080;
    }

    private boolean isMavenProject(String projectPath) {
        return new File(projectPath, "pom.xml").exists();
    }

    private boolean isNpmProject(String projectPath) {
        return new File(projectPath, "package.json").exists();
    }

    private boolean isGradleProject(String projectPath) {
        return new File(projectPath, "build.gradle").exists()
                || new File(projectPath, "build.gradle.kts").exists();
    }

    private File findBuiltJar(String projectPath) {
        File targetDir = new File(projectPath, "target");
        if (!targetDir.exists()) return null;
        File[] jars = targetDir.listFiles((dir, name) ->
                name.endsWith(".jar") && !name.contains("-sources") && !name.contains("-javadoc"));
        if (jars != null && jars.length > 0) {
            for (File jar : jars) {
                if (jar.getName().contains("SNAPSHOT") || jar.getName().contains("exec")) {
                    return jar;
                }
            }
            return jars[0];
        }
        return null;
    }

    private String findExecutable(String name) {
        String[] paths = System.getenv("PATH").split(File.pathSeparator);
        for (String path : paths) {
            File file = new File(path, name);
            if (file.exists() && file.canExecute()) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }

    private void killProcessTree(long pid) {
        try {
            ProcessBuilder pb = new ProcessBuilder("pkill", "-P", String.valueOf(pid));
            pb.start().waitFor(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.debug("pkill -P failed: {}", e.getMessage());
        }
        try {
            ProcessBuilder pb = new ProcessBuilder("kill", "-9", String.valueOf(pid));
            pb.start().waitFor(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.debug("kill -9 failed: {}", e.getMessage());
        }
    }

    private void killPortProcess(int port) {
        try {
            ProcessBuilder pb = new ProcessBuilder("lsof", "-ti", ":" + port);
            Process p = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String pidStr;
                while ((pidStr = reader.readLine()) != null) {
                    try {
                        long pid = Long.parseLong(pidStr.trim());
                        if (pid != ProcessHandle.current().pid()) {
                            new ProcessBuilder("kill", "-9", String.valueOf(pid))
                                    .start().waitFor(3, TimeUnit.SECONDS);
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        } catch (Exception e) {
            log.debug("killPortProcess failed: {}", e.getMessage());
        }
    }
}
