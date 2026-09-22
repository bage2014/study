package com.bage.study.ai.best.practice.rca.agent.tool;

import com.bage.study.ai.best.practice.rca.agent.model.TimelineEvent;

import java.util.ArrayList;
import java.util.List;

import static com.bage.study.ai.best.practice.rca.agent.tool.MockToolCatalog.*;

/**
 * 变更事件工具：发布记录 / 配置变更 / Schema 变更 / 限流阈值调整。
 * 变更往往是根因直接触发器，时序上位于故障前几分钟（rca.md 变更感知）。
 */
public class ChangeEventTool implements McpTool {

    public static final String NAME = "query_change_events";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String description() {
        return "查询告警时间窗前 60 分钟内的变更事件：发布/配置/Schema/限流调整";
    }

    @Override
    public String layer() {
        return "CHANGE";
    }

    @Override
    public ToolResult execute(ToolQuery query) {
        List<TimelineEvent> events = new ArrayList<>();
        if (query.alarmTime() == null) {
            return ToolResult.changes(NAME, events);
        }
        String appId = query.appId();
        switch (query.scene() == null ? "default" : query.scene()) {
            case SCENE_DB -> events.add(new TimelineEvent(
                    query.alarmTime().minusMinutes(8), TimelineEvent.Kind.CHANGE,
                    NAME, appId + "-db",
                    "DB Schema 变更：给 orders 表新增索引失败，大事务持锁 8 分钟"));
            case SCENE_MQ -> events.add(new TimelineEvent(
                    query.alarmTime().minusMinutes(5), TimelineEvent.Kind.CHANGE,
                    NAME, appId + "-mq",
                    "MQ 消费者发布 v2.3.1：消费逻辑新增 DB 同步写，消费吞吐下降"));
            case SCENE_APP -> events.add(new TimelineEvent(
                    query.alarmTime().minusMinutes(3), TimelineEvent.Kind.CHANGE,
                    NAME, appId,
                    "应用发布 v3.1.0：引入本地缓存未设上限，堆内存持续上涨"));
            case SCENE_ERROR -> {
                events.add(new TimelineEvent(
                        query.alarmTime().minusMinutes(2), TimelineEvent.Kind.CHANGE,
                        NAME, appId,
                        "应用发布 v3.1.2：下单参数校验重构，空指针异常被抛出"));
                events.add(new TimelineEvent(
                        query.alarmTime().minusMinutes(25), TimelineEvent.Kind.CHANGE,
                        NAME, appId,
                        "配置变更：调整熔断阈值（与故障时间窗相距较远）"));
            }
            case SCENE_REDIS -> events.add(new TimelineEvent(
                    query.alarmTime().minusMinutes(6), TimelineEvent.Kind.CHANGE,
                    NAME, appId + "-redis",
                    "Redis 配置变更：maxmemory-policy 被改为 noeviction，内存写满"));
            default -> {
                // 无变更
            }
        }
        return ToolResult.changes(NAME, events);
    }
}
