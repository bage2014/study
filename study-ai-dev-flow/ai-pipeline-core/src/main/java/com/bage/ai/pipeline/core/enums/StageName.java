package com.bage.ai.pipeline.core.enums;

public enum StageName {
    REQUIREMENT_ANALYSIS,
    FEATURE_POINT_SPLIT,
    TASK_SPLIT,
    CODE_SEARCH,
    CODE_GEN,
    TEST_GEN,
    CODE_REVIEW,
    TEST_EXEC,
    UI_TEST,
    PR_CREATION,
    CI_TRIGGER,
    BUILD,
    DEPLOY
}