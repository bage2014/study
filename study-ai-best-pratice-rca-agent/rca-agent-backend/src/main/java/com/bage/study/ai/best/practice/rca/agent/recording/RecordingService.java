package com.bage.study.ai.best.practice.rca.agent.recording;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分析历史与录制的内存存储服务。
 * 以 analysisId 为主键；录制通过 analysisId 与分析记录关联。
 */
@Service
public class RecordingService {

    private final ConcurrentHashMap<String, AnalysisRecord> records = new ConcurrentHashMap<>();

    public void save(AnalysisRecord record) {
        records.put(record.getAnalysisId(), record);
    }

    public AnalysisRecord get(String analysisId) {
        return records.get(analysisId);
    }

    public List<AnalysisRecord> list() {
        List<AnalysisRecord> all = new ArrayList<>(records.values());
        all.sort(Comparator.comparing(AnalysisRecord::getCreatedAt).reversed());
        return all;
    }

    public boolean delete(String analysisId) {
        return records.remove(analysisId) != null;
    }

    public List<Recording> listRecordings() {
        return records.values().stream()
                .map(AnalysisRecord::getRecording)
                .filter(java.util.Objects::nonNull)
                .sorted(Comparator.comparing(Recording::getCreatedAt).reversed())
                .toList();
    }

    public Recording getRecording(String recordingId) {
        return records.values().stream()
                .map(AnalysisRecord::getRecording)
                .filter(r -> r != null && recordingId.equals(r.getRecordingId()))
                .findFirst()
                .orElse(null);
    }
}
