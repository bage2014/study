package com.bage.ai.pipeline.api.activity;

import com.bage.ai.pipeline.agent.service.UiTestAgentService;
import com.bage.ai.pipeline.core.dto.activity.UiTestInput;
import com.bage.ai.pipeline.core.dto.activity.UiTestResult;
import com.bage.ai.pipeline.core.activity.UiTestActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UiTestActivityImpl implements UiTestActivity {

    private final UiTestAgentService uiTestAgentService;

    public UiTestActivityImpl(UiTestAgentService uiTestAgentService) {
        this.uiTestAgentService = uiTestAgentService;
    }

    @Override
    public UiTestResult run(UiTestInput input) {
        log.info("Starting UI test for run: {}", input.getRunId());
        String testReport = uiTestAgentService.generateAndRunTests(
                input.getFrontendLocalPath(),
                input.getFrontendUrl(),
                input.getBackendUrl()
        );
        log.info("UI test completed for run: {}", input.getRunId());
        return UiTestResult.builder()
                .runId(input.getRunId())
                .success(true)
                .testReport(testReport)
                .build();
    }
}