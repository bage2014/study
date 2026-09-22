package com.bage.study.ai.best.practice.rca.agent.model;

/**
 * 症状模式：Plan 阶段 LLM/规则对假设的结构化分类，Playbook 按
 * targetType:symptom 二维键做确定性工具路由。
 */
public enum SymptomPattern {
    HIGH_LATENCY,
    HIGH_ERROR_RATE,
    RESOURCE_EXHAUSTED,
    CONSUMER_LAG,
    DEPENDENCY_FAILURE
}
