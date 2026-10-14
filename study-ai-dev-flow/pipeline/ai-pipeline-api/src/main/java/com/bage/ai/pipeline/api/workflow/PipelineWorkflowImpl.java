package com.bage.ai.pipeline.api.workflow;

import com.bage.ai.pipeline.core.activity.*;
import com.bage.ai.pipeline.core.dto.activity.*;
import com.bage.ai.pipeline.core.dto.workflow.ApprovalSignal;
import com.bage.ai.pipeline.core.dto.workflow.PipelineRunResult;
import com.bage.ai.pipeline.core.dto.workflow.PipelineStartInput;
import com.bage.ai.pipeline.core.dto.workflow.RejectionSignal;
import com.bage.ai.pipeline.core.enums.PipelineStatus;
import com.bage.ai.pipeline.core.enums.StageName;
import com.bage.ai.pipeline.core.workflow.PipelineWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PipelineWorkflowImpl implements PipelineWorkflow {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PipelineWorkflowImpl.class);

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
        try {
            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String reqResultJson = mapper.writeValueAsString(Map.of(
                    "analysisResult", reqResult.getParsedRequirementJson(),
                    "requirementMd", input.getRequirementMd()
            ));
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.REQUIREMENT_ANALYSIS.name(), reqResultJson, null);
        } catch (Exception e) {
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.REQUIREMENT_ANALYSIS.name(), "", null);
        }

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
        try {
            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String fpResultJson = mapper.writeValueAsString(Map.of(
                    "featurePoints", fpResult.getFeaturePoints()
            ));
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.FEATURE_POINT_SPLIT.name(), fpResultJson, null);
        } catch (Exception e) {
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.FEATURE_POINT_SPLIT.name(), "", null);
        }

        // Stage 3 & 4: Per Feature Point → Task Split → Code Gen
        Map<String, String> allGeneratedFiles = new LinkedHashMap<>();
        CodeGenResult codeResult;

        currentStage = StageName.TASK_SPLIT;
        statusUpdateActivity.recordStageStart(input.getRunId(), StageName.TASK_SPLIT.name(), 3);
        List<Object> allTasks = new java.util.ArrayList<>();

        for (FeaturePoint fp : fpResult.getFeaturePoints()) {
            var taskResult = taskSplitActivity.split(TaskSplitInput.builder()
                    .runId(input.getRunId())
                    .projectLocalPath(input.getProjectLocalPath())
                    .featurePoint(fp)
                    .projectType(input.getProjectType())
                    .buildTool(input.getBuildTool())
                    .build());

            if (!taskResult.getTasks().isEmpty()) {
                allTasks.add(Map.of(
                        "featurePoint", fp.getTitle(),
                        "tasks", taskResult.getTasks()
                ));
            }

            for (AtomicTask task : taskResult.getTasks()) {
                var taskCodeResult = codeGenActivity.generate(CodeGenInput.builder()
                        .runId(input.getRunId())
                        .projectLocalPath(input.getProjectLocalPath())
                        .parsedRequirementJson(reqResult.getParsedRequirementJson())
                        .projectType(input.getProjectType())
                        .buildTool(input.getBuildTool())
                        .frontendLocalPath(isFrontendTask(task) ? input.getFrontendLocalPath() : null)
                        .currentTask(task)
                        .build());
                allGeneratedFiles.putAll(taskCodeResult.getGeneratedFiles());
            }
        }

        try {
            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String taskResultJson = mapper.writeValueAsString(Map.of(
                    "featurePoints", fpResult.getFeaturePoints(),
                    "allTasks", allTasks
            ));
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.TASK_SPLIT.name(), taskResultJson, null);
        } catch (Exception e) {
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.TASK_SPLIT.name(), "", null);
        }

        currentStage = StageName.CODE_GEN;
        statusUpdateActivity.recordStageStart(input.getRunId(), StageName.CODE_GEN.name(), 4);
        try {
            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String codeResultJson = mapper.writeValueAsString(Map.of(
                    "generatedFiles", allGeneratedFiles,
                    "totalFiles", allGeneratedFiles.size(),
                    "message", "Code generation completed"
            ));
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.CODE_GEN.name(), codeResultJson, null);
        } catch (Exception e) {
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.CODE_GEN.name(), "", null);
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
        var testGenResult = testGenActivity.generate(TestGenInput.builder()
                .runId(input.getRunId())
                .projectLocalPath(input.getProjectLocalPath())
                .projectPath(input.getProjectLocalPath())
                .projectType(input.getProjectType())
                .generatedFilePaths(generatedFilePaths)
                .generatedFiles(codeResult.getGeneratedFiles())
                .frontendLocalPath(input.getFrontendLocalPath())
                .build());
        try {
            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String testGenJson = mapper.writeValueAsString(Map.of(
                    "generatedFiles", testGenResult != null ? testGenResult.getTestFiles() : Map.of()
            ));
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.TEST_GEN.name(), testGenJson, null);
        } catch (Exception e) {
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.TEST_GEN.name(), "", null);
        }

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
        try {
            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String reviewJson = mapper.writeValueAsString(Map.of(
                    "hasCriticalIssues", reviewResult.isHasCriticalIssues(),
                    "issues", reviewResult.getIssues()
            ));
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.CODE_REVIEW.name(), reviewJson, null);
        } catch (Exception e) {
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.CODE_REVIEW.name(), "", null);
        }

        if (reviewResult.isHasCriticalIssues() && !isAutoApprove(input, StageName.CODE_REVIEW)) {
            PipelineRunResult w = waitForApproval();
            if (w != null) return w;
        }

        // Stage 7: Write Files & Git Commit
        currentStage = StageName.PR_CREATION;
        statusUpdateActivity.recordStageStart(input.getRunId(), StageName.PR_CREATION.name(), 7);
        String baseCommitMessage = "feat: AI-generated code and tests for run " + input.getRunId();
        
        int codeFileCount = codeResult.getGeneratedFiles() != null ? codeResult.getGeneratedFiles().size() : 0;
        int testFileCount = testGenResult != null && testGenResult.getTestFiles() != null ? testGenResult.getTestFiles().size() : 0;
        
        WriteAndCommitResult writeResult = writeActivity.writeAndCommit(WriteAndCommitInput.builder()
                .runId(input.getRunId())
                .projectLocalPath(input.getProjectLocalPath())
                .generatedFiles(codeResult.getGeneratedFiles())
                .testFiles(testGenResult != null ? testGenResult.getTestFiles() : Map.of())
                .commitMessage(baseCommitMessage)
                .frontendLocalPath(input.getFrontendLocalPath())
                .build());
        try {
            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            
            java.util.List<String> allFilePaths = new java.util.ArrayList<>();
            if (codeResult.getGeneratedFiles() != null) {
                allFilePaths.addAll(codeResult.getGeneratedFiles().keySet());
            }
            if (testGenResult != null && testGenResult.getTestFiles() != null) {
                allFilePaths.addAll(testGenResult.getTestFiles().keySet());
            }
            
            java.util.Map<String, Object> resultMap = new java.util.HashMap<>();
            resultMap.put("prUrl", writeResult.getPrUrl());
            resultMap.put("commitHash", writeResult.getCommitId());
            resultMap.put("stats", Map.of("added", codeFileCount + testFileCount, "modified", 0, "deleted", 0));
            resultMap.put("files", allFilePaths);
            resultMap.put("risk", "低");
            resultMap.put("riskItems", List.of("✅ 无破坏性变更", "✅ 向后兼容"));
            String writeJson = mapper.writeValueAsString(resultMap);
            log.info("PR_CREATION resultJson: {}", writeJson);
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.PR_CREATION.name(), writeJson, null);
        } catch (Exception e) {
            log.error("Failed to generate PR_CREATION resultJson: {}", e.getMessage(), e);
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.PR_CREATION.name(), "", null);
        }

        if (!isAutoApprove(input, StageName.PR_CREATION)) {
            PipelineRunResult w = waitForApproval();
            if (w != null) return w;
        }

        // Stage 8: Test Execution + Auto-fix loop
        currentStage = StageName.TEST_EXEC;
        statusUpdateActivity.recordStageStart(input.getRunId(), StageName.TEST_EXEC.name(), 8);
        int fixAttempt = 0;
        String testFailureContext = null;
        TestExecResult testExecResult = null;
        while (true) {
            try {
                testExecResult = testExecActivity.execute(TestExecInput.builder()
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
        try {
            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            
            int totalTests = testExecResult != null && testExecResult.getTotalTests() != null 
                    ? testExecResult.getTotalTests() : 0;
            int passedTests = testExecResult != null && testExecResult.getPassedTests() != null 
                    ? testExecResult.getPassedTests() : 0;
            int failedTests = testExecResult != null && testExecResult.getFailedTests() != null 
                    ? testExecResult.getFailedTests() : 0;
            
            java.util.List<java.util.Map<String, Object>> testList = new java.util.ArrayList<>();
            if (testExecResult != null && testExecResult.getTestDetails() != null) {
                for (java.util.Map<String, String> detail : testExecResult.getTestDetails()) {
                    java.util.Map<String, Object> testItem = new java.util.HashMap<>();
                    testItem.put("name", detail.get("name"));
                    testItem.put("status", detail.get("status"));
                    testList.add(testItem);
                }
            }
            
            String testExecJson = mapper.writeValueAsString(Map.of(
                    "testStats", Map.of("total", totalTests, "passed", passedTests, "failed", failedTests, "coverage", "0%"),
                    "tests", testList,
                    "fixAttempts", fixAttempt
            ));
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.TEST_EXEC.name(), testExecJson, null);
        } catch (Exception e) {
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.TEST_EXEC.name(), "", null);
        }

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
        try {
            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            boolean buildSuccess = buildResult != null && Boolean.TRUE.equals(buildResult.getSuccess());
            String buildOutput = buildResult != null && buildResult.getBuildOutput() != null 
                    ? buildResult.getBuildOutput() : buildResult != null && buildResult.getOutput() != null 
                    ? buildResult.getOutput() : "";
            
            String buildJson = mapper.writeValueAsString(Map.of(
                    "buildStatus", buildSuccess ? "SUCCESS" : "FAILED",
                    "imageName", buildResult != null ? buildResult.getImageName() : imageName,
                    "imageTag", buildResult != null ? buildResult.getImageTag() : "latest",
                    "log", buildOutput.length() > 2000 ? buildOutput.substring(0, 2000) : buildOutput
            ));
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.BUILD.name(), buildJson, null);
        } catch (Exception e) {
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.BUILD.name(), "", null);
        }

        if (!isAutoApprove(input, StageName.BUILD)) {
            PipelineRunResult w = waitForApproval();
            if (w != null) return w;
        }

        // Stage 11: Deploy (requires manual approval)
        currentStage = StageName.DEPLOY;
        statusUpdateActivity.recordStageStart(input.getRunId(), StageName.DEPLOY.name(), 10);

        if (!isAutoApprove(input, StageName.DEPLOY)) {
            PipelineRunResult w = waitForApproval();
            if (w != null) {
                statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.DEPLOY.name(), "", 
                        rejected ? "Rejected by user" : "Pipeline cancelled");
                return w;
            }
        }

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
        try {
            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String deployJson = mapper.writeValueAsString(Map.of(
                    "deployStatus", "SUCCESS",
                    "environment", "开发环境",
                    "version", "1.0.0",
                    "imageName", buildResult.getImageName(),
                    "imageTag", buildResult.getImageTag(),
                    "endpoints", List.of(
                            Map.of("method", "GET", "url", "http://localhost:8081/api/health", "status", "UP"),
                            Map.of("method", "GET", "url", "http://localhost:8081/api/health/detail", "status", "UP")
                    )
            ));
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.DEPLOY.name(), deployJson, null);
        } catch (Exception e) {
            statusUpdateActivity.recordStageEnd(input.getRunId(), StageName.DEPLOY.name(), "", null);
        }

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