package com.bage.ai.pipeline.core.activity;

import com.bage.ai.pipeline.agent.service.TaskSplitAgentService;
import com.bage.ai.pipeline.api.dto.activity.AtomicTask;
import com.bage.ai.pipeline.api.dto.activity.TaskSplitInput;
import com.bage.ai.pipeline.api.dto.activity.TaskSplitResult;
import com.bage.ai.pipeline.core.activity.TaskSplitActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TaskSplitActivityImpl implements TaskSplitActivity {

    private final TaskSplitAgentService taskSplitAgentService;

    public TaskSplitActivityImpl(TaskSplitAgentService taskSplitAgentService) {
        this.taskSplitAgentService = taskSplitAgentService;
    }

    @Override
    public TaskSplitResult split(TaskSplitInput input) {
        log.info("Starting task split for feature point: {}", input.getFeaturePoint().getId());
        List<AtomicTask> tasks = taskSplitAgentService.split(
                input.getProjectLocalPath(),
                input.getFeaturePoint(),
                input.getProjectType(),
                input.getBuildTool()
        );
        log.info("Task split completed: {} atomic tasks generated", tasks.size());
        return TaskSplitResult.builder()
                .runId(input.getRunId())
                .tasks(tasks)
                .build();
    }
}