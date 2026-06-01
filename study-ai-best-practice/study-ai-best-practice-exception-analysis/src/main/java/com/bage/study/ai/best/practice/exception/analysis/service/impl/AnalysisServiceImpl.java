package com.bage.study.ai.best.practice.exception.analysis.service.impl;

import com.bage.study.ai.best.practice.exception.analysis.context.*;
import com.bage.study.ai.best.practice.exception.analysis.dto.request.AnalysisRequest;
import com.bage.study.ai.best.practice.exception.analysis.dto.response.AnalysisResponse;
import com.bage.study.ai.best.practice.exception.analysis.mcp.CodeMcpService;
import com.bage.study.ai.best.practice.exception.analysis.mcp.DeploymentMcpService;
import com.bage.study.ai.best.practice.exception.analysis.mcp.MetricsMcpService;
import com.bage.study.ai.best.practice.exception.analysis.service.AnalysisService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        String appId = request.getAppId();
        LocalDateTime alarmTime = request.getAlarmTime();
        
        RequestMetrics requestMetrics = metricsMcpService.getRequestMetrics(appId, alarmTime, null);
        AppMetrics appMetrics = metricsMcpService.getAppMetrics(appId, alarmTime);
        List<DeploymentRecord> deploymentRecords = deploymentMcpService.getDeploymentRecords(appId, alarmTime, null);
        CodeInfo codeInfo = codeMcpService.getCodeInfo(appId);

        List<AnalysisResponse.Evidence> evidences = collectEvidences(requestMetrics, appMetrics, deploymentRecords, codeInfo);
        AnalysisResponse.RootCause rootCause = analyzeRootCause(request.getAlarmDescription(), requestMetrics, appMetrics, deploymentRecords);
        List<String> suggestions = generateSuggestions(rootCause, evidences);

        AnalysisResponse response = new AnalysisResponse();
        response.setAnalysisId("ANALYSIS-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10));
        response.setAppId(appId);
        response.setAlarmDescription(request.getAlarmDescription());
        response.setAnalysisTime(LocalDateTime.now());
        response.setRootCause(rootCause);
        response.setEvidences(evidences);
        response.setSuggestions(suggestions);

        return response;
    }

    private List<AnalysisResponse.Evidence> collectEvidences(RequestMetrics requestMetrics, AppMetrics appMetrics,
                                                             List<DeploymentRecord> deploymentRecords, CodeInfo codeInfo) {
        List<AnalysisResponse.Evidence> evidences = new ArrayList<>();

        String errorRateRelevance = requestMetrics.getErrorRate() > 0.1 ? "高" : requestMetrics.getErrorRate() > 0.05 ? "中" : "低";
        evidences.add(new AnalysisResponse.Evidence(
            "请求量监控",
            String.format("错误率: %.2f%%, 总请求数: %d, 失败请求数: %d, 平均响应时间: %.2fms",
                requestMetrics.getErrorRate() * 100,
                requestMetrics.getTotalRequests(),
                requestMetrics.getFailedRequests(),
                requestMetrics.getAvgResponseTime()),
            errorRateRelevance
        ));

        String cpuRelevance = appMetrics.getCpuUsage() > 80 ? "高" : appMetrics.getCpuUsage() > 60 ? "中" : "低";
        evidences.add(new AnalysisResponse.Evidence(
            "应用监控(CPU)",
            String.format("CPU使用率: %.2f%%, 线程数: %d, 活跃线程: %d",
                appMetrics.getCpuUsage(),
                appMetrics.getThreadCount(),
                appMetrics.getActiveThreads()),
            cpuRelevance
        ));

        String memoryRelevance = appMetrics.getMemoryUsage() > 85 ? "高" : appMetrics.getMemoryUsage() > 70 ? "中" : "低";
        evidences.add(new AnalysisResponse.Evidence(
            "应用监控(内存)",
            String.format("内存使用率: %.2f%%, GC次数: %d, GC耗时: %.2fs",
                appMetrics.getMemoryUsage(),
                appMetrics.getGcCount(),
                appMetrics.getGcTime()),
            memoryRelevance
        ));

        if (!deploymentRecords.isEmpty()) {
            DeploymentRecord latest = deploymentRecords.get(0);
            String deployRelevance = "中";
            evidences.add(new AnalysisResponse.Evidence(
                "发布记录",
                String.format("最近发布版本: %s, 状态: %s, 发布时间: %s",
                    latest.getVersion(),
                    latest.getStatus(),
                    latest.getDeployTime()),
                deployRelevance
            ));
        }

        if (codeInfo != null) {
            String codeRelevance = codeInfo.getRecentChangesSummary().contains("修复") ? "高" : "中";
            evidences.add(new AnalysisResponse.Evidence(
                "代码仓库",
                String.format("最近提交: %s, 变更文件: %s, 变更摘要: %s",
                    codeInfo.getCommitId(),
                    codeInfo.getChangedFiles(),
                    codeInfo.getRecentChangesSummary()),
                codeRelevance
            ));
        }

        return evidences;
    }

    private AnalysisResponse.RootCause analyzeRootCause(String alarmDescription, RequestMetrics requestMetrics,
                                                       AppMetrics appMetrics, List<DeploymentRecord> deploymentRecords) {
        if (requestMetrics.getErrorRate() > 0.1) {
            return new AnalysisResponse.RootCause(
                "高错误率",
                "当前错误率(" + String.format("%.2f%%", requestMetrics.getErrorRate() * 100) + ")超过10%阈值，可能存在服务异常或业务逻辑错误",
                "85%"
            );
        }

        if (appMetrics.getCpuUsage() > 80) {
            return new AnalysisResponse.RootCause(
                "CPU过载",
                "CPU使用率(" + String.format("%.2f%%", appMetrics.getCpuUsage()) + ")超过80%，可能存在性能瓶颈或死循环",
                "80%"
            );
        }

        if (appMetrics.getMemoryUsage() > 85) {
            return new AnalysisResponse.RootCause(
                "内存不足",
                "内存使用率(" + String.format("%.2f%%", appMetrics.getMemoryUsage()) + ")超过85%，可能存在内存泄漏或资源未释放",
                "75%"
            );
        }

        if (!deploymentRecords.isEmpty()) {
            LocalDateTime deployTime = deploymentRecords.get(0).getDeployTime();
            return new AnalysisResponse.RootCause(
                "发布问题",
                "告警时间与最近发布时间接近，可能是新版本引入的问题",
                "70%"
            );
        }

        if (alarmDescription.contains("登录") || alarmDescription.contains("认证") || alarmDescription.contains("token")) {
            return new AnalysisResponse.RootCause(
                "认证问题",
                "告警描述涉及登录或认证功能，可能存在认证服务异常或token失效问题",
                "65%"
            );
        }

        return new AnalysisResponse.RootCause(
            "未知原因",
            "根据现有数据无法确定具体根因，建议进一步查看详细日志",
            "40%"
        );
    }

    private List<String> generateSuggestions(AnalysisResponse.RootCause rootCause, List<AnalysisResponse.Evidence> evidences) {
        List<String> suggestions = new ArrayList<>();

        switch (rootCause.getType()) {
            case "高错误率":
                suggestions.add("检查服务日志，定位具体错误类型");
                suggestions.add("查看失败请求的详细信息");
                suggestions.add("检查依赖服务是否正常");
                break;
            case "CPU过载":
                suggestions.add("分析线程dump，定位CPU消耗高的线程");
                suggestions.add("检查是否存在死循环或低效算法");
                suggestions.add("考虑增加实例数或优化代码");
                break;
            case "内存不足":
                suggestions.add("分析堆dump，查找内存泄漏");
                suggestions.add("检查GC日志，调整GC参数");
                suggestions.add("考虑增加堆内存或优化对象创建");
                break;
            case "发布问题":
                suggestions.add("回滚到上一个稳定版本");
                suggestions.add("检查发布内容，定位问题代码");
                suggestions.add("联系发布人员确认变更内容");
                break;
            case "认证问题":
                suggestions.add("检查认证服务状态");
                suggestions.add("验证JWT token配置");
                suggestions.add("查看用户认证日志");
                break;
            default:
                suggestions.add("查看应用详细日志");
                suggestions.add("检查服务器资源使用情况");
                suggestions.add("联系开发人员进一步排查");
        }

        suggestions.add("查看完整监控面板获取更多上下文");
        return suggestions;
    }
}