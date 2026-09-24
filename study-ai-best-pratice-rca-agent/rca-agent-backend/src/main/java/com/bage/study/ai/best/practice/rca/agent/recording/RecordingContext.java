package com.bage.study.ai.best.practice.rca.agent.recording;

/**
 * 录制上下文：基于 ThreadLocal 绑定当前分析的 RecordingSession。
 * 并行流（ForkJoinPool）场景下，由调用方在 worker 线程中显式 set/clear 传播。
 */
public final class RecordingContext {

    private static final ThreadLocal<RecordingSession> HOLDER = new ThreadLocal<>();

    private RecordingContext() {
    }

    public static void set(RecordingSession session) {
        HOLDER.set(session);
    }

    public static RecordingSession get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }

    public static boolean isRecording() {
        RecordingSession s = HOLDER.get();
        return s != null && s.getMode() == RecordingSession.Mode.RECORD;
    }

    public static boolean isReplaying() {
        RecordingSession s = HOLDER.get();
        return s != null && s.getMode() == RecordingSession.Mode.REPLAY;
    }

    public static boolean isActive() {
        return HOLDER.get() != null;
    }
}
