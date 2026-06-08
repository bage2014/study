package com.bage.study.ai.best.practice.exception.analysis.mcp;

import com.bage.study.ai.best.practice.exception.analysis.context.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TopologyMcpService {

    private final Random random = new Random();

    private static final Map<String, ModuleTopologyConfig> MODULE_CONFIGS = new HashMap<>();

    static {
        MODULE_CONFIGS.put("下单", new ModuleTopologyConfig("下单模块",
            Arrays.asList(
                new DependencyConfig("order-topic", "MQ", "RocketMQ", "消息队列"),
                new DependencyConfig("payment-topic", "MQ", "RabbitMQ", "支付回调"),
                new DependencyConfig("order-db", "DB", "MySQL", "订单库"),
                new DependencyConfig("库存服务", "外部服务", "inventory-service", "库存扣减")
            ),
            Arrays.asList(
                new DependencyConfig("订单同步JOB", "JOB", "0 0 2 * * ?", "日结")
            )
        ));

        MODULE_CONFIGS.put("改单", new ModuleTopologyConfig("改单模块",
            Arrays.asList(
                new DependencyConfig("order-modify-topic", "MQ", "RocketMQ", "改单消息"),
                new DependencyConfig("order-db", "DB", "MySQL", "订单库")
            ),
            Arrays.asList(
                new DependencyConfig("订单状态检查JOB", "JOB", "0/30 * * * ?", "实时")
            )
        ));

        MODULE_CONFIGS.put("支付", new ModuleTopologyConfig("支付模块",
            Arrays.asList(
                new DependencyConfig("payment-callback-topic", "MQ", "Kafka", "支付回调"),
                new DependencyConfig("payment-gateway", "外部服务", "alipay/wechatpay", "支付网关"),
                new DependencyConfig("payment-db", "DB", "MySQL", "支付库")
            ),
            Arrays.asList(
                new DependencyConfig("对账JOB", "JOB", "0 0 1 * * ?", "日结")
            )
        ));

        MODULE_CONFIGS.put("取消", new ModuleTopologyConfig("取消模块",
            Arrays.asList(
                new DependencyConfig("order-cancel-topic", "MQ", "RocketMQ", "取消消息"),
                new DependencyConfig("order-db", "DB", "MySQL", "订单库"),
                new DependencyConfig("优惠服务", "外部服务", "coupon-service", "优惠回滚")
            ),
            Arrays.asList()
        ));

        MODULE_CONFIGS.put("出票", new ModuleTopologyConfig("出票模块",
            Arrays.asList(
                new DependencyConfig("ticket-issue-topic", "MQ", "RocketMQ", "出票消息"),
                new DependencyConfig("ticket-supplier-api", "外部服务", "supplier-api", "供应商接口"),
                new DependencyConfig("ticket-db", "DB", "MySQL", "票务库")
            ),
            Arrays.asList(
                new DependencyConfig("出票重试JOB", "JOB", "0/10 * * * ?", "实时重试")
            )
        ));

        MODULE_CONFIGS.put("退票", new ModuleTopologyConfig("退票模块",
            Arrays.asList(
                new DependencyConfig("refund-topic", "MQ", "Kafka", "退款消息"),
                new DependencyConfig("order-db", "DB", "MySQL", "订单库"),
                new DependencyConfig("票务供应商", "外部服务", "supplier-api", "退票接口")
            ),
            Arrays.asList(
                new DependencyConfig("退票审核JOB", "JOB", "0 0 * * ?", "每小时")
            )
        ));

        MODULE_CONFIGS.put("扣位", new ModuleTopologyConfig("扣位模块",
            Arrays.asList(
                new DependencyConfig("seat-lock-topic", "MQ", "RocketMQ", "座位锁定"),
                new DependencyConfig("seat-db", "DB", "Redis", "座位缓存"),
                new DependencyConfig("seat-db-backup", "DB", "MySQL", "座位库")
            ),
            Arrays.asList()
        ));
    }

    public TopologyInfo getTopology(String appId) {
        return buildTopologyInfo(appId);
    }

    public TopologyInfo getModuleTopology(String appId, String moduleName) {
        return buildModuleTopologyInfo(appId, moduleName);
    }

    public List<MqMetrics> getMqMetrics(String appId, List<String> mqNames) {
        List<MqMetrics> metrics = new ArrayList<>();
        for (String mqName : mqNames) {
            MqMetrics mqMetric = generateMqMetrics(mqName);
            metrics.add(mqMetric);
        }
        return metrics;
    }

    public List<JobMetrics> getJobMetrics(String appId, List<String> jobNames) {
        List<JobMetrics> metrics = new ArrayList<>();
        for (String jobName : jobNames) {
            JobMetrics jobMetric = generateJobMetrics(jobName);
            metrics.add(jobMetric);
        }
        return metrics;
    }

    public MqMetrics getMqBacklogStatus(String mqName) {
        return generateMqMetrics(mqName);
    }

    private TopologyInfo buildTopologyInfo(String appId) {
        TopologyInfo info = new TopologyInfo();
        info.setAppId(appId);
        info.setAppName("订单中心-" + appId);
        info.setAppType("核心业务服务");

        List<TopologyInfo.ModuleDependency> modules = new ArrayList<>();
        for (String moduleName : MODULE_CONFIGS.keySet()) {
            modules.add(buildModuleDependency(moduleName));
        }
        info.setModules(modules);

        List<TopologyInfo.ApplicationDependency> downstream = Arrays.asList(
            new TopologyInfo.ApplicationDependency("app-order-query", "订单查询服务", "下游", "提供订单查询接口"),
            new TopologyInfo.ApplicationDependency("app-notification", "通知服务", "下游", "发送订单通知")
        );
        info.setDownstreamApps(downstream);

        List<TopologyInfo.ApplicationDependency> upstream = Arrays.asList(
            new TopologyInfo.ApplicationDependency("app-user-center", "用户中心", "上游", "提供用户信息"),
            new TopologyInfo.ApplicationDependency("app-product", "商品服务", "上游", "提供商品信息")
        );
        info.setUpstreamApps(upstream);

        return info;
    }

    private TopologyInfo buildModuleTopologyInfo(String appId, String moduleName) {
        TopologyInfo info = new TopologyInfo();
        info.setAppId(appId);
        info.setAppName("订单中心-" + appId);
        info.setAppType("核心业务服务");

        List<TopologyInfo.ModuleDependency> modules = new ArrayList<>();
        modules.add(buildModuleDependency(moduleName));
        info.setModules(modules);

        info.setDownstreamApps(new ArrayList<>());
        info.setUpstreamApps(new ArrayList<>());

        return info;
    }

    private TopologyInfo.ModuleDependency buildModuleDependency(String moduleName) {
        ModuleTopologyConfig config = MODULE_CONFIGS.getOrDefault(moduleName,
            new ModuleTopologyConfig(moduleName, new ArrayList<>(), new ArrayList<>()));

        TopologyInfo.ModuleDependency module = new TopologyInfo.ModuleDependency(moduleName, "业务模块");

        List<TopologyInfo.DependencyItem> mqs = new ArrayList<>();
        for (DependencyConfig dep : config.mqDependencies) {
            mqs.add(new TopologyInfo.DependencyItem(dep.name, dep.type, "NORMAL", "正常"));
        }
        module.setMqDependencies(mqs);

        List<TopologyInfo.DependencyItem> jobs = new ArrayList<>();
        for (DependencyConfig dep : config.jobDependencies) {
            jobs.add(new TopologyInfo.DependencyItem(dep.name, "JOB", "NORMAL", "正常"));
        }
        module.setJobDependencies(jobs);

        List<TopologyInfo.DependencyItem> dbs = new ArrayList<>();
        for (DependencyConfig dep : config.dbDependencies) {
            dbs.add(new TopologyInfo.DependencyItem(dep.name, "DB", "NORMAL", "正常"));
        }
        module.setDbDependencies(dbs);

        List<TopologyInfo.DependencyItem> externals = new ArrayList<>();
        for (DependencyConfig dep : config.externalDependencies) {
            externals.add(new TopologyInfo.DependencyItem(dep.name, "外部服务", "NORMAL", "正常"));
        }
        module.setExternalServiceDependencies(externals);

        return module;
    }

    public MqMetrics generateMqMetrics(String mqName, boolean forceBacklog) {
        boolean hasBacklog = forceBacklog || random.nextDouble() > 0.5;
        long lag = hasBacklog ? random.nextInt(50000) + 10000 : random.nextInt(5000);
        double lagIncreaseRate = hasBacklog ? random.nextDouble() * 10 + 5 : random.nextDouble() * 2;

        return new MqMetrics(
            mqName,
            mqName + "-topic",
            mqName + "-consumer-group",
            random.nextInt(100000) + 10000,
            random.nextInt(5) + 2,
            random.nextDouble() * 1000 + 100,
            hasBacklog ? random.nextDouble() * 200 : random.nextDouble() * 900 + 100,
            lag,
            lagIncreaseRate,
            hasBacklog ? "WARNING" : "HEALTHY"
        );
    }

    private MqMetrics generateMqMetrics(String mqName) {
        return generateMqMetrics(mqName, false);
    }

    public JobMetrics generateJobMetrics(String jobName) {
        JobMetrics job = new JobMetrics(jobName, "default-group", "CRON", "NORMAL");
        job.setDuration(random.nextInt(60000) + 1000);
        job.setExecutionCount(random.nextInt(1000) + 100);
        job.setFailureCount(random.nextInt(10));
        job.setSuccessRate(100 - job.getFailureCount() * 100.0 / job.getExecutionCount());
        return job;
    }

    private static class ModuleTopologyConfig {
        String displayName;
        List<DependencyConfig> mqDependencies;
        List<DependencyConfig> jobDependencies;
        List<DependencyConfig> dbDependencies;
        List<DependencyConfig> externalDependencies;

        ModuleTopologyConfig(String displayName,
                           List<DependencyConfig> mqDependencies,
                           List<DependencyConfig> jobDependencies) {
            this.displayName = displayName;
            this.mqDependencies = mqDependencies;
            this.jobDependencies = jobDependencies;
            this.dbDependencies = new ArrayList<>();
            this.externalDependencies = new ArrayList<>();

            for (DependencyConfig dep : mqDependencies) {
                if ("DB".equals(dep.type)) {
                    this.dbDependencies.add(dep);
                } else if ("外部服务".equals(dep.type)) {
                    this.externalDependencies.add(dep);
                }
            }
        }
    }

    private static class DependencyConfig {
        String name;
        String type;
        String provider;
        String description;

        DependencyConfig(String name, String type, String provider, String description) {
            this.name = name;
            this.type = type;
            this.provider = provider;
            this.description = description;
        }
    }
}