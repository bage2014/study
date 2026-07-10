package com.bage.ai.pipeline.agent.config;

import com.bage.ai.pipeline.agent.tool.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

@Configuration
@Import(ModelGatewayConfig.class)
public class AgentAutoConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public FileReadTool fileReadTool() {
        return new FileReadTool();
    }

    @Bean
    public FileWriteTool fileWriteTool() {
        return new FileWriteTool();
    }

    @Bean
    public GitTool gitTool() {
        return new GitTool();
    }

    @Bean
    public MavenTool mavenTool() {
        return new MavenTool();
    }

    @Bean
    public NpmTool npmTool() {
        return new NpmTool();
    }

    @Bean
    public DockerTool dockerTool() {
        return new DockerTool();
    }

    @Bean
    public AstAnalysisTool astAnalysisTool() {
        return new AstAnalysisTool();
    }

    @Bean
    public AgentToolRegistry agentToolRegistry(
            FileReadTool fileReadTool,
            FileWriteTool fileWriteTool,
            GitTool gitTool,
            MavenTool mavenTool,
            NpmTool npmTool,
            DockerTool dockerTool,
            AstAnalysisTool astAnalysisTool) {
        return new AgentToolRegistry(List.of(fileReadTool, fileWriteTool, gitTool, mavenTool, npmTool, dockerTool, astAnalysisTool));
    }
}