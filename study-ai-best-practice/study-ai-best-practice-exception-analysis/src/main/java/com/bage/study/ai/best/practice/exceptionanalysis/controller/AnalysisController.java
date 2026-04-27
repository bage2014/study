package com.bage.study.ai.best.practice.exceptionanalysis.controller;

import com.bage.study.ai.best.practice.exceptionanalysis.model.ProblemAnalysisRequest;
import com.bage.study.ai.best.practice.exceptionanalysis.model.ProblemAnalysisResponse;
import com.bage.study.ai.best.practice.exceptionanalysis.service.AnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<ProblemAnalysisResponse> analyze(@RequestBody ProblemAnalysisRequest request) {
        ProblemAnalysisResponse response = analysisService.analyze(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/reanalyze/{analysisId}")
    public ResponseEntity<ProblemAnalysisResponse> reanalyze(
            @PathVariable String analysisId, 
            @RequestBody Map<String, String> feedback) {
        ProblemAnalysisResponse response = analysisService.reanalyze(
            analysisId, feedback.getOrDefault("feedback", "")
        );
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/analysis/{analysisId}")
    public ResponseEntity<ProblemAnalysisResponse> getAnalysis(@PathVariable String analysisId) {
        ProblemAnalysisResponse response = analysisService.getAnalysis(analysisId);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<Map<String, ProblemAnalysisResponse>> getRecentAnalyses() {
        return ResponseEntity.ok(analysisService.getRecentAnalyses());
    }

    @DeleteMapping("/cache/clear")
    public ResponseEntity<Void> clearCache() {
        analysisService.clearCache();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
