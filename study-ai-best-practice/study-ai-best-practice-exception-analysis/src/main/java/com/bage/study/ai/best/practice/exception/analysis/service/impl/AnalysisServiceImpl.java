package com.bage.study.ai.best.practice.exception.analysis.service.impl;

import com.bage.study.ai.best.practice.exception.analysis.context.*;
import com.bage.study.ai.best.practice.exception.analysis.dto.request.AnalysisRequest;
import com.bage.study.ai.best.practice.exception.analysis.dto.response.AnalysisResponse;
import com.bage.study.ai.best.practice.exception.analysis.dto.response.AnalysisResponse.Evidence;
import com.bage.study.ai.best.practice.exception.analysis.dto.response.AnalysisResponse.RootCause;
import com.bage.study.ai.best.practice.exception.analysis.mcp.CodeMcpService;
import com.bage.study.ai.best.practice.exception.analysis.mcp.DeploymentMcpService;
import com.bage.study.ai.best.practice.exception.analysis.mcp.MetricsMcpService;
import com.bage.study.ai.best.practice.exception.analysis.service.AnalysisService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalysisServiceImpl implements AnalysisService {

    private final DeploymentMcpService deploymentMcpService;
    private final MetricsMcpService metricsMcpService;
    private final CodeMcpService codeMcpService;

    public AnalysisServiceImpl(DeploymentMcpService deploymentMcpService, 
                               MetricsMcpService metricsMcpService, 
                               CodeMcpService codeMcpService) {
        this.deploymentMcpService = deploymentMcpService;
        this.metricsMcpService = metricsMcpService;
        this.codeMcpService = codeMcpService;
    }

    @Override
    public AnalysisResponse analyze(AnalysisRequest request) {
        String analysisId = generateAnalysisId();
        LocalDateTime analysisTime = LocalDateTime.now();

        List<DeploymentRecord> deployments = fetchDeploymentRecords(request.getAppId(), request.getAlarmTime());
        List<RequestMetrics> requestMetrics = fetchRequestMetrics(request.getAppId(), request.getAlarmTime());
        List<AppMetrics> appMetrics = fetchAppMetrics(request.getAppId(), request.getAlarmTime());
        List<CodeInfo> codeInfoList = fetchCodeInfo(request.getAppId(), request.getAlarmDescription());

        List<Evidence> evidences = collectEvidences(deployments, requestMetrics, appMetrics, codeInfoList);
        
        RootCause rootCause = analyzeRootCause(request, deployments, requestMetrics, appMetrics, codeInfoList);
        
        List<String> suggestions = generateSuggestions(rootCause, evidences);

        AnalysisResponse response = new AnalysisResponse();
        response.setAnalysisId(analysisId);
        response.setAppId(request.getAppId());
        response.setAlarmDescription(request.getAlarmDescription());
        response.setAnalysisTime(analysisTime);
        response.setRootCause(rootCause);
        response.setEvidences(evidences);
        response.setSuggestions(suggestions);

        return response;
    }

    private String generateAnalysisId() {
        return "ANALYSIS-" + System.currentTimeMillis();
    }

    private List<DeploymentRecord> fetchDeploymentRecords(String appId, LocalDateTime alarmTime) {
        LocalDateTime startTime = alarmTime != null ? alarmTime.minusHours(6) : LocalDateTime.now().minusHours(6);
        LocalDateTime endTime = alarmTime != null ? alarmTime.plusHours(1) : LocalDateTime.now();
        return deploymentMcpService.getDeploymentRecords(appId, startTime, endTime);
    }

    private List<RequestMetrics> fetchRequestMetrics(String appId, LocalDateTime alarmTime) {
        LocalDateTime startTime = alarmTime != null ? alarmTime.minusHours(3) : LocalDateTime.now().minusHours(3);
        LocalDateTime endTime = alarmTime != null ? alarmTime.plusHours(1) : LocalDateTime.now();
        return metricsMcpService.getRequestMetrics(appId, startTime, endTime);
    }

    private List<AppMetrics> fetchAppMetrics(String appId, LocalDateTime alarmTime) {
        LocalDateTime startTime = alarmTime != null ? alarmTime.minusHours(3) : LocalDateTime.now().minusHours(3);
        LocalDateTime endTime = alarmTime != null ? alarmTime.plusHours(1) : LocalDateTime.now();
        return metricsMcpService.getAppMetrics(appId, startTime, endTime);
    }

    private List<CodeInfo> fetchCodeInfo(String appId, String alarmDescription) {
        String keyword = extractKeyword(alarmDescription);
        return codeMcpService.getRelatedCode(appId, keyword);
    }

    private String extractKeyword(String description) {
        if (description.contains("登录")) return "login";
        if (description.contains("数据库") || description.contains("DB")) return "database";
        if (description.contains("内存") || description.contains("OOM")) return "memory";
        if (description.contains("线程")) return "thread";
        return "error";
    }

    private List<Evidence> collectEvidences(List<DeploymentRecord> deployments,
                                            List<RequestMetrics> requestMetrics,
                                            List<AppMetrics> appMetrics,
                                            List<CodeInfo> codeInfoList) {
        List<Evidence> evidences = new ArrayList<>();

        if (!deployments.isEmpty()) {
            DeploymentRecord latest = deployments.get(0);
            evidences.add(new Evidence("发布记录", 
                String.format("最近发布版本: %s, 发布时间: %s, 变更内容: %s", 
                    latest.getVersion(), latest.getDeployTime(), latest.getChanges()),
                "高"));
        }

        if (!requestMetrics.isEmpty()) {
            RequestMetrics latest = requestMetrics.get(0);
            evidences.add(new Evidence("请求监控",
                String.format("请求总量: %d, 成功率: %.1f%%, 平均响应时间: %.2fms, 错误率: %d%%",
                    latest.getTotalRequests(),
                    latest.getTotalRequests() > 0 ? (latest.getSuccessRequests() * 100.0 / latest.getTotalRequests()) : 0,
                    latest.getAvgResponseTime(),
                    latest.getErrorRate()),
                "高"));
        }

        if (!appMetrics.isEmpty()) {
            AppMetrics latest = appMetrics.get(0);
            evidences.add(new Evidence("应用监控",
                String.format("CPU使用率: %.1f%%, 内存使用率: %.1f%%, GC次数: %d, GC耗时: %dms, 线程数: %d",
                    latest.getCpuUsage(),
                    latest.getMemoryUsage(),
                    latest.getGcCount(),
                    latest.getGcTime(),
                    latest.getThreadCount()),
                "中"));
        }

        if (!codeInfoList.isEmpty()) {
            CodeInfo code = codeInfoList.get(0);
            evidences.add(new Evidence("代码分析",
                String.format("相关文件: %s, 方法: %s, 最后修改: %s",
                    code.getFile(), code.getMethod(), code.getLastModified()),
                "中"));
        }

        return evidences;
    }

    private RootCause analyzeRootCause(AnalysisRequest request,
                                       List<DeploymentRecord> deployments,
                                       List<RequestMetrics> requestMetrics,
                                       List<AppMetrics> appMetrics,
                                       List<CodeInfo> codeInfoList) {
        RootCause rootCause = new RootCause();

        if (!requestMetrics.isEmpty()) {
            RequestMetrics latest = requestMetrics.get(0);
            if (latest.getErrorRate() > 10) {
                rootCause.setType("高错误率");
                rootCause.setDescription("当前错误率(" + latest.getErrorRate() + "%)超过阈值，可能存在服务异常或性能问题");
                rootCause.setConfidence("85%");
                return rootCause;
            }
        }

        if (!appMetrics.isEmpty()) {
            AppMetrics latest = appMetrics.get(0);
            if (latest.getCpuUsage() > 80) {
                rootCause.setType("CPU过载");
                rootCause.setDescription("CPU使用率(" + String.format("%.1f", latest.getCpuUsage()) + "%)过高，可能存在CPU密集型操作或死循环");
                rootCause.setConfidence("80%");
                return rootCause;
            }
            if (latest.getMemoryUsage() > 85) {
                rootCause.setType("内存不足");
                rootCause.setDescription("内存使用率(" + String.format("%.1f", latest.getMemoryUsage()) + "%)过高，可能存在内存泄漏");
                rootCause.setConfidence("75%");
                return rootCause;
            }
        }

        if (!deployments.isEmpty()) {
            DeploymentRecord latest = deployments.get(0);
            if (isRecentDeployment(latest.getDeployTime(), request.getAlarmTime())) {
                rootCause.setType("发布问题");
                rootCause.setDescription("告警时间与最近发布时间(" + latest.getDeployTime() + ")接近，可能是新版本引入的问题");
                rootCause.setConfidence("70%");
                return rootCause;
            }
        }

        if (request.getAlarmDescription().contains("登录")) {
            rootCause.setType("认证问题");
            rootCause.setDescription("告警涉及登录功能，可能是认证逻辑或用户数据问题");
            rootCause.setConfidence("65%");
            return rootCause;
        }

        rootCause.setType("未知原因");
        rootCause.setDescription("根据现有数据无法确定具体原因，建议进一步排查");
        rootCause.setConfidence("50%");
        return rootCause;
    }

    private boolean isRecentDeployment(LocalDateTime deployTime, LocalDateTime alarmTime) {
        if (deployTime == null) return false;
        LocalDateTime alarm = alarmTime != null ? alarmTime : LocalDateTime.now();
        return java.time.Duration.between(deployTime, alarm).toHours() < 3;
    }

    private List<String> generateSuggestions(RootCause rootCause, List<Evidence> evidences) {
        List<String> suggestions = new ArrayList<>();

        switch (rootCause.getType()) {
            case "高错误率":
                suggestions.add("检查服务日志，定位具体错误类型");
                suggestions.add("查看失败请求的具体堆栈信息");
                suggestions.add("检查依赖服务是否正常");
                break;
            case "CPU过载":
                suggestions.add("使用profiling工具分析CPU热点");
                suggestions.add("检查是否有死循环或低效算法");
                suggestions.add("考虑增加实例数或升级配置");
                break;
            case "内存不足":
                suggestions.add("分析堆转储文件定位内存泄漏");
                suggestions.add("检查缓存策略是否合理");
                suggestions.add("考虑增加堆内存或优化对象生命周期");
                break;
            case "发布问题":
                suggestions.add("回滚到上一个稳定版本");
                suggestions.add("审查本次发布的代码变更");
                suggestions.add("检查数据库迁移脚本是否正确");
                break;
            case "认证问题":
                suggestions.add("检查认证服务是否正常");
                suggestions.add("验证JWT密钥配置");
                suggestions.add("检查用户数据完整性");
                break;
            default:
                suggestions.add("收集更多监控数据");
                suggestions.add("查看详细日志信息");
                suggestions.add("联系相关开发人员排查");
        }

        return suggestions;
    }
}
