package com.bage.study.ai.best.practice.rca.agent.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 工具目录：集中声明全部 Mock 工具。
 * 这是工具名的唯一事实来源 —— Playbook 映射、LLM 工具目录都只能引用此处注册名。
 * 覆盖 rca.md 的 6 类数据域：应用层 / 数据库层 / 缓存层 / 消息队列层 / 基础设施层 / 外部依赖层。
 */
public final class MockToolCatalog {

    public static final String SCENE_DB = "db_slow_query";
    public static final String SCENE_MQ = "mq_lag";
    public static final String SCENE_APP = "app_resource";
    public static final String SCENE_SUPPLIER = "supplier_failure";
    public static final String SCENE_ERROR = "high_error";
    public static final String SCENE_REDIS = "redis";

    private static final Set<String> DB_SCENES = Set.of(SCENE_DB);
    private static final Set<String> MQ_SCENES = Set.of(SCENE_MQ);
    private static final Set<String> APP_SCENES = Set.of(SCENE_APP);
    private static final Set<String> SUPPLIER_SCENES = Set.of(SCENE_SUPPLIER);
    private static final Set<String> ERROR_SCENES = Set.of(SCENE_ERROR);
    private static final Set<String> REDIS_SCENES = Set.of(SCENE_REDIS);
    private static final Set<String> NONE = Set.of();

    private MockToolCatalog() {
    }

