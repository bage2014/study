package com.bage.study.ai.best.practice.rca.agent.recording;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实现：仅用于单元测试，保持与旧版内存 RecordingService 行为一致。
 */
public class InMemoryRecordingService extends RecordingService {

    private final Map<String, AnalysisRecord> records = new ConcurrentHashMap<>();
    private final Map<String, Recording> recordings = new ConcurrentHashMap<>();

    public InMemoryRecordingService() {
        super(null, null);
    }

    @Override
    public void save(AnalysisRecord record) {
        records.put(record.getAnalysisId(), record);
        if (record.getRecording() != null) {
            recordings.put(record.getRecording().getRecordingId(), record.getRecording());
        }
    }

    @Override
    public AnalysisRecord get(String analysisId) {
        AnalysisRecord record = records.get(analysisId);
        if (record != null && record.isRecorded() && record.getRecording() == null) {
            // 尝试从 recordings 补全
            recordings.values().stream()
                    .filter(r -> analysisId.equals(r.getAnalysisId()))
                    .findFirst()
                    .ifPresent(record::setRecording);
        }
        return record;
    }

    @Override
    public List<AnalysisRecord> list() {
        List<AnalysisRecord> all = new ArrayList<>(records.values());
        all.sort(Comparator.comparing(AnalysisRecord::getCreatedAt).reversed());
        return all;
    }

    @Override
    public boolean delete(String analysisId) {
        AnalysisRecord removed = records.remove(analysisId);
        if (removed != null && removed.getRecording() != null) {
            recordings.remove(removed.getRecording().getRecordingId());
        }
        return removed != null;
    }

    @Override
    public List<Recording> listRecordings() {
        List<Recording> all = new ArrayList<>(recordings.values());
        all.sort(Comparator.comparing(Recording::getCreatedAt).reversed());
        return all;
    }

    @Override
    public Recording getRecording(String recordingId) {
        return recordings.get(recordingId);
    }
}
