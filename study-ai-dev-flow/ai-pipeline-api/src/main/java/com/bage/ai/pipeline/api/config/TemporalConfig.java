package com.bage.ai.pipeline.api.config;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import com.bage.ai.pipeline.core.workflow.PipelineWorkflowImpl;
import com.bage.ai.pipeline.api.activity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class TemporalConfig {

    @Bean(destroyMethod = "shutdown")
    public WorkflowServiceStubs workflowServiceStubs() {
        return WorkflowServiceStubs.newInstance();
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs serviceStubs) {
        return WorkflowClient.newInstance(serviceStubs);
    }

    @Bean(destroyMethod = "shutdown")
    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
        return WorkerFactory.newInstance(workflowClient);
    }

    @Bean
    public Worker worker(WorkerFactory workerFactory,
                         RequirementAnalysisActivityImpl requirementAnalysisActivity,
                         FeaturePointSplitActivityImpl featurePointSplitActivity,
                         TaskSplitActivityImpl taskSplitActivity,
                         CodeGenerationActivityImpl codeGenerationActivity,
                         CodeReviewActivityImpl codeReviewActivity,
                         TestGenerationActivityImpl testGenerationActivity,
                         BuildActivityImpl buildActivity,
                         TestExecutionActivityImpl testExecutionActivity,
                         DeployActivityImpl deployActivity,
                         PrCreationActivityImpl prCreationActivity,
                         ApprovalWaitActivityImpl approvalWaitActivity,
                         UiTestActivityImpl uiTestActivity,
                         WriteAndCommitActivityImpl writeAndCommitActivity) {
        Worker worker = workerFactory.newWorker("pipeline-task-queue");
        worker.registerWorkflowImplementationTypes(PipelineWorkflowImpl.class);
        worker.registerActivitiesImplementations(
                requirementAnalysisActivity,
                featurePointSplitActivity,
                taskSplitActivity,
                codeGenerationActivity,
                codeReviewActivity,
                testGenerationActivity,
                buildActivity,
                testExecutionActivity,
                deployActivity,
                prCreationActivity,
                approvalWaitActivity,
                uiTestActivity,
                writeAndCommitActivity
        );
        workerFactory.start();
        log.info("Temporal worker started on task queue: pipeline-task-queue");
        return worker;
    }
}