package com.bage.study.ai.best.practice.rca.agent.planner;

import com.bage.study.ai.best.practice.rca.agent.dto.RcaRequest;
import com.bage.study.ai.best.practice.rca.agent.model.Hypothesis;
import com.bage.study.ai.best.practice.rca.agent.model.SymptomPattern;
import com.bage.study.ai.best.practice.rca.agent.model.TargetType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 确定性规则 Plan：LLM 不可用时的兜底路径，保证全流程可运行、可复现、可单测。
 * 从告警文本关键字分类出主假设，再补齐竞争性假设（多假设并行，避免单链锚定）。
 */
@Component
public class RuleBasedPlanner {

    public PlanResult plan(RcaRequest request) {
        String text = request.getAlarmDescription() == null ? "" : request.getAlarmDescription().toLowerCase();
        String appId = request.getAppId();

        Hypothesis primary = classifyPrimary(text, appId);
        List<Hypothesis> hypotheses = new ArrayList<>();
        hypotheses.add(primary);

        // 补齐竞争假设：与主假设 type/symptom 不同的常见候选
        List<Hypothesis> candidates = defaultCandidates(appId);
        double[] priors = {0.3, 0.2, 0.1};
        int idx = 0;
        for (Hypothesis c : candidates) {
            if (hypotheses.size() >= 4) {
                break;
            }
            if (c.getTargetType() == primary.getTargetType() && c.getSymptom() == primary.getSymptom()) {
                continue;
            }
            c = new Hypothesis("h" + (hypotheses.size() + 1), c.getDesc(),
                    c.getTargetType(), c.getTargetName(), c.getSymptom(), priors[Math.min(idx, 2)]);
            hypotheses.add(c);
            idx++;
        }
        // 重新编号 id，保证 h1..hn 稳定
        for (int i = 0; i < hypotheses.size(); i++) {
            Hypothesis old = hypotheses.get(i);
            hypotheses.set(i, new Hypothesis("h" + (i + 1), old.getDesc(),
                    old.getTargetType(), old.getTargetName(), old.getSymptom(),
                    old.getPriorConfidence()));
        }
        return new PlanResult(hypotheses, false, "规则 Plan：关键字分类 + 竞争假设补齐");
    }

    private Hypothesis classifyPrimary(String text, String appId) {
        TargetType type;
        SymptomPattern symptom;
        String desc;

        if (containsAny(text, "堆积", "积压", "消费", "lag", "kafka", "rocketmq", "mq", "死信")) {
            type = TargetType.MQ;
            symptom = SymptomPattern.CONSUMER_LAG;
            desc = "MQ 消费堆积导致消息处理延迟，业务履约滞后";
        } else if (containsAny(text, "redis", "缓存", "命中率", "热点key", "大key")) {
            type = TargetType.REDIS;
            if (containsAny(text, "内存", "驱逐", "evict", "资源")) {
                symptom = SymptomPattern.RESOURCE_EXHAUSTED;
                desc = "Redis 内存打满触发驱逐，缓存容量不足";
            } else {
                symptom = SymptomPattern.HIGH_LATENCY;
                desc = "Redis 延迟升高/命中率下降，拖慢接口";
            }
        } else if (containsAny(text, "第三方", "供应商", "下游", "外部接口", "熔断", "supplier", "依赖不可用", "调用外部")) {
            type = TargetType.SUPPLIER;
            symptom = SymptomPattern.DEPENDENCY_FAILURE;
            desc = "外部供应商依赖故障，熔断/超时传导到本服务";
        } else if (containsAny(text, "数据库", "db", "sql", "慢查询", "锁等待", "事务")) {
            type = TargetType.DB;
            symptom = SymptomPattern.HIGH_LATENCY;
            desc = "数据库慢查询/锁等待导致接口超时";
        } else if (containsAny(text, "线程池", "thread", "gc", "full gc", "内存", "oom", "堆", "资源耗尽")) {
            type = TargetType.APP;
            symptom = SymptomPattern.RESOURCE_EXHAUSTED;
            desc = "应用资源耗尽（线程池/GC/内存）导致处理能力下降";
        } else if (containsAny(text, "cpu", "磁盘", "容器重启", "主机", "基础设施", "pod")) {
            type = TargetType.INFRA;
            symptom = SymptomPattern.RESOURCE_EXHAUSTED;
            desc = "基础设施资源瓶颈或容器异常导致服务受损";
        } else if (containsAny(text, "错误率", "报错", "失败", "error", "5xx", "异常", "空指针", "npe")) {
            type = TargetType.APP;
            symptom = SymptomPattern.HIGH_ERROR_RATE;
            desc = "应用错误率飙升，存在代码逻辑或依赖异常";
        } else {
            // 超时/延迟/慢/p99/timeout 以及兜底
            type = TargetType.APP;
            symptom = SymptomPattern.HIGH_LATENCY;
            desc = "应用接口延迟升高，需定位慢点（DB/缓存/下游）";
        }
        return new Hypothesis("h1", desc, type, targetName(type, appId), symptom, 0.6);
    }

    private List<Hypothesis> defaultCandidates(String appId) {
        List<Hypothesis> list = new ArrayList<>();
        list.add(new Hypothesis("", "最近发布/变更引入缺陷导致错误率升高",
                TargetType.APP, targetName(TargetType.APP, appId), SymptomPattern.HIGH_ERROR_RATE, 0));
        list.add(new Hypothesis("", "数据库慢查询导致请求超时",
                TargetType.DB, targetName(TargetType.DB, appId), SymptomPattern.HIGH_LATENCY, 0));
        list.add(new Hypothesis("", "MQ 消费堆积导致异步链路延迟",
                TargetType.MQ, targetName(TargetType.MQ, appId), SymptomPattern.CONSUMER_LAG, 0));
        list.add(new Hypothesis("", "外部供应商故障导致调用失败",
                TargetType.SUPPLIER, targetName(TargetType.SUPPLIER, appId), SymptomPattern.DEPENDENCY_FAILURE, 0));
        return list;
    }

    public static String targetName(TargetType type, String appId) {
        return switch (type) {
            case APP -> appId;
            case DB -> appId + "-db";
            case REDIS -> appId + "-redis";
            case MQ -> appId + "-mq";
            case SUPPLIER -> "supplier-api";
            case INFRA -> appId + "-host";
        };
    }

    private static boolean containsAny(String text, String... keys) {
        for (String k : keys) {
            if (text.contains(k)) {
                return true;
            }
        }
        return false;
    }
}
