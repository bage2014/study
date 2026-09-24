package com.bage.study.ai.best.practice.rca.agent.recording;

import com.bage.study.ai.best.practice.rca.agent.tool.McpTool;
import com.bage.study.ai.best.practice.rca.agent.tool.ToolQuery;
import com.bage.study.ai.best.practice.rca.agent.tool.ToolResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * McpTool 装饰器：根据 RecordingContext 的模式决定录制、回放或直透传。
 * - LIVE：直接调用委托工具。
 * - RECORD：调用委托工具，并把入参/出参/耗时写入当前 RecordingSession。
 * - REPLAY：按复合键查找录制的出参并反序列化返回，不调用真实工具。
 */
public class RecordingMcpTool implements McpTool {

    private static final Logger log = LoggerFactory.getLogger(RecordingMcpTool.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final McpTool delegate;

    public RecordingMcpTool(McpTool delegate) {
        this.delegate = delegate;
    }

    public McpTool getDelegate() {
        return delegate;
    }

    @Override
    public String name() {
        return delegate.name();
    }

    @Override
    public String description() {
        return delegate.description();
    }

    @Override
    public String layer() {
        return delegate.layer();
    }

    @Override
    public ToolResult execute(ToolQuery query) {
        RecordingSession session = RecordingContext.get();

        // REPLAY 模式：返回录制值
        if (session != null && session.getMode() == RecordingSession.Mode.REPLAY) {
            String key = RecordingKeys.toolKey(delegate.name(), query);
            InteractionRecord record = session.replay(key);
            if (record != null) {
                try {
                    return MAPPER.readValue(record.getOutputJson(), ToolResult.class);
                } catch (Exception e) {
                    log.warn("Replay tool {} failed to deserialize output, fallback to live: {}",
                            delegate.name(), e.getMessage());
                }
            } else {
                log.warn("Replay tool {} found no matching recorded interaction, fallback to live", delegate.name());
            }
            // 回放未命中时回退到真实调用，保证流程不中断
            return delegate.execute(query);
        }

        // RECORD 或 LIVE：执行真实工具
        long start = System.currentTimeMillis();
        String inputJson = RecordingKeys.toJson(query);
        ToolResult result = null;
        boolean success = true;
        String error = null;
        try {
            result = delegate.execute(query);
        } catch (RuntimeException e) {
            success = false;
            error = e.getMessage();
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - start;
            if (session != null && session.getMode() == RecordingSession.Mode.RECORD) {
                String outputJson = (success && result != null) ? RecordingKeys.toJson(result) : "null";
                session.record(new InteractionRecord(
                        InteractionType.TOOL,
                        delegate.name(),
                        inputJson,
                        outputJson,
                        LocalDateTime.now(),
                        duration,
                        success,
                        error
                ));
            }
        }
        return result;
    }
}
