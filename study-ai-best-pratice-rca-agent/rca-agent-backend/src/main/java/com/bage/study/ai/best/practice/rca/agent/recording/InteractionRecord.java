package com.bage.study.ai.best.practice.rca.agent.recording;

import java.time.LocalDateTime;

/**
 * 单条外部交互录制：入参/出参均以 JSON 字符串持久化，便于跨语言查看与回放。
 */
public class InteractionRecord {

    private InteractionType type;
    private String name;
    private String inputJson;
    private String outputJson;
    private LocalDateTime timestamp;
    private long durationMs;
    private boolean success;
    private String error;

    public InteractionRecord() {
    }

    public InteractionRecord(InteractionType type, String name, String inputJson, String outputJson,
                             LocalDateTime timestamp, long durationMs, boolean success, String error) {
        this.type = type;
        this.name = name;
        this.inputJson = inputJson;
        this.outputJson = outputJson;
        this.timestamp = timestamp;
        this.durationMs = durationMs;
        this.success = success;
        this.error = error;
    }

    public InteractionType getType() {
        return type;
    }

    public void setType(InteractionType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInputJson() {
        return inputJson;
    }

    public void setInputJson(String inputJson) {
        this.inputJson = inputJson;
    }

    public String getOutputJson() {
        return outputJson;
    }

    public void setOutputJson(String outputJson) {
        this.outputJson = outputJson;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
