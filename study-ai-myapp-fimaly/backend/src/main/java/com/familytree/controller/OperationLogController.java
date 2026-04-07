package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.OperationLog;
import com.familytree.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class OperationLogController {
    @Autowired
    private OperationLogService operationLogService;

    @GetMapping
    public ApiResponse<List<OperationLog>> getAllLogs() {
        try {
            List<OperationLog> logs = operationLogService.getAllLogs();
            return ApiResponse.success(logs);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/operator/{operatorId}")
    public ApiResponse<List<OperationLog>> getLogsByOperatorId(@PathVariable Long operatorId) {
        try {
            List<OperationLog> logs = operationLogService.getLogsByOperatorId(operatorId);
            return ApiResponse.success(logs);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/type/{operationType}")
    public ApiResponse<List<OperationLog>> getLogsByOperationType(@PathVariable String operationType) {
        try {
            List<OperationLog> logs = operationLogService.getLogsByOperationType(operationType);
            return ApiResponse.success(logs);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/time-range")
    public ApiResponse<List<OperationLog>> getLogsByTimeRange(
            @RequestParam LocalDateTime startDate, 
            @RequestParam LocalDateTime endDate) {
        try {
            List<OperationLog> logs = operationLogService.getLogsByTimeRange(startDate, endDate);
            return ApiResponse.success(logs);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/related/{relatedDataId}")
    public ApiResponse<List<OperationLog>> getLogsByRelatedDataId(@PathVariable Long relatedDataId) {
        try {
            List<OperationLog> logs = operationLogService.getLogsByRelatedDataId(relatedDataId);
            return ApiResponse.success(logs);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping
    public ApiResponse<OperationLog> createLog(@RequestBody OperationLog log) {
        try {
            OperationLog createdLog = operationLogService.createLog(log);
            return ApiResponse.success(createdLog);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteLog(@PathVariable Long id) {
        try {
            operationLogService.deleteLog(id);
            return ApiResponse.success(null, "Log deleted successfully");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
