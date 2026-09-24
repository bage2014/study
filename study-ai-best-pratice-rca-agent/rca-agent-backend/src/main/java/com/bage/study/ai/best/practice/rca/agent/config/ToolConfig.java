package com.bage.study.ai.best.practice.rca.agent.config;

import com.bage.study.ai.best.practice.rca.agent.recording.RecordingMcpTool;
import com.bage.study.ai.best.practice.rca.agent.tool.MockToolCatalog;
import com.bage.study.ai.best.practice.rca.agent.tool.McpTool;
import com.bage.study.ai.best.practice.rca.agent.tool.ToolRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ToolConfig {

    @Bean
    public ToolRegistry toolRegistry() {
        List<McpTool> wrapped = MockToolCatalog.allTools().stream()
                .map(RecordingMcpTool::new)
                .map(t -> (McpTool) t)
                .toList();
        return new ToolRegistry(wrapped);
    }
}
