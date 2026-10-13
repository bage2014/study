package com.bage.study.ai.best.practice.rca.agent.recording;

import com.bage.study.ai.best.practice.rca.agent.dto.RcaRequest;
import com.bage.study.ai.best.practice.rca.agent.dto.RcaResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 分析历史与录制数据的持久化服务。
 * 将 AnalysisRecord / Recording 序列化为 JSON 存储到 H2，避免对象映射的复杂性。
 */
@Service
public class RecordingService {

    private static final Logger log = LoggerFactory.getLogger(RecordingService.class);

    private final AnalysisRecordRepository analysisRecordRepository;
    private final RecordingRepository recordingRepository;
    private final ObjectMapper objectMapper;

    public RecordingService(AnalysisRecordRepository analysisRecordRepository,
                            RecordingRepository recordingRepository) {
        this.analysisRecordRepository = analysisRecordRepository;
        this.recordingRepository = recordingRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Transactional
    public void save(AnalysisRecord record) {
        try {
            AnalysisRecordEntity entity = new AnalysisRecordEntity();
            entity.setAnalysisId(record.getAnalysisId());
            entity.setRequestJson(objectMapper.writeValueAsString(record.getRequest()));
            entity.setResponseJson(objectMapper.writeValueAsString(record.getResponse()));
            entity.setResultLevel(record.getResultLevel());
            entity.setRecorded(record.isRecorded());
            entity.setCreatedAt(record.getCreatedAt() != null ? record.getCreatedAt() : LocalDateTime.now());
            analysisRecordRepository.save(entity);

            if (record.getRecording() != null) {
                RecordingEntity recEntity = new RecordingEntity();
                recEntity.setRecordingId(record.getRecording().getRecordingId());
                recEntity.setAnalysisId(record.getRecording().getAnalysisId());
                recEntity.setInteractionsJson(objectMapper.writeValueAsString(record.getRecording().getInteractions()));
                recEntity.setCreatedAt(record.getRecording().getCreatedAt() != null
                        ? record.getRecording().getCreatedAt() : LocalDateTime.now());
                recordingRepository.save(recEntity);
            }
            log.debug("Saved analysis record {} to H2", record.getAnalysisId());
        } catch (Exception e) {
            log.error("Failed to save analysis record {}", record.getAnalysisId(), e);
            throw new RuntimeException("保存分析记录失败", e);
        }
    }

    public AnalysisRecord get(String analysisId) {
        try {
            Optional<AnalysisRecordEntity> entityOpt = analysisRecordRepository.findById(analysisId);
            if (entityOpt.isEmpty()) {
                return null;
            }
            AnalysisRecordEntity entity = entityOpt.get();
            AnalysisRecord record = new AnalysisRecord();
            record.setAnalysisId(entity.getAnalysisId());
            record.setRequest(objectMapper.readValue(entity.getRequestJson(), RcaRequest.class));
            record.setResponse(objectMapper.readValue(entity.getResponseJson(), RcaResponse.class));
            record.setResultLevel(entity.getResultLevel());
            record.setRecorded(entity.isRecorded());
            record.setCreatedAt(entity.getCreatedAt());

            if (entity.isRecorded()) {
                recordingRepository.findByAnalysisId(analysisId).ifPresent(recEntity -> {
                    try {
                        Recording recording = new Recording();
                        recording.setRecordingId(recEntity.getRecordingId());
                        recording.setAnalysisId(recEntity.getAnalysisId());
                        recording.setCreatedAt(recEntity.getCreatedAt());
                        List<InteractionRecord> interactions = objectMapper.readValue(
                                recEntity.getInteractionsJson(), new TypeReference<>() {});
                        recording.setInteractions(interactions);
                        record.setRecording(recording);
                    } catch (Exception e) {
                        log.warn("Failed to deserialize recording for analysisId={}", analysisId, e);
                    }
                });
            }
            return record;
        } catch (Exception e) {
            log.error("Failed to load analysis record {}", analysisId, e);
            return null;
        }
    }

    public List<AnalysisRecord> list() {
        List<AnalysisRecord> result = new ArrayList<>();
        for (AnalysisRecordEntity entity : analysisRecordRepository.findAll()) {
            try {
                AnalysisRecord record = new AnalysisRecord();
                record.setAnalysisId(entity.getAnalysisId());
                record.setRequest(objectMapper.readValue(entity.getRequestJson(), RcaRequest.class));
                record.setResponse(objectMapper.readValue(entity.getResponseJson(), RcaResponse.class));
                record.setResultLevel(entity.getResultLevel());
                record.setRecorded(entity.isRecorded());
                record.setCreatedAt(entity.getCreatedAt());
                result.add(record);
            } catch (Exception e) {
                log.warn("Failed to deserialize analysis record {}", entity.getAnalysisId(), e);
            }
        }
        result.sort(Comparator.comparing(AnalysisRecord::getCreatedAt).reversed());
        return result;
    }

    @Transactional
    public boolean delete(String analysisId) {
        try {
            Optional<AnalysisRecordEntity> entityOpt = analysisRecordRepository.findById(analysisId);
            if (entityOpt.isEmpty()) {
                return false;
            }
            recordingRepository.findByAnalysisId(analysisId).ifPresent(recordingRepository::delete);
            analysisRecordRepository.deleteById(analysisId);
            log.debug("Deleted analysis record {} from H2", analysisId);
            return true;
        } catch (Exception e) {
            log.error("Failed to delete analysis record {}", analysisId, e);
            return false;
        }
    }

    public List<Recording> listRecordings() {
        List<Recording> result = new ArrayList<>();
        for (RecordingEntity entity : recordingRepository.findAll()) {
            try {
                Recording recording = new Recording();
                recording.setRecordingId(entity.getRecordingId());
                recording.setAnalysisId(entity.getAnalysisId());
                recording.setCreatedAt(entity.getCreatedAt());
                List<InteractionRecord> interactions = objectMapper.readValue(
                        entity.getInteractionsJson(), new TypeReference<>() {});
                recording.setInteractions(interactions);
                result.add(recording);
            } catch (Exception e) {
                log.warn("Failed to deserialize recording {}", entity.getRecordingId(), e);
            }
        }
        result.sort(Comparator.comparing(Recording::getCreatedAt).reversed());
        return result;
    }

    public Recording getRecording(String recordingId) {
        try {
            Optional<RecordingEntity> entityOpt = recordingRepository.findById(recordingId);
            if (entityOpt.isEmpty()) {
                return null;
            }
            RecordingEntity entity = entityOpt.get();
            Recording recording = new Recording();
            recording.setRecordingId(entity.getRecordingId());
            recording.setAnalysisId(entity.getAnalysisId());
            recording.setCreatedAt(entity.getCreatedAt());
            List<InteractionRecord> interactions = objectMapper.readValue(
                    entity.getInteractionsJson(), new TypeReference<>() {});
            recording.setInteractions(interactions);
            return recording;
        } catch (Exception e) {
            log.error("Failed to load recording {}", recordingId, e);
            return null;
        }
    }
}
