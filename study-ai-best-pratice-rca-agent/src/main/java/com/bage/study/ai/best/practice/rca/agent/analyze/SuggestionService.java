package com.bage.study.ai.best.practice.rca.agent.analyze;

import com.bage.study.ai.best.practice.rca.agent.model.Hypothesis;
import com.bage.study.ai.best.practice.rca.agent.model.Suggestions;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 建议生成：按 Google MTTM 思路分三层 —— 先止血(Mitigation)，再根治(Remediation)，后预防(Prevention)。
 */
@Service
public class SuggestionService {

    public Suggestions forHypothesis(Hypothesis h) {
        if (h == null) {
            return inconclusive();
        }
        return switch (h.getTargetType()) {
            case DB -> new Suggestions(
                    List.of("对慢 SQL 执行限流/kill，优先恢复接口可用性（MTTM）",
                            "必要时回滚近期 Schema 变更"),
                    List.of("针对 Top SQL 补充索引或改写，消除全表扫描",
                            "拆分大事务，缩短锁持有时间",
                            "核查 DB 连接池配置，避免连接耗尽"),
                    List.of("上线前 SQL 审核与执行计划检查",
                            "慢查询与锁等待告警接入发布卡点"));
            case MQ -> new Suggestions(
                    List.of("临时扩容消费组实例/分区，加快追平 lag",
                            "死信消息先旁路隔离，避免阻塞主链路"),
                    List.of("修复消费者慢逻辑（同步写 DB/外部调用异步化）",
                            "消费者限流与重试退避改造，避免重入死信"),
                    List.of("lag 增长率与死信量告警",
                            "消费能力容量评估纳入发布检查项"));
            case REDIS -> new Suggestions(
                    List.of("临时扩容 Redis 内存或调整淘汰策略止血",
                            "对热点 key 请求做本地缓存/限流"),
                    List.of("治理大 key/热点 key，拆分缓存结构",
                            "核查 maxmemory 与淘汰策略配置变更"),
                    List.of("缓存命中率/水位告警",
                            "缓存配置变更纳入评审与灰度"));
            case SUPPLIER -> new Suggestions(
                    List.of("对故障供应商执行熔断/降级，启用兜底缓存",
                            "流量切流到备用供应商（如有）"),
                    List.of("与供应商确认故障并推动恢复",
                            "优化超时/重试/舱壁配置，防止级联耗尽线程资源"),
                    List.of("多供应商/多活容灾建设",
                            "供应商 SLA 与熔断指标常态化看板"));
            case INFRA -> new Suggestions(
                    List.of("故障节点驱逐/重启，Pod 漂移到健康宿主",
                            "临时扩容节点池"),
                    List.of("排查宿主资源争抢与磁盘/网络瓶颈"),
                    List.of("资源水位与容器重启告警",
                            "关键服务资源 request/limit 与压测基线复核"));
            case APP -> appSuggestions(h);
        };
    }

    private Suggestions appSuggestions(Hypothesis h) {
        return switch (h.getSymptom()) {
            case RESOURCE_EXHAUSTED -> new Suggestions(
                    List.of("重启异常实例并扩容，先恢复容量",
                            "摘掉问题节点流量，避免雪崩"),
                    List.of("抓取堆 dump/线程 dump 定位内存泄漏与阻塞点",
                            "线程池/连接池参数调优，限制无界队列"),
                    List.of("发布前压测校验内存/线程基线",
                            "GC 与池水位告警接入发布卡点"));
            case HIGH_ERROR_RATE -> new Suggestions(
                    List.of("回滚最近一次发布（变更在故障时间窗内）",
                            "对报错接口限流/降级"),
                    List.of("按错误日志聚类定位异常栈并修复",
                            "修复后小流量灰度验证"),
                    List.of("关键参数校验补充单测",
                            "错误率突增自动关联发布单"));
            default -> new Suggestions(
                    List.of("对慢接口限流，优先保证核心链路",
                            "回滚时间窗内最近发布"),
                    List.of("沿 trace 最慢 span 定位下游慢点（DB/Redis/供应商）",
                            "优化热点路径与缓存策略"),
                    List.of("P99 延迟分维度告警（入口/下游）",
                            "关键接口容量基线常态化"));
        };
    }

    public Suggestions inconclusive() {
        return new Suggestions(
                List.of("值班 SRE 立即介入，按 TSG 手册止血",
                        "拉取近 30 分钟 trace 与错误日志扩大信息面"),
                List.of("补充日志/Trace/Span 明细数据后重新执行 RCA"),
                List.of("沉淀本次故障的 SOP 与异常信号阈值",
                        "完善拓扑与变更数据接入"));
    }
}
