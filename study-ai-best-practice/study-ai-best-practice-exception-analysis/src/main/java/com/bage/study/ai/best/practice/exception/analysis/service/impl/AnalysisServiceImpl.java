package com.bage.study.ai.best.practice.exception.analysis.service.impl;

import com.bage.study.ai.best.practice.exception.analysis.context.*;
import com.bage.study.ai.best.practice.exception.analysis.dto.request.AnalysisRequest;
import com.bage.study.ai.best.practice.exception.analysis.dto.response.AnalysisResponse;
import com.bage.study.ai.best.practice.exception.analysis.mcp.CodeMcpService;
import com.bage.study.ai.best.practice.exception.analysis.mcp.DeploymentMcpService;
import com.bage.study.ai.best.practice.exception.analysis.mcp.MetricsMcpService;
import com.bage.study.ai.best.practice.exception.analysis.mcp.TopologyMcpService;
import com.bage.study.ai.best.practice.exception.analysis.service.AnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AnalysisServiceImpl implements AnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisServiceImpl.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final List<String> ALL_MODULES = Arrays.asList(
            "下单", "改单", "支付", "取消", "出票", "退票", "扣位"
    );

    private final DeploymentMcpService deploymentMcpService;
    private final MetricsMcpService metricsMcpService;
    private final CodeMcpService codeMcpService;
    private final TopologyMcpService topologyMcpService;
    private final Random random = new Random();

    public AnalysisServiceImpl(DeploymentMcpService deploymentMcpService,
                              MetricsMcpService metricsMcpService,
                              CodeMcpService codeMcpService,
                              TopologyMcpService topologyMcpService) {
        this.deploymentMcpService = deploymentMcpService;
        this.metricsMcpService = metricsMcpService;
        this.codeMcpService = codeMcpService;
        this.topologyMcpService = topologyMcpService;
    }

    @Override
    public AnalysisResponse analyze(AnalysisRequest request) {
        String analysisId = "ANALYSIS-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        
        logger.info("========== [{}] 开始异常分析 ==========", analysisId);
        logger.info("[{}] 输入参数: appId={}, alarmDescription={}, alarmTime={}, scene={}", 
            analysisId, 
            request.getAppId(), 
            request.getAlarmDescription(),
            request.getAlarmTime() != null ? request.getAlarmTime().format(FORMATTER) : "null",
            request.getScene());

        String appId = request.getAppId();
        LocalDateTime alarmTime = request.getAlarmTime();
        String alarmDescription = request.getAlarmDescription();

        logger.info("[{}] 【步骤1/6】开始收集上下文数据", analysisId);
        
        RequestMetrics requestMetrics = metricsMcpService.getRequestMetrics(appId, alarmTime, null);
        logger.info("[{}]  - 请求量监控数据: 总请求数={}, 错误率={:.2f}%, 平均响应时间={:.2f}ms", 
            analysisId, 
            requestMetrics.getTotalRequests(), 
            requestMetrics.getErrorRate() * 100,
            requestMetrics.getAvgResponseTime());

        AppMetrics appMetrics = metricsMcpService.getAppMetrics(appId, alarmTime);
        logger.info("[{}]  - 应用监控数据: CPU={:.2f}%, 内存={:.2f}%, 线程数={}, GC次数={}", 
            analysisId, 
            appMetrics.getCpuUsage(), 
            appMetrics.getMemoryUsage(),
            appMetrics.getThreadCount(),
            appMetrics.getGcCount());

        List<DeploymentRecord> deploymentRecords = deploymentMcpService.getDeploymentRecords(appId, alarmTime, null);
        logger.info("[{}]  - 发布记录: {}条记录", analysisId, deploymentRecords.size());
        if (!deploymentRecords.isEmpty()) {
            DeploymentRecord latest = deploymentRecords.get(0);
            logger.info("[{}]    最新发布: 版本={}, 状态={}, 时间={}", 
                analysisId, latest.getVersion(), latest.getStatus(), latest.getDeployTime());
        }

        CodeInfo codeInfo = codeMcpService.getCodeInfo(appId);
        logger.info("[{}]  - 代码仓库信息: commitId={}, 变更文件={}", 
            analysisId, 
            codeInfo != null ? codeInfo.getCommitId() : "null",
            codeInfo != null ? codeInfo.getChangedFiles() : "null");

        logger.info("[{}] 【步骤2/6】开始模块识别", analysisId);
        List<String> alarmModules = extractAlarmModules(alarmDescription);
        logger.info("[{}]  - 从告警描述中识别到模块: {}", analysisId, 
            alarmModules.isEmpty() ? "无" : String.join(", ", alarmModules));

        logger.info("[{}] 【步骤3/6】开始拓扑分析", analysisId);
        TopologyInfo topologyInfo = topologyMcpService.getTopology(appId);
        logger.info("[{}]  - 应用拓扑: appName={}, 模块数量={}, 上游应用数={}, 下游应用数={}", 
            analysisId,
            topologyInfo.getAppName(),
            topologyInfo.getModules() != null ? topologyInfo.getModules().size() : 0,
            topologyInfo.getUpstreamApps() != null ? topologyInfo.getUpstreamApps().size() : 0,
            topologyInfo.getDownstreamApps() != null ? topologyInfo.getDownstreamApps().size() : 0);

        boolean forceMqBacklog = "mq_backlog".equals(request.getScene());
        logger.info("[{}]  - 场景模式: forceMqBacklog={}", analysisId, forceMqBacklog);

        logger.info("[{}] 【步骤4/6】开始收集MQ和JOB指标", analysisId);
        List<MqMetrics> mqMetricsList = collectMqMetrics(topologyInfo, alarmModules, forceMqBacklog);
        List<JobMetrics> jobMetricsList = collectJobMetrics(topologyInfo, alarmModules);
        
        long backlogMqCount = mqMetricsList.stream().filter(MqMetrics::isBacklog).count();
        long failedJobCount = jobMetricsList.stream().filter(j -> j.getFailureCount() > 0).count();
        logger.info("[{}]  - MQ指标: 总数={}, 积压MQ数={}", analysisId, mqMetricsList.size(), backlogMqCount);
        logger.info("[{}]  - JOB指标: 总数={}, 失败JOB数={}", analysisId, jobMetricsList.size(), failedJobCount);

        logger.info("[{}] 【步骤5/6】开始证据收集", analysisId);
        List<AnalysisResponse.Evidence> evidences = collectEvidences(
            requestMetrics, appMetrics, deploymentRecords, codeInfo, alarmModules, topologyInfo, mqMetricsList, jobMetricsList);
        
        long highRelevanceCount = evidences.stream().filter(e -> "高".equals(e.getRelevance()) || e.getRelevance().contains("高")).count();
        long disturbanceCount = evidences.stream().filter(e -> e.getRelevance().contains("干扰项")).count();
        logger.info("[{}]  - 收集到证据数: {}, 高相关性证据数: {}, 干扰项数: {}", 
            analysisId, evidences.size(), highRelevanceCount, disturbanceCount);

        logger.info("[{}] 【步骤6/6】开始根因分析", analysisId);
        AnalysisResponse.RootCause rootCause = analyzeRootCause(
            alarmDescription, requestMetrics, appMetrics, deploymentRecords, alarmModules, topologyInfo, mqMetricsList, jobMetricsList);
        
        logger.info("[{}]  - 根因分析结果: 类型={}, 置信度={}, 描述={}", 
            analysisId, rootCause.getType(), rootCause.getConfidence(), rootCause.getDescription());

        logger.info("[{}] 生成修复建议", analysisId);
        List<String> suggestions = generateSuggestions(rootCause, evidences, alarmModules, topologyInfo, mqMetricsList);
        logger.info("[{}]  - 建议数量: {}", analysisId, suggestions.size());

        AnalysisResponse response = new AnalysisResponse();
        response.setAnalysisId(analysisId);
        response.setAppId(appId);
        response.setAlarmDescription(alarmDescription);
        response.setAnalysisTime(LocalDateTime.now());
        response.setRootCause(rootCause);
        response.setEvidences(evidences);
        response.setSuggestions(suggestions);

        logger.info("[{}] ========== 异常分析完成 ==========", analysisId);
        
        return response;
    }

    private List<String> extractAlarmModules(String alarmDescription) {
        List<String> foundModules = new ArrayList<>();
        for (String module : ALL_MODULES) {
            if (alarmDescription.contains(module)) {
                logger.debug("识别到模块: {} (告警描述包含该模块名称)", module);
                foundModules.add(module);
            }
        }
        return foundModules;
    }

    private List<String> getDisturbanceModules(List<String> alarmModules) {
        List<String> disturbanceModules = new ArrayList<>(ALL_MODULES);
        disturbanceModules.removeAll(alarmModules);
        return disturbanceModules;
    }

    private List<MqMetrics> collectMqMetrics(TopologyInfo topologyInfo, List<String> alarmModules, boolean forceMqBacklog) {
        List<MqMetrics> mqMetricsList = new ArrayList<>();

        if (topologyInfo.getModules() != null) {
            for (TopologyInfo.ModuleDependency module : topologyInfo.getModules()) {
                if (module.getMqDependencies() != null) {
                    for (TopologyInfo.DependencyItem mqDep : module.getMqDependencies()) {
                        boolean shouldBacklog = forceMqBacklog && alarmModules.contains(module.getModuleName());
                        MqMetrics mqMetric = topologyMcpService.generateMqMetrics(mqDep.getName(), shouldBacklog);
                        mqMetricsList.add(mqMetric);
                        
                        if (mqMetric.isBacklog()) {
                            logger.debug("发现MQ积压: {} (模块={}, 积压={}, 增长率={:.2f}/s)", 
                                mqMetric.getMqName(), 
                                module.getModuleName(), 
                                mqMetric.getLag(), 
                                mqMetric.getLagIncreaseRate());
                        }
                    }
                }
            }
        }
        return mqMetricsList;
    }

    private List<JobMetrics> collectJobMetrics(TopologyInfo topologyInfo, List<String> alarmModules) {
        List<JobMetrics> jobMetricsList = new ArrayList<>();

        if (topologyInfo.getModules() != null) {
            for (TopologyInfo.ModuleDependency module : topologyInfo.getModules()) {
                if (module.getJobDependencies() != null) {
                    for (TopologyInfo.DependencyItem job : module.getJobDependencies()) {
                        JobMetrics jobMetric = topologyMcpService.generateJobMetrics(job.getName());
                        jobMetricsList.add(jobMetric);
                        
                        if (jobMetric.getFailureCount() > 0) {
                            logger.debug("发现JOB失败: {} (模块={}, 失败次数={}, 成功率={:.2f}%)", 
                                jobMetric.getJobName(), 
                                module.getModuleName(), 
                                jobMetric.getFailureCount(), 
                                jobMetric.getSuccessRate());
                        }
                    }
                }
            }
        }
        return jobMetricsList;
    }

    private List<AnalysisResponse.Evidence> collectEvidences(RequestMetrics requestMetrics, AppMetrics appMetrics,
                                                             List<DeploymentRecord> deploymentRecords, CodeInfo codeInfo,
                                                             List<String> alarmModules, TopologyInfo topologyInfo,
                                                             List<MqMetrics> mqMetricsList, List<JobMetrics> jobMetricsList) {
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
        if ("高".equals(errorRateRelevance)) {
            logger.debug("高相关性证据: 请求量监控 - 错误率超过10%阈值");
        }

        String cpuRelevance = appMetrics.getCpuUsage() > 80 ? "高" : appMetrics.getCpuUsage() > 60 ? "中" : "低";
        evidences.add(new AnalysisResponse.Evidence(
            "应用监控(CPU)",
            String.format("CPU使用率: %.2f%%, 线程数: %d, 活跃线程: %d",
                appMetrics.getCpuUsage(),
                appMetrics.getThreadCount(),
                appMetrics.getActiveThreads()),
            cpuRelevance
        ));
        if ("高".equals(cpuRelevance)) {
            logger.debug("高相关性证据: 应用监控(CPU) - CPU使用率超过80%");
        }

        String memoryRelevance = appMetrics.getMemoryUsage() > 85 ? "高" : appMetrics.getMemoryUsage() > 70 ? "中" : "低";
        evidences.add(new AnalysisResponse.Evidence(
            "应用监控(内存)",
            String.format("内存使用率: %.2f%%, GC次数: %d, GC耗时: %.2fs",
                appMetrics.getMemoryUsage(),
                appMetrics.getGcCount(),
                appMetrics.getGcTime()),
            memoryRelevance
        ));
        if ("高".equals(memoryRelevance)) {
            logger.debug("高相关性证据: 应用监控(内存) - 内存使用率超过85%");
        }

        if (!deploymentRecords.isEmpty()) {
            DeploymentRecord latest = deploymentRecords.get(0);
            evidences.add(new AnalysisResponse.Evidence(
                "发布记录",
                String.format("最近发布版本: %s, 状态: %s, 发布时间: %s",
                    latest.getVersion(),
                    latest.getStatus(),
                    latest.getDeployTime()),
                "中"
            ));
            logger.debug("证据: 发布记录 - 存在最近发布");
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
            if ("高".equals(codeRelevance)) {
                logger.debug("高相关性证据: 代码仓库 - 最近提交包含修复内容");
            }
        }

        for (MqMetrics mq : mqMetricsList) {
            String relevance;
            if (mq.isBacklog()) {
                relevance = "高-需关注";
                logger.debug("高相关性证据: MQ监控({}) - 存在积压", mq.getMqName());
            } else {
                relevance = "低";
            }
            evidences.add(new AnalysisResponse.Evidence(
                "MQ监控(" + mq.getMqName() + ")",
                String.format("状态: %s, 积压: %d, 积压增长率: %.2f/s, 生产率: %.2f/s, 消费率: %.2f/s",
                    mq.getStatus(),
                    mq.getLag(),
                    mq.getLagIncreaseRate(),
                    mq.getProduceRate(),
                    mq.getConsumeRate()),
                relevance
            ));
        }

        for (JobMetrics job : jobMetricsList) {
            String relevance = job.getFailureCount() > 0 ? "中" : "低";
            evidences.add(new AnalysisResponse.Evidence(
                "JOB监控(" + job.getJobName() + ")",
                String.format("状态: %s, 执行次数: %d, 失败次数: %d, 成功率: %.2f%%",
                    job.getStatus(),
                    job.getExecutionCount(),
                    job.getFailureCount(),
                    job.getSuccessRate()),
                relevance
            ));
            if (job.getFailureCount() > 0) {
                logger.debug("中等相关性证据: JOB监控({}) - 存在失败记录", job.getJobName());
            }
        }

        List<String> disturbanceModules = getDisturbanceModules(alarmModules);
        List<String> selectedDisturbanceModules = selectDisturbanceModules(disturbanceModules);

        for (String module : selectedDisturbanceModules) {
            String errorMsg = generateDisturbanceError(module);
            evidences.add(new AnalysisResponse.Evidence(
                "模块错误(" + module + ")",
                errorMsg,
                "干扰项-不相关"
            ));
            logger.debug("干扰项证据: 模块错误({}) - 与告警模块不相关", module);
        }

        return evidences;
    }

    private List<String> selectDisturbanceModules(List<String> disturbanceModules) {
        int count = Math.min(random.nextInt(2) + 1, disturbanceModules.size());
        Collections.shuffle(disturbanceModules);
        return disturbanceModules.subList(0, count);
    }

    private String generateDisturbanceError(String module) {
        Map<String, String[]> errorMessages = new HashMap<>();
        errorMessages.put("下单", new String[]{
                "下单服务数据库连接超时",
                "下单库存不足错误"
        });
        errorMessages.put("改单", new String[]{
                "改单服务RPC调用失败",
                "改单订单状态不允许修改"
        });
        errorMessages.put("支付", new String[]{
                "支付渠道返回签名验证失败",
                "支付网关响应超时"
        });
        errorMessages.put("取消", new String[]{
                "取消订单业务规则校验失败",
                "取消订单涉及优惠不可取消"
        });
        errorMessages.put("出票", new String[]{
                "出票服务接口异常",
                "出票供应商返回数据格式错误"
        });
        errorMessages.put("退票", new String[]{
                "退票审核流程异常",
                "退票退款计算错误"
        });
        errorMessages.put("扣位", new String[]{
                "扣位服务锁座失败",
                "扣位资源分配冲突"
        });

        String[] messages = errorMessages.get(module);
        if (messages != null && messages.length > 0) {
            return messages[random.nextInt(messages.length)];
        }
        return module + "模块发生未知错误";
    }

    private AnalysisResponse.RootCause analyzeRootCause(String alarmDescription, RequestMetrics requestMetrics,
                                                       AppMetrics appMetrics, List<DeploymentRecord> deploymentRecords,
                                                       List<String> alarmModules, TopologyInfo topologyInfo,
                                                       List<MqMetrics> mqMetricsList, List<JobMetrics> jobMetricsList) {

        logger.debug("开始根因分析 - 评估多个假设...");

        List<MqMetrics> backlogMqs = findBacklogMq(mqMetricsList);
        boolean hasMqBacklog = !backlogMqs.isEmpty();
        boolean isAlarmModuleMqRelated = isMqRelatedToAlarmModule(topologyInfo, alarmModules, backlogMqs);

        logger.debug("假设1: MQ积压 - hasMqBacklog={}, isAlarmModuleMqRelated={}", hasMqBacklog, isAlarmModuleMqRelated);
        if (hasMqBacklog && isAlarmModuleMqRelated) {
            MqMetrics worstMq = backlogMqs.get(0);
            logger.info("假设1成立: MQ({})积压与告警模块相关", worstMq.getMqName());
            return new AnalysisResponse.RootCause(
                "MQ积压",
                String.format("MQ(%s)积压严重，积压量: %d, 积压增长率: %.2f/s，可能导致消息处理延迟，进而影响上游服务",
                    worstMq.getMqName(),
                    worstMq.getLag(),
                    worstMq.getLagIncreaseRate()),
                "90%"
            );
        }

        logger.debug("假设2: 高错误率 - 错误率={:.2f}%", requestMetrics.getErrorRate() * 100);
        if (requestMetrics.getErrorRate() > 0.1) {
            String moduleInfo = alarmModules.isEmpty() ? "" : "涉及模块: " + String.join(", ", alarmModules);
            logger.info("假设2成立: 错误率超过10%阈值");
            return new AnalysisResponse.RootCause(
                "高错误率",
                "当前错误率(" + String.format("%.2f%%", requestMetrics.getErrorRate() * 100) + ")超过10%阈值，可能存在服务异常或业务逻辑错误。" + moduleInfo,
                "85%"
            );
        }

        logger.debug("假设3: CPU过载 - CPU使用率={:.2f}%", appMetrics.getCpuUsage());
        if (appMetrics.getCpuUsage() > 80) {
            logger.info("假设3成立: CPU使用率超过80%");
            return new AnalysisResponse.RootCause(
                "CPU过载",
                "CPU使用率(" + String.format("%.2f%%", appMetrics.getCpuUsage()) + ")超过80%，可能存在性能瓶颈或死循环",
                "80%"
            );
        }

        logger.debug("假设4: 内存不足 - 内存使用率={:.2f}%", appMetrics.getMemoryUsage());
        if (appMetrics.getMemoryUsage() > 85) {
            logger.info("假设4成立: 内存使用率超过85%");
            return new AnalysisResponse.RootCause(
                "内存不足",
                "内存使用率(" + String.format("%.2f%%", appMetrics.getMemoryUsage()) + ")超过85%，可能存在内存泄漏或资源未释放",
                "75%"
            );
        }

        logger.debug("假设5: 发布问题 - 发布记录数={}", deploymentRecords.size());
        if (!deploymentRecords.isEmpty()) {
            logger.info("假设5成立: 存在最近发布记录");
            return new AnalysisResponse.RootCause(
                "发布问题",
                "告警时间与最近发布时间接近，可能是新版本引入的问题",
                "70%"
            );
        }

        logger.debug("假设6: 模块异常 - 告警模块数={}", alarmModules.size());
        if (!alarmModules.isEmpty()) {
            String moduleName = alarmModules.get(0);
            logger.info("假设6成立: 告警涉及具体模块({})", moduleName);
            return new AnalysisResponse.RootCause(
                moduleName + "模块异常",
                "告警描述涉及" + moduleName + "模块，该模块可能存在业务逻辑问题或外部依赖异常",
                "75%"
            );
        }

        logger.debug("假设7: 认证问题 - 告警描述包含登录/认证/token={}", 
            alarmDescription.contains("登录") || alarmDescription.contains("认证") || alarmDescription.contains("token"));
        if (alarmDescription.contains("登录") || alarmDescription.contains("认证") || alarmDescription.contains("token")) {
            logger.info("假设7成立: 告警涉及认证相关问题");
            return new AnalysisResponse.RootCause(
                "认证问题",
                "告警描述涉及登录或认证功能，可能存在认证服务异常或token失效问题",
                "65%"
            );
        }

        logger.debug("假设8: MQ积压(非直接原因) - hasMqBacklog={}", hasMqBacklog);
        if (hasMqBacklog) {
            MqMetrics mq = backlogMqs.get(0);
            logger.info("假设8成立: MQ积压但与告警模块无关");
            return new AnalysisResponse.RootCause(
                "MQ积压(非直接原因)",
                String.format("检测到MQ(%s)存在积压，但与告警模块无直接关联，可能是干扰因素", mq.getMqName()),
                "40%"
            );
        }

        logger.info("所有假设均不成立，返回未知原因");
        return new AnalysisResponse.RootCause(
            "未知原因",
            "根据现有数据无法确定具体根因，建议进一步查看详细日志",
            "40%"
        );
    }

    private List<MqMetrics> findBacklogMq(List<MqMetrics> mqMetricsList) {
        List<MqMetrics> backlogMqs = new ArrayList<>();
        for (MqMetrics mq : mqMetricsList) {
            if (mq.isBacklog()) {
                backlogMqs.add(mq);
            }
        }
        backlogMqs.sort((a, b) -> Long.compare(b.getLag(), a.getLag()));
        return backlogMqs;
    }

    private boolean isMqRelatedToAlarmModule(TopologyInfo topologyInfo, List<String> alarmModules, List<MqMetrics> backlogMqs) {
        if (alarmModules.isEmpty() || topologyInfo.getModules() == null) {
            return false;
        }

        Set<String> alarmMqNames = new HashSet<>();
        for (MqMetrics mq : backlogMqs) {
            alarmMqNames.add(mq.getMqName());
        }

        for (String moduleName : alarmModules) {
            for (TopologyInfo.ModuleDependency module : topologyInfo.getModules()) {
                if (module.getModuleName().equals(moduleName) && module.getMqDependencies() != null) {
                    for (TopologyInfo.DependencyItem mq : module.getMqDependencies()) {
                        if (alarmMqNames.contains(mq.getName())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private List<String> generateSuggestions(AnalysisResponse.RootCause rootCause, List<AnalysisResponse.Evidence> evidences,
                                           List<String> alarmModules, TopologyInfo topologyInfo, List<MqMetrics> mqMetricsList) {
        List<String> suggestions = new ArrayList<>();
        String rootCauseType = rootCause.getType();

        if (rootCauseType.contains("MQ积压")) {
            suggestions.add("检查MQ消费者的处理能力是否不足");
            suggestions.add("排查消费者是否存在异常导致处理缓慢");
            suggestions.add("考虑增加消费者实例或优化消费逻辑");
            suggestions.add("检查MQ broker是否存在性能问题");
        } else if (rootCauseType.contains("下单")) {
            suggestions.add("检查下单模块数据库连接池状态");
            suggestions.add("验证下单接口的入参校验逻辑");
            suggestions.add("查看下单服务的上下游依赖是否正常");
        } else if (rootCauseType.contains("改单")) {
            suggestions.add("检查改单服务的RPC调用链");
            suggestions.add("验证订单状态机流转是否正常");
        } else if (rootCauseType.contains("支付")) {
            suggestions.add("检查支付渠道的签名配置是否正确");
            suggestions.add("验证支付网关的连通性");
        } else if (rootCauseType.contains("取消")) {
            suggestions.add("检查取消订单的业务规则配置");
            suggestions.add("验证取消流程涉及的优惠计算");
        } else if (rootCauseType.contains("出票")) {
            suggestions.add("检查出票供应商接口响应情况");
            suggestions.add("验证出票服务的异常处理逻辑");
        } else if (rootCauseType.contains("退票")) {
            suggestions.add("检查退票审核流程是否畅通");
            suggestions.add("验证退款金额计算逻辑");
        } else if (rootCauseType.contains("扣位")) {
            suggestions.add("检查扣位服务的分布式锁配置");
            suggestions.add("验证座位资源的分配策略");
        } else {
            switch (rootCauseType) {
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
        }

        if (!alarmModules.isEmpty()) {
            List<String> disturbanceModules = getDisturbanceModules(alarmModules);
            if (!disturbanceModules.isEmpty()) {
                suggestions.add("注意：以下模块的错误为干扰项，与本次告警无关：" + String.join("、", disturbanceModules));
            }
        }

        suggestions.add("查看完整监控面板获取更多上下文");
        return suggestions;
    }
}