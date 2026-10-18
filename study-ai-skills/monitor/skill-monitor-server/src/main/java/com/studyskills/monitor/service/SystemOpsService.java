package com.studyskills.monitor.service;

import com.studyskills.monitor.repository.SkillUsageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 系统运维：监控服务自身状态查看 + 关闭/重启/Cron 操作。
 * 实际生命周期操作委托给客户端脚本 ~/.trae-cn/scripts/skill-usage.sh
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemOpsService {

    private static final String HOME = System.getProperty("user.home");
    private static final String SCRIPT = HOME + "/.trae-cn/scripts/skill-usage.sh";
    private static final String PLIST = HOME + "/Library/LaunchAgents/com.studyskills.skill-monitor.plist";
    private static final String JSONL = HOME + "/.trae-cn/skill-usage.jsonl";
    private static final String OFFSET = HOME + "/.trae-cn/skill-upload.offset";
    private static final String UPLOAD_LOG = HOME + "/.trae-cn/skill-upload.log";

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    private final SkillUsageRepository repo;
    private final SkillUsageService skillUsageService;

    /** 聚合系统状态（服务端页面 / API 共用） */
    public Map<String, Object> status() {
        Map<String, Object> m = new LinkedHashMap<>();

        // 服务本身（能响应本请求即为 up）
        var runtime = ManagementFactory.getRuntimeMXBean();
        m.put("serverUp", true);
        m.put("startTime", FMT.format(Instant.ofEpochMilli(runtime.getStartTime())));
        m.put("uptimeMinutes", Duration.ofMillis(runtime.getUptime()).toMinutes());

        // Cron
        boolean plistExists = Files.exists(Path.of(PLIST));
        boolean cronLoaded = runCmd(List.of("launchctl", "list", "com.studyskills.skill-monitor"), 5) != null;
        m.put("cronInstalled", plistExists);
        m.put("cronLoaded", cronLoaded);
        m.put("plistPath", PLIST);

        // 客户端埋点脚本
        Path script = Path.of(SCRIPT);
        m.put("scriptPath", SCRIPT);
        m.put("scriptExists", Files.exists(script));
        m.put("scriptExecutable", Files.isExecutable(script));

        // 数据管道
        long jsonlLines = countLines(JSONL);
        long offset = parseLongFile(OFFSET);
        m.put("jsonlLines", jsonlLines);
        m.put("offset", offset);
        m.put("pendingUpload", Math.max(0, jsonlLines - offset));
        m.put("dbRecords", repo.count());
        m.put("lastUploadLog", lastLine(UPLOAD_LOG));
        var last = skillUsageService.recent(1);
        m.put("lastRecordTs", last.isEmpty() ? null : FMT.format(last.get(0).getTs()));

        return m;
    }

    /** 关闭服务：先返回响应，1 秒后由 detached 脚本执行 pkill */
    public Map<String, Object> stopServer() {
        detach("stop-server");
        log.info("system stop requested via dashboard/api");
        return result("stopping", "关闭指令已发出，服务将在 1-2 秒内停止（页面将失联）");
    }

    /** 重启服务：先返回响应，detached 脚本执行 stop + start，约 20-40 秒恢复 */
    public Map<String, Object> restartServer() {
        detach("restart-server");
        log.info("system restart requested via dashboard/api");
        return result("restarting", "重启指令已发出，预计 20-40 秒后恢复，请稍后刷新");
    }

    /** Cron 安装/卸载（阻塞执行，命令本身很快） */
    public Map<String, Object> cronControl(boolean start) {
        String out = runScript(start ? "start-cron" : "stop-cron", 15);
        log.info("cron {} done: {}", start ? "install" : "uninstall", out);
        return result(start ? "cron-started" : "cron-stopped", out);
    }

    // ── helpers ──────────────────────────────────────────────

    /** fire-and-forget：后台执行脚本命令，不阻塞当前请求（服务可自行关闭/重启） */
    private void detach(String scriptCmd) {
        try {
            String sh = "nohup /bin/bash '" + SCRIPT + "' " + scriptCmd + " >/dev/null 2>&1 &";
            new ProcessBuilder("/bin/bash", "-c", sh).start();
        } catch (IOException e) {
            throw new IllegalStateException("failed to detach: " + scriptCmd, e);
        }
    }

    /** 阻塞执行脚本并返回输出 */
    private String runScript(String arg, int timeoutSec) {
        List<String> cmd = List.of("/bin/bash", SCRIPT, arg);
        return runCmd(cmd, timeoutSec);
    }

    /** 阻塞执行命令并返回输出；失败/超时返回 null */
    private String runCmd(List<String> cmd, int timeoutSec) {
        try {
            Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
            String out;
            try (var in = p.getInputStream()) {
                out = new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();
            }
            if (!p.waitFor(timeoutSec, TimeUnit.SECONDS)) {
                p.destroyForcibly();
                log.warn("command timeout: {}", cmd);
                return null;
            }
            return p.exitValue() == 0 ? out : null;
        } catch (Exception e) {
            log.warn("command failed: {} ({})", cmd, e.getMessage());
            return null;
        }
    }

    private Map<String, Object> result(String status, String message) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("status", status);
        m.put("message", message);
        return m;
    }

    private long countLines(String file) {
        try {
            return Files.readAllLines(Path.of(file), StandardCharsets.UTF_8).size();
        } catch (IOException e) {
            return 0;
        }
    }

    private long parseLongFile(String file) {
        try {
            return Long.parseLong(Files.readString(Path.of(file)).trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private String lastLine(String file) {
        try {
            List<String> lines = Files.readAllLines(Path.of(file), StandardCharsets.UTF_8);
            return lines.isEmpty() ? "-" : lines.get(lines.size() - 1);
        } catch (IOException e) {
            return "-";
        }
    }
}
