package com.familytree.controller;

import com.familytree.model.OperationLog;
import com.familytree.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/logs")
public class OperationLogController {
    @Autowired
    private OperationLogService operationLogService;

    @GetMapping
    public ResponseEntity<List<OperationLog>> getAllLogs() {
        List<OperationLog> logs = operationLogService.getAllLogs();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/operator/{operatorId}")
    public ResponseEntity<List<OperationLog>> getLogsByOperatorId(@PathVariable Long operatorId) {
        List<OperationLog> logs = operationLogService.getLogsByOperatorId(operatorId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/type/{operationType}")
    public ResponseEntity<List<OperationLog>> getLogsByOperationType(@PathVariable String operationType) {
        List<OperationLog> logs = operationLogService.getLogsByOperationType(operationType);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/time-range")
    public ResponseEntity<List<OperationLog>> getLogsByTimeRange(
            @RequestParam LocalDateTime startDate, 
            @RequestParam LocalDateTime endDate) {
        List<OperationLog> logs = operationLogService.getLogsByTimeRange(startDate, endDate);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/related/{relatedDataId}")
    public ResponseEntity<List<OperationLog>> getLogsByRelatedDataId(@PathVariable Long relatedDataId) {
        List<OperationLog> logs = operationLogService.getLogsByRelatedDataId(relatedDataId);
        return ResponseEntity.ok(logs);
    }

    @PostMapping
    public ResponseEntity<OperationLog> createLog(@RequestBody OperationLog log) {
        OperationLog createdLog = operationLogService.createLog(log);
        return ResponseEntity.ok(createdLog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        operationLogService.deleteLog(id);
        return ResponseEntity.ok().build();
    }
}
