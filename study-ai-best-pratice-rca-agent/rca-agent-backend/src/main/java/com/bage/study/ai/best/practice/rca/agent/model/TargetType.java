package com.bage.study.ai.best.practice.rca.agent.model;

/**
 * 假设目标类型，对应 rca.md 的 6 类故障信号数据域。
 */
public enum TargetType {
    APP,
    DB,
    REDIS,
    MQ,
    SUPPLIER,
    INFRA
}
