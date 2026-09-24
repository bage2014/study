package com.bage.study.ai.best.practice.rca.agent.recording;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 单次 RCA 分析的录制会话，绑定到线程（含并行流 worker 线程）。
 * - RECORD 模式：所有外部交互追加到 recording.interactions。
 * - REPLAY 模式：按复合键查找未消费的录制交互并返回其出参。
 */
public class RecordingSession {

    public enum Mode { LIVE, RECORD, REPLAY }

    private final Mode mode;
    private final Recording recording;
    /** REPLAY 模式下已消费的交互下标，线程安全由外部同步保证。 */
    private final Set<Integer> consumed = new HashSet<>();

    public RecordingSession(Mode mode, Recording recording) {
        this.mode = mode;
        this.recording = recording;
    }

    public Mode getMode() {
        return mode;
    }

    public Recording getRecording() {
        return recording;
    }

    /** RECORD 模式追加一条交互。 */
    public synchronized void record(InteractionRecord record) {
        recording.add(record);
    }

    /** REPLAY 模式按复合键查找下一条未消费的匹配交互。 */
    public synchronized InteractionRecord replay(String key) {
        List<InteractionRecord> interactions = recording.getInteractions();
        for (int i = 0; i < interactions.size(); i++) {
            if (consumed.contains(i)) {
                continue;
            }
            InteractionRecord r = interactions.get(i);
            String rKey = r.getType() + ":" + r.getName() + ":" + sha256(r.getInputJson());
            if (rKey.equals(key)) {
                consumed.add(i);
                return r;
            }
        }
        return null;
    }

    static String sha256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
