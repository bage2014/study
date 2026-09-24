package com.bage.study.ai.best.practice.rca.agent.llm;

import com.bage.study.ai.best.practice.rca.agent.recording.InteractionRecord;
import com.bage.study.ai.best.practice.rca.agent.recording.InteractionType;
import com.bage.study.ai.best.practice.rca.agent.recording.RecordingContext;
import com.bage.study.ai.best.practice.rca.agent.recording.RecordingKeys;
import com.bage.study.ai.best.practice.rca.agent.recording.RecordingSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * LLM 统一入口：仅在受约束节点（假设生成 / 证据解读）使用。
 * 未启用、无 ChatClient、调用失败 —— 一律返回 null 由调用方确定性降级，
 * 保证 RCA 主链路永远可运行、可复现。
 */
@Component
public class LlmGateway {

    private static final Logger log = LoggerFactory.getLogger(LlmGateway.class);

    private final boolean enabledByConfig;
    private final ChatClient chatClient;

    public LlmGateway(ObjectProvider<ChatClient.Builder> builderProvider,
                      @Value("${rca.llm.enabled:false}") boolean enabledByConfig) {
        this.enabledByConfig = enabledByConfig;
        ChatClient client = null;
        if (enabledByConfig) {
            try {
                ChatClient.Builder builder = builderProvider.getIfAvailable();
                if (builder != null) {
                    client = builder.build();
                } else {
                    log.warn("rca.llm.enabled=true but ChatClient.Builder unavailable, LLM nodes will degrade");
                }
            } catch (Exception e) {
                log.warn("Failed to build ChatClient, LLM nodes will degrade: {}", e.getMessage());
            }
        }
        this.chatClient = client;
    }

    public boolean isEnabled() {
        return enabledByConfig && chatClient != null;
    }

    /**
     * 同步补全；任何异常（超时/鉴权/网络/内容问题）都软失败返回 null。
     * 支持录制/回放：REPLAY 模式返回录制值，RECORD 模式记录入参出参。
     */
    public String complete(String prompt) {
        RecordingSession session = RecordingContext.get();

        // REPLAY 模式：返回录制的 LLM 出参
        if (session != null && session.getMode() == RecordingSession.Mode.REPLAY) {
            String key = RecordingKeys.llmKey(prompt);
            InteractionRecord record = session.replay(key);
            if (record != null) {
                return "null".equals(record.getOutputJson()) ? null : record.getOutputJson();
            }
            log.warn("Replay LLM found no matching recorded interaction, fallback to live");
        }

        long start = System.currentTimeMillis();
        String inputJson = RecordingKeys.toJson(prompt);
        String response = null;
        boolean success = true;
        String error = null;
        try {
            if (!isEnabled()) {
                return null;
            }
            response = chatClient.prompt().user(prompt).call().content();
            return response;
        } catch (Exception e) {
            success = false;
            error = e.getMessage();
            log.warn("LLM call failed, degrade to deterministic path: {}", e.getMessage());
            return null;
        } finally {
            long duration = System.currentTimeMillis() - start;
            if (session != null && session.getMode() == RecordingSession.Mode.RECORD) {
                String outputJson = response == null ? "null" : RecordingKeys.toJson(response);
                session.record(new InteractionRecord(
                        InteractionType.LLM,
                        "complete",
                        inputJson,
                        outputJson,
                        LocalDateTime.now(),
                        duration,
                        success,
                        error
                ));
            }
        }
    }
}
