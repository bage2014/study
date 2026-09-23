package com.bage.study.ai.best.practice.rca.agent.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * RCA 分析请求。scene 用于驱动 Mock 工具的数据场景：
 * default / db_slow_query / mq_lag / app_resource / supplier_failure / high_error / redis
 */
public class RcaRequest {

    @NotBlank(message = "appId 不能为空")
    private String appId = "order-service";

    @NotBlank(message = "alarmDescription 不能为空")
    private String alarmDescription;

    private LocalDateTime alarmTime;

    private String scene = "default";

    /** 是否允许在 Plan/Analyze 节点调用 LLM；关闭时走确定性规则，LLM 失败同样软降级。 */
    private Boolean enableLlm = false;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAlarmDescription() {
        return alarmDescription;
    }

    public void setAlarmDescription(String alarmDescription) {
        this.alarmDescription = alarmDescription;
    }

    public LocalDateTime getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(LocalDateTime alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public Boolean getEnableLlm() {
        return enableLlm;
    }

    public void setEnableLlm(Boolean enableLlm) {
        this.enableLlm = enableLlm;
    }
}
