package com.bage.study.ai.best.practice.exception.analysis.controller;

import com.bage.study.ai.best.practice.exception.analysis.dto.request.AnalysisRequest;
import com.bage.study.ai.best.practice.exception.analysis.dto.response.AnalysisResponse;
import com.bage.study.ai.best.practice.exception.analysis.service.AnalysisService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResponse> analyze(@Valid @RequestBody AnalysisRequest request) {
        AnalysisResponse response = analysisService.analyze(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "exception-analysis");
        return ResponseEntity.ok(health);
    }
}
