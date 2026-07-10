package com.bage.ai.pipeline.api.activity;

import com.bage.ai.pipeline.core.dto.activity.PrCreationInput;
import com.bage.ai.pipeline.core.dto.activity.PrCreationResult;
import com.bage.ai.pipeline.core.activity.PrCreationActivity;
import com.bage.ai.pipeline.api.strategy.PrCreationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PrCreationActivityImpl implements PrCreationActivity {

    private final PrCreationStrategy prCreationStrategy;

    public PrCreationActivityImpl(PrCreationStrategy prCreationStrategy) {
        this.prCreationStrategy = prCreationStrategy;
    }

    @Override
    public PrCreationResult createPr(PrCreationInput input) {
        log.info("Starting PR creation for pipeline: {}, strategy: {}", input.getPipelineId(), prCreationStrategy.getName());
        PrCreationResult result = prCreationStrategy.createPr(input);
        log.info("PR creation completed for pipeline: {}, success: {}", input.getPipelineId(), result.getSuccess());
        return result;
    }
}