package com.familytree.repository;

import com.familytree.model.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {
    List<OperationLog> findByOperatorId(Long operatorId);
    List<OperationLog> findByOperationType(String operationType);
    List<OperationLog> findByOperationTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<OperationLog> findByRelatedDataId(Long relatedDataId);
}