    public static List<McpTool> allTools() {
        List<McpTool> tools = new ArrayList<>();

        // ========== 应用层 ==========
        tools.add(metric("app_error_rate", "APP", "应用错误率(%)，5xx+业务异常占比",
                "%", 0.2, 1.0, true, 12.5, ERROR_SCENES,
                "错误率处于基线水平", "错误率显著飙升，大量请求失败"));
        tools.add(metric("app_p99_latency", "APP", "应用 P99 响应延迟(ms)",
                "ms", 80, 500, true, 2400, Set.of(SCENE_DB, SCENE_REDIS, SCENE_SUPPLIER),
                "P99 延迟平稳", "P99 延迟超过基线 20 倍"));
        tools.add(metric("app_gc_log", "APP", "GC 次数/分钟及 Full GC 统计",
                "count/min", 2, 10, true, 28, APP_SCENES,
                "GC 频率正常", "Full GC 频繁，停顿明显"));
        tools.add(metric("app_thread_pool", "APP", "业务线程池使用率(%)",
                "%", 45, 85, true, 99, APP_SCENES,
                "线程池水位正常", "线程池接近打满，任务排队"));
        tools.add(metric("app_heap_usage", "APP", "堆内存使用率(%)",
                "%", 55, 85, true, 93, APP_SCENES,
                "堆水位正常", "堆使用率接近上限，疑似泄漏"));
        tools.add(metric("app_error_log", "APP", "错误日志条数/分钟",
                "count/min", 1, 10, true, 220, ERROR_SCENES,
                "错误日志零星", "同类异常堆栈成批出现"));
        tools.add(metric("trace_p99", "APP", "分布式追踪入口 span P99 耗时(ms)",
                "ms", 90, 500, true, 2600, Set.of(SCENE_DB, SCENE_REDIS, SCENE_SUPPLIER),
                "追踪链路耗时正常", "追踪显示耗时集中在下游调用"));
        tools.add(metric("trace_span_detail", "APP", "最慢 span 明细耗时(ms)",
                "ms", 40, 500, true, 2500, Set.of(SCENE_DB, SCENE_REDIS, SCENE_SUPPLIER),
                "span 耗时分布均匀", "单个下游 span 贡献 90% 以上耗时"));
        tools.add(metric("log_error_pattern", "APP", "日志异常模式聚类命中数/分钟",
                "count/min", 0, 5, true, 180, Set.of(SCENE_ERROR, SCENE_APP),
                "无聚簇异常模式", "单一异常模式高频复现"));

        // ========== 数据库层 ==========
        tools.add(metric("db_slow_query", "DB", "慢查询条数/分钟",
                "count/min", 1, 5, true, 86, DB_SCENES,
                "慢查询偶发", "慢查询激增，出现全表扫描 SQL"));
        tools.add(metric("db_connections", "DB", "连接池使用率(%)",
                "%", 40, 80, true, 92, DB_SCENES,
                "连接池水位正常", "连接被长事务占满"));
        tools.add(metric("db_lock_waits", "DB", "锁等待次数/分钟",
                "count/min", 0, 3, true, 34, DB_SCENES,
                "无明显锁等待", "行锁/表锁等待排队严重"));
        tools.add(metric("db_cpu", "DB", "数据库 CPU 使用率(%)",
                "%", 35, 80, true, 94, DB_SCENES,
                "DB CPU 正常", "DB CPU 被慢查询打满"));
        tools.add(metric("db_top_sql", "DB", "Top SQL 最大耗时(ms)",
                "ms", 12, 1000, true, 8200, DB_SCENES,
                "Top SQL 耗时正常", "Top SQL 单条耗时 8s+"));

        // ========== 缓存层 ==========
        tools.add(metric("redis_hit_rate", "REDIS", "Redis 命中率(%)",
                "%", 98.5, 95, false, 61, REDIS_SCENES,
                "命中率高于阈值", "命中率跌破 95%，请求穿透到 DB"));
        tools.add(metric("redis_eviction", "REDIS", "Redis key 驱逐数/分钟",
                "keys/min", 0, 10, true, 420, REDIS_SCENES,
                "无 key 驱逐", "内存紧张导致大量 key 被驱逐"));
        tools.add(metric("redis_memory", "REDIS", "Redis 内存使用率(%)",
                "%", 62, 85, true, 96, REDIS_SCENES,
                "内存水位正常", "内存超过 maxmemory 水位"));
        tools.add(metric("redis_latency", "REDIS", "Redis 命令平均延迟(ms)",
                "ms", 0.8, 5, true, 42, REDIS_SCENES,
                "Redis 延迟亚毫秒级", "Redis 延迟放大 50 倍"));

        // ========== 消息队列层 ==========
        tools.add(metric("mq_consumer_lag", "MQ", "消费组堆积量(lag)",
                "count", 120, 10000, true, 186000, MQ_SCENES,
                "lag 在正常范围", "消费严重滞后，lag 持续增长"));
        tools.add(metric("mq_produce_rate", "MQ", "生产速率(msg/s)",
                "msg/s", 1200, 5000, true, 1200, NONE,
                "生产速率平稳", "生产速率突增"));
        tools.add(metric("mq_consume_rate", "MQ", "消费速率(msg/s)",
                "msg/s", 1180, 200, false, 60, MQ_SCENES,
                "消费速率与生产基本持平", "消费速率骤降，远低于生产"));
        tools.add(metric("mq_dead_letter", "MQ", "死信队列消息数/分钟",
                "count/min", 0, 10, true, 340, MQ_SCENES,
                "死信队列为空", "消费失败消息大量进入死信队列"));
        tools.add(metric("mq_consume_status", "MQ", "消费组状态(0=正常,1=异常)",
                "status", 0, 0.5, true, 1, MQ_SCENES,
                "消费组在线且稳定", "消费组出现 rebalance/消费者掉线"));
        tools.add(metric("consumer_group_detail", "MQ", "消费组明细：单分区最大 lag",
                "count", 120, 10000, true, 186000, MQ_SCENES,
                "各分区 lag 均衡", "单个分区 lag 倾斜严重"));

        // ========== 外部依赖层 ==========
        tools.add(metric("http_success_rate", "SUPPLIER", "下游 HTTP 成功率(%)",
                "%", 99.6, 99, false, 71, SUPPLIER_SCENES,
                "下游成功率正常", "下游成功率跌破 99%"));
        tools.add(metric("http_p99_latency", "SUPPLIER", "下游 HTTP P99 延迟(ms)",
                "ms", 120, 800, true, 6100, SUPPLIER_SCENES,
                "下游延迟正常", "下游响应超时频发"));
        tools.add(metric("circuit_breaker_status", "SUPPLIER", "熔断器状态(0=闭合,1=打开)",
                "status", 0, 0.5, true, 1, SUPPLIER_SCENES,
                "熔断器闭合", "熔断器已打开，调用被快速失败"));
        tools.add(metric("dns_resolve", "SUPPLIER", "DNS 解析耗时(ms)",
                "ms", 15, 100, true, 15, NONE,
                "DNS 解析正常", "DNS 解析超时"));
        tools.add(metric("downstream_success_rate", "SUPPLIER", "下游服务业务成功率(%)",
                "%", 99.4, 99, false, 70, SUPPLIER_SCENES,
                "下游业务成功率正常", "下游业务大量返回失败"));
        tools.add(metric("http_error_breakdown", "SUPPLIER", "下游 5xx 占比(%)",
                "%", 0.1, 5, true, 26, SUPPLIER_SCENES,
                "5xx 占比极低", "下游 5xx 集中爆发"));

        // ========== 基础设施层（默认全部正常，用于干扰项裁剪演示） ==========
        tools.add(metric("infra_cpu", "INFRA", "主机 CPU 使用率(%)",
                "%", 38, 80, true, 38, NONE,
                "主机 CPU 正常", "主机 CPU 饱和"));
        tools.add(metric("infra_memory", "INFRA", "主机内存使用率(%)",
                "%", 52, 85, true, 52, NONE,
                "主机内存正常", "主机内存不足"));
        tools.add(metric("infra_disk_io", "INFRA", "磁盘 IO 使用率(%)",
                "%", 25, 85, true, 25, NONE,
                "磁盘 IO 正常", "磁盘 IO 打满"));
        tools.add(metric("container_restart", "INFRA", "容器重启次数/小时",
                "count/h", 0, 1, true, 0, NONE,
                "容器无重启", "容器频繁重启"));

        tools.add(new ChangeEventTool());
        return tools;
    }

    private static MockMcpTool metric(String name, String layer, String description,
                                     String unit, double normal, double threshold,
                                     boolean higherIsBad, double abnormal, Set<String> scenes,
                                     String normalDetail, String abnormalDetail) {
        return new MockMcpTool(new MockMcpTool.Spec(
                name, layer, description, unit, normal, threshold,
                higherIsBad, abnormal, scenes, normalDetail, abnormalDetail));
    }
}
