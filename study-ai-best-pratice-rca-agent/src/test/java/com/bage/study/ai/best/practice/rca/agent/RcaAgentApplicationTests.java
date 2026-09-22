package com.bage.study.ai.best.practice.rca.agent;

import com.bage.study.ai.best.practice.rca.agent.playbook.ScanPlaybook;
import com.bage.study.ai.best.practice.rca.agent.service.RcaOrchestrator;
import com.bage.study.ai.best.practice.rca.agent.tool.ToolRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RcaAgentApplicationTests {

    @Autowired
    private RcaOrchestrator orchestrator;

    @Autowired
    private ToolRegistry toolRegistry;

    @Autowired
    private ScanPlaybook scanPlaybook;

    @Test
    void contextLoadsAndPlaybookValidated() {
        assertThat(orchestrator).isNotNull();
        assertThat(toolRegistry.all()).isNotEmpty();
        // @PostConstruct 校验在容器启动时已隐式执行；此处显式再跑一次保证幂等
        scanPlaybook.validate();
    }
}
