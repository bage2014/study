package com.familytree.service;

import com.familytree.model.OperationLog;
import com.familytree.repository.OperationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OperationLogService {
    @Autowired
    private OperationLogRepository operationLogRepository;

    public List<OperationLog> getAllLogs() {
        return operationLogRepository.findAll();
    }

    public List<OperationLog> getLogsByOperatorId(Long operatorId) {
        return operationLogRepository.findByOperatorId(operatorId);
    }

    public List<OperationLog> getLogsByOperationType(String operationType) {
        return operationLogRepository.findByOperationType(operationType);
    }

    public List<OperationLog> getLogsByTimeRange(LocalDateTime startDate, LocalDateTime endDate) {
        return operationLogRepository.findByOperationTimeBetween(startDate, endDate);
    }

    public List<OperationLog> getLogsByRelatedDataId(Long relatedDataId) {
        return operationLogRepository.findByRelatedDataId(relatedDataId);
    }

    public OperationLog createLog(OperationLog log) {
        return operationLogRepository.save(log);
    }

    public void deleteLog(Long id) {
        operationLogRepository.deleteById(id);
    }
}
