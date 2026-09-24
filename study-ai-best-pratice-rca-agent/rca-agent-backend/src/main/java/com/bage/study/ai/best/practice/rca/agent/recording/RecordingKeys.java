package com.bage.study.ai.best.practice.rca.agent.recording;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 录制匹配复合键工具：type + name + SHA-256(入参JSON)。
 * 复合键可容忍 BroadScan 并发导致的交互乱序，保证回放按入参精确命中。
 */
public final class RecordingKeys {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private RecordingKeys() {
    }

    public static String toolKey(String toolName, Object input) {
        return InteractionType.TOOL + ":" + toolName + ":" + sha256(toJson(input));
    }

    public static String llmKey(Object input) {
        return InteractionType.LLM + ":complete:" + sha256(toJson(input));
    }

    public static String toJson(Object obj) {
        if (obj == null) {
            return "null";
        }
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return String.valueOf(obj);
        }
    }

    private static String sha256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is guaranteed by JDK spec
            throw new IllegalStateException(e);
        }
    }
}
