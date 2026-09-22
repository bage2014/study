package com.bage.study.ai.best.practice.rca.agent.config;

import com.bage.study.ai.best.practice.rca.agent.tool.MockToolCatalog;
import com.bage.study.ai.best.practice.rca.agent.tool.ToolRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolConfig {

    @Bean
    public ToolRegistry toolRegistry() {
        return new ToolRegistry(MockToolCatalog.allTools());
    }
}
