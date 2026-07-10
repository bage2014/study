package com.bage.ai.pipeline.api.config;

import com.bage.ai.pipeline.agent.config.AgentAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(AgentAutoConfiguration.class)
public class ApiAutoConfiguration {
}