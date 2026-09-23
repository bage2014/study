package com.bage.study.ai.best.practice.rca.agent.model;

import java.util.List;

/**
 * 建议三层（Google MTTM 思路）：先止血，再根治，后预防。
 */
public record Suggestions(
        List<String> mitigation,
        List<String> remediation,
        List<String> prevention
) {
}
