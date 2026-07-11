package com.bage.ai.pipeline.api.enums;

public enum StageName {
    REQUIREMENT_ANALYSIS,
    FEATURE_POINT_SPLIT,
    TASK_SPLIT,
    CODE_GEN,
    TEST_GEN,
    CODE_REVIEW,
    PR_CREATION,
    TEST_EXEC,
    BUILD,
    DEPLOY,

    CODE_SEARCH,
    UI_TEST,
    CI_TRIGGER
}
