package com.bage.ai.ops.api.strategy.impl;

import com.bage.ai.ops.core.dto.activity.PrCreationInput;
import com.bage.ai.ops.core.dto.activity.PrCreationResult;
import com.bage.ai.ops.core.strategy.PrCreationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NonePrCreationStrategy implements PrCreationStrategy {

    @Override
    public PrCreationResult createPr(PrCreationInput input) {
        log.info("PR creation skipped (none strategy) for project: {}", input.getProjectPath());
        return PrCreationResult.builder()
                .success(true)
                .prUrl(null)
                .prInfo("PR creation skipped - using 'none' strategy")
                .build();
    }

    @Override
    public String getName() {
        return "none";
    }
}
