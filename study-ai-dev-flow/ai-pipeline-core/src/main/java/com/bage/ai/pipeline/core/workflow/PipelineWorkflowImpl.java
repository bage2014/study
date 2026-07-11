package com.bage.ai.pipeline.core.workflow;

import com.bage.ai.pipeline.core.activity.*;
import com.bage.ai.pipeline.api.dto.activity.*;
import com.bage.ai.pipeline.api.dto.workflow.ApprovalSignal;
import com.bage.ai.pipeline.api.dto.workflow.PipelineRunResult;
import com.bage.ai.pipeline.api.dto.workflow.PipelineStartInput;
import com.bage.ai.pipeline.api.dto.workflow.RejectionSignal;
import com.bage.ai.pipeline.api.enums.PipelineStatus;
import com.bage.ai.pipeline.api.enums.StageName;
import com.bage.ai.pipeline.api.workflow.PipelineWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PipelineWorkflowImpl implements PipelineWorkflow {

    private final ActivityOptions defaultOptions = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(30))
            .build();

    private final RequirementAnalysisActivity requirementActivity =
            Workflow.newActivityStub(RequirementAnalysisActivity.class, defaultOptions);
    private final FeaturePointSplitActivity featurePointSplitActivity =
            Workflow.newActivityStub(FeaturePointSplitActivity.class, defaultOptions);
    private final TaskSplitActivity taskSplitActivity =
            Workflow.newActivityStub(TaskSplitActivity.class, defaultOptions);
    private final CodeGenerationActivity codeGenActivity =
            Workflow.newActivityStub(CodeGenerationActivity.class, defaultOptions);
    private final TestGenActivity testGenActivity =
            Workflow.newActivityStub(TestGenActivity.class, defaultOptions);
    private final CodeReviewActivity codeReviewActivity =
            Workflow.newActivityStub(CodeReviewActivity.class, defaultOptions);
    private final WriteAndCommitActivity writeActivity =
            Workflow.newActivityStub(WriteAndCommitActivity.class, defaultOptions);
    private final TestExecutionActivity testExecActivity =
            Workflow.newActivityStub(TestExecutionActivity.class, defaultOptions);
    private final BuildActivity buildActivity =
            Workflow.newActivityStub(BuildActivity.class, defaultOptions);
    private final DeployActivity deployActivity =
            Workflow.newActivityStub(DeployActivity.class, defaultOptions);
    private final UiTestActivity uiTestActivity =
            Workflow.newActivityStub(UiTestActivity.class, defaultOptions);
    private final PipelineStatusUpdateActivity statusUpdateActivity =
            Workflow.newActivityStub(PipelineStatusUpdateActivity.class, defaultOptions);

    private PipelineStatus status = PipelineStatus.PENDING;
    private StageName currentStage;
    private boolean approvalReceived = false;
    private boolean rejected = false;
    private boolean cancelled = false;
    private String humanComment = "";

    @Override
    public PipelineRunResult start(PipelineStartInput input) {
        status = PipelineStatus.RUNNING;

        // Stage 1: Requirement Analysis
        currentStage = StageName.REQUIREMENT_ANALYSIS;
        statusUpdateActivity.recordStageStart(input.getRunId(), StageName.REQUIREMENT_ANALYSIS.name(), 1);
        var reqResult = requirementActivity.analyze(RequirementAnalysisInput.builder()
                .runId(input.getRunId())
                .requirementMd(input.getRequirementMd())
                .build());
        statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.REQUIREMENT_ANALYSIS.name(), "", null);

        if (!isAutoApprove(input, StageName.REQUIREMENT_ANALYSIS)) {
            PipelineRunResult w = waitForApproval();
            if (w != null) return w;
        }

        // Stage 2: Feature Point Split
        currentStage = StageName.FEATURE_POINT_SPLIT;
        statusUpdateActivity.recordStageStart(input.getRunId(), StageName.FEATURE_POINT_SPLIT.name(), 2);
        var fpResult = featurePointSplitActivity.split(FeaturePointSplitInput.builder()
                .runId(input.getRunId())
                .parsedRequirementJson(reqResult.getParsedRequirementJson())
                .projectType(input.getProjectType())
                .buildTool(input.getBuildTool())
                .projectLocalPath(input.getProjectLocalPath())
                .build());
        statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.FEATURE_POINT_SPLIT.name(), "", null);

        // Stage 3 & 4: Per Feature Point → Task Split → Code Gen
        Map<String, String> allGeneratedFiles = new LinkedHashMap<>();
        CodeGenResult codeResult;

        for (FeaturePoint fp : fpResult.getFeaturePoints()) {
            currentStage = StageName.TASK_SPLIT;
            statusUpdateActivity.recordStageStart(input.getRunId(), StageName.TASK_SPLIT.name(), 3);
            var taskResult = taskSplitActivity.split(TaskSplitInput.builder()
                    .runId(input.getRunId())
                    .projectLocalPath(input.getProjectLocalPath())
                    .featurePoint(fp)
                    .projectType(input.getProjectType())
                    .buildTool(input.getBuildTool())
                    .build());
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.TASK_SPLIT.name(), "", null);

            for (AtomicTask task : taskResult.getTasks()) {
                currentStage = StageName.CODE_GEN;
                statusUpdateActivity.recordStageStart(input.getRunId(), StageName.CODE_GEN.name(), 4);
                CodeGenInput codeGenInput = CodeGenInput.builder()
                        .runId(input.getRunId())
                        .projectLocalPath(input.getProjectLocalPath())
                        .parsedRequirementJson(reqResult.getParsedRequirementJson())
                        .projectType(input.getProjectType())
                        .buildTool(input.getBuildTool())
                        .frontendLocalPath(isFrontendTask(task) ? input.getFrontendLocalPath() : null)
                        .currentTask(task)
                        .build();

                var taskCodeResult = codeGenActivity.generate(codeGenInput);
                statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.CODE_GEN.name(), "", null);
                allGeneratedFiles.putAll(taskCodeResult.getGeneratedFiles());
            }
        }

        codeResult = CodeGenResult.builder()
                .runId(input.getRunId())
                .generatedFiles(allGeneratedFiles)
                .message("Multi-task code generation done")
                .success(true)
                .build();

        if (!isAutoApprove(input, StageName.CODE_GEN)) {
            PipelineRunResult w = waitForApproval();
            if (w != null) return w;
        }

        // Stage 5: Test Generation
        currentStage = StageName.TEST_GEN;
        statusUpdateActivity.recordStageStart(input.getRunId(), StageName.TEST_GEN.name(), 5);
        List<String> generatedFilePaths = List.copyOf(codeResult.getGeneratedFiles().keySet());
        testGenActivity.generate(TestGenInput.builder()
                .runId(input.getRunId())
                .projectLocalPath(input.getProjectLocalPath())
                .projectPath(input.getProjectLocalPath())
                .projectType(input.getProjectType())
                .generatedFilePaths(generatedFilePaths)
                .generatedFiles(codeResult.getGeneratedFiles())
                .frontendLocalPath(input.getFrontendLocalPath())
                .build());
        statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.TEST_GEN.name(), "", null);

        if (!isAutoApprove(input, StageName.TEST_GEN)) {
            PipelineRunResult w = waitForApproval();
            if (w != null) return w;
        }

        // Stage 6: Code Review
        currentStage = StageName.CODE_REVIEW;
        statusUpdateActivity.recordStageStart(input.getRunId(), StageName.CODE_REVIEW.name(), 6);
        var reviewResult = codeReviewActivity.review(CodeReviewInput.builder()
                .runId(input.getRunId())
                .projectLocalPath(input.getProjectLocalPath())
                .generatedFiles(codeResult.getGeneratedFiles())
                .build());
        statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.CODE_REVIEW.name(), "", null);

        if (reviewResult.isHasCriticalIssues() && !isAutoApprove(input, StageName.CODE_REVIEW)) {
            PipelineRunResult w = waitForApproval();
            if (w != null) return w;
        }

        // Stage 7: Write Files & Git Commit
        currentStage = StageName.PR_CREATION;
        statusUpdateActivity.recordStageStart(input.getRunId(), StageName.PR_CREATION.name(), 7);
        String baseCommitMessage = "feat: AI-generated code and tests for run " + input.getRunId();
        WriteAndCommitResult writeResult = writeActivity.writeAndCommit(WriteAndCommitInput.builder()
                .runId(input.getRunId())
                .projectLocalPath(input.getProjectLocalPath())
                .generatedFiles(codeResult.getGeneratedFiles())
                .commitMessage(baseCommitMessage)
                .frontendLocalPath(input.getFrontendLocalPath())
                .build());
        statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.PR_CREATION.name(), "", null);

        if (!isAutoApprove(input, StageName.PR_CREATION)) {
            PipelineRunResult w = waitForApproval();
            if (w != null) return w;
        }

        // Stage 8: Test Execution + Auto-fix loop
        currentStage = StageName.TEST_EXEC;
        statusUpdateActivity.recordStageStart(input.getRunId(), StageName.TEST_EXEC.name(), 8);
        int fixAttempt = 0;
        String testFailureContext = null;
        while (true) {
            try {
                testExecActivity.execute(TestExecInput.builder()
                        .runId(input.getRunId())
                        .projectLocalPath(input.getProjectLocalPath())
                        .projectPath(input.getProjectLocalPath())
                        .projectType(input.getProjectType())
                        .buildTool(input.getBuildTool())
                        .frontendLocalPath(input.getFrontendLocalPath())
                        .sandboxEnabled(input.isSandboxEnabled())
                        .build());
                break;
            } catch (ActivityFailure e) {
                testFailureContext = extractFailureMessage(e);

                if (!input.isAutoFixOnTestFail() || fixAttempt >= input.getMaxTestFixRetries()) {
                    if (!isAutoApprove(input, StageName.TEST_EXEC)) {
                        PipelineRunResult w = waitForApproval();
                        if (w != null) return w;
                        break;
                    }
                    throw e;
                }

                fixAttempt++;
                currentStage = StageName.CODE_GEN;
                CodeGenInput fixInput = CodeGenInput.builder()
                        .runId(input.getRunId())
                        .projectLocalPath(input.getProjectLocalPath())
                        .parsedRequirementJson(reqResult.getParsedRequirementJson())
                        .projectType(input.getProjectType())
                        .buildTool(input.getBuildTool())
                        .frontendLocalPath(input.getFrontendLocalPath())
                        .testFailureContext(testFailureContext)
                        .build();
                codeResult = codeGenActivity.generate(fixInput);

                currentStage = StageName.PR_CREATION;
                String fixMsg = "fix(ai-retry-" + fixAttempt + "): auto-fix for run " + input.getRunId();
                writeResult = writeActivity.writeAndCommit(WriteAndCommitInput.builder()
                        .runId(input.getRunId())
                        .projectLocalPath(input.getProjectLocalPath())
                        .generatedFiles(codeResult.getGeneratedFiles())
                        .commitMessage(fixMsg)
                        .frontendLocalPath(input.getFrontendLocalPath())
                        .build());

                currentStage = StageName.TEST_EXEC;
            }
        }
        statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.TEST_EXEC.name(), "", null);

        if (!isAutoApprove(input, StageName.TEST_EXEC)) {
            PipelineRunResult w = waitForApproval();
            if (w != null) return w;
        }

        // Stage 9: UI Test (only fullstack)
        if (input.getFrontendLocalPath() != null && !input.getFrontendLocalPath().isBlank()) {
            currentStage = StageName.UI_TEST;
            uiTestActivity.run(UiTestInput.builder()
                    .runId(input.getRunId())
                    .frontendLocalPath(input.getFrontendLocalPath())
                    .projectLocalPath(input.getProjectLocalPath())
                    .frontendUrl("http://localhost:5173")
                    .backendUrl("http://localhost:8081")
                    .build());

            if (!isAutoApprove(input, StageName.UI_TEST)) {
                PipelineRunResult w = waitForApproval();
                if (w != null) return w;
            }
        }

        // Stage 10: Build
        currentStage = StageName.BUILD;
        statusUpdateActivity.recordStageStart(input.getRunId(), StageName.BUILD.name(), 9);
        String imageName = "ai-app-" + input.getRunId().substring(0, 8).toLowerCase();
        var buildResult = buildActivity.build(BuildInput.builder()
                .runId(input.getRunId())
                .projectLocalPath(input.getProjectLocalPath())
                .projectPath(input.getProjectLocalPath())
                .projectType(input.getProjectType())
                .buildTool(input.getBuildTool())
                .imageName(imageName)
                .frontendLocalPath(input.getFrontendLocalPath())
                .build());
        statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.BUILD.name(), "", null);

        if (!isAutoApprove(input, StageName.BUILD)) {
            PipelineRunResult w = waitForApproval();
            if (w != null) return w;
        }

        // Stage 11: Deploy
        currentStage = StageName.DEPLOY;
        statusUpdateActivity.recordStageStart(input.getRunId(), StageName.DEPLOY.name(), 10);
        String containerName = "ai-app-" + input.getRunId().substring(0, 8);
        var deployResult = deployActivity.deploy(DeployInput.builder()
                .runId(input.getRunId())
                .projectPath(input.getProjectLocalPath())
                .imageName(buildResult.getImageName())
                .imageTag(buildResult.getImageTag())
                .containerName(containerName)
                .hostPort(8081)
                .containerPort(8080)
                .frontendLocalPath(input.getFrontendLocalPath())
                .frontendHostPort(5173)
                .build());
        statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.DEPLOY.name(), "", null);

        status = PipelineStatus.COMPLETED;
        String fpCount = fpResult.getFeaturePoints().size() + " feature point(s)";
        String prInfo = (writeResult.getPrUrl() != null && !writeResult.getPrUrl().isBlank())
                ? "  PR: " + writeResult.getPrUrl() : "";
        PipelineRunResult finalResult = PipelineRunResult.completed(
                "Pipeline completed. Access: " + deployResult.getAccessUrl()
                + "  Branch: ai-run-" + input.getRunId().substring(0, 8)
                + "  Decomposed: " + fpCount
                + prInfo
                + (fixAttempt > 0 ? "  (auto-fixed in " + fixAttempt + " attempt(s))" : ""));

        statusUpdateActivity.updateStatus(PipelineStatusUpdateInput.builder()
                .runId(input.getRunId())
                .status(PipelineStatus.COMPLETED)
                .stage(StageName.DEPLOY.name())
                .build());
        statusUpdateActivity.updateResult(input.getRunId(), finalResult);

        return finalResult;
    }

    @Override
    public void approve(ApprovalSignal signal) {
        approvalReceived = true;
        humanComment = signal.getComment() != null ? signal.getComment() : "";
    }

    @Override
    public void reject(RejectionSignal signal) {
        rejected = true;
        humanComment = signal.getReason() != null ? signal.getReason() : "";
    }

    @Override
    public void cancel() {
        cancelled = true;
        approvalReceived = true;
    }

    @Override
    public PipelineStatus getStatus() {
        if (cancelled) {
            return PipelineStatus.CANCELLED;
        }
        return status;
    }

    @Override
    public StageName getCurrentStage() {
        return currentStage;
    }

    private PipelineRunResult waitForApproval() {
        status = PipelineStatus.WAITING_APPROVAL;
        approvalReceived = false;
        rejected = false;

        Workflow.await(() -> approvalReceived || rejected);

        if (cancelled) {
            status = PipelineStatus.CANCELLED;
            return PipelineRunResult.builder()
                    .status(PipelineStatus.CANCELLED)
                    .success(false)
                    .message("Pipeline cancelled")
                    .build();
        }

        if (rejected) {
            status = PipelineStatus.FAILED;
            return PipelineRunResult.failed("Rejected at stage " + currentStage, humanComment);
        }
        status = PipelineStatus.RUNNING;
        return null;
    }

    private boolean isAutoApprove(PipelineStartInput input, StageName stage) {
        if (input.getAutoApproveMap() == null) return false;
        return Boolean.TRUE.equals(input.getAutoApproveMap().get(stage));
    }

    private boolean isFrontendTask(AtomicTask task) {
        return task.getType() != null && task.getType().name().equals("FRONTEND");
    }

    private String extractFailureMessage(ActivityFailure e) {
        if (e.getCause() != null) {
            return e.getCause().getMessage() != null ? e.getCause().getMessage() : e.getMessage();
        }
        return e.getMessage();
    }
}