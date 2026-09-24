package com.bage.study.ai.best.practice.rca.agent.recording;

/**
 * 外部交互类型。RCA 管道中只有两类需要录制/回放的外部依赖：
 * 数据采集工具（MCP 风格）与 LLM 补全。
 */
public enum InteractionType {
    TOOL,
    LLM
}
