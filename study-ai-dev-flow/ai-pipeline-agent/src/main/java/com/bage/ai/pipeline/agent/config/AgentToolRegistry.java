package com.bage.ai.pipeline.agent.config;

import com.bage.ai.pipeline.agent.tool.AgentTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AgentToolRegistry {

    private final Map<String, AgentTool> tools;

    public AgentToolRegistry(List<AgentTool> agentTools) {
        this.tools = agentTools.stream()
                .collect(Collectors.toMap(
                        AgentTool::toolName,
                        t -> t,
                        (a, b) -> b,
                        LinkedHashMap::new));
        log.info("AgentToolRegistry: {} tools registered: {}", tools.size(), tools.keySet());
    }

    public Object[] getTools(String... names) {
        return Arrays.stream(names)
                .map(tools::get)
                .filter(Objects::nonNull)
                .toArray();
    }

    public Object[] getAllTools() {
        return tools.values().toArray();
    }

    public boolean hasTools(String... names) {
        return Arrays.stream(names).allMatch(tools::containsKey);
    }
}