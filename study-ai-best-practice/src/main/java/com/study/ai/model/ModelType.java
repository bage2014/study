package com.study.ai.model;

public enum ModelType {
    DEEPSEEK("deepseek", "DeepSeek Chat Model"),
    OPENAI("openai", "OpenAI Chat Model"),
    CLAUDE("claude", "Anthropic Claude Model");

    private final String code;
    private final String description;

    ModelType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ModelType fromCode(String code) {
        for (ModelType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return DEEPSEEK;
    }
}
