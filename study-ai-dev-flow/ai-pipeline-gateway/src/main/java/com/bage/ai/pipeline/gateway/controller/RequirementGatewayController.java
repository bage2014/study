package com.bage.ai.pipeline.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/requirement")
@CrossOrigin(origins = "*")
public class RequirementGatewayController {

    private final WebClient webClient;

    public RequirementGatewayController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Map<String, Object>>> getRequirementsByProject(@PathVariable Long projectId) {
        try {
            List<Map<String, Object>> result = webClient.get()
                    .uri("/api/requirement/project/{projectId}", projectId)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to get requirements: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @GetMapping("/project/{projectId}/stats")
    public ResponseEntity<Map<String, Object>> getProjectRequirementStats(@PathVariable Long projectId) {
        try {
            Map<String, Object> result = webClient.get()
                    .uri("/api/requirement/project/{projectId}/stats", projectId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to get requirement stats: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @GetMapping("/project/{projectId}/{status}")
    public ResponseEntity<List<Map<String, Object>>> getRequirementsByProjectAndStatus(
            @PathVariable Long projectId,
            @PathVariable String status) {
        try {
            List<Map<String, Object>> result = webClient.get()
                    .uri("/api/requirement/project/{projectId}/{status}", projectId, status)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to get requirements by status: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getRequirement(@PathVariable Long id) {
        try {
            Map<String, Object> result = webClient.get()
                    .uri("/api/requirement/{id}", id)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (result == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to get requirement: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @GetMapping("/pipeline/{pipelineId}")
    public ResponseEntity<Map<String, Object>> getRequirementByPipelineId(@PathVariable String pipelineId) {
        try {
            Map<String, Object> result = webClient.get()
                    .uri("/api/requirement/pipeline/{pipelineId}", pipelineId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (result == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to get requirement by pipelineId: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createRequirement(@RequestBody Map<String, Object> body) {
        try {
            Map<String, Object> result = webClient.post()
                    .uri("/api/requirement")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (result == null) {
                return ResponseEntity.internalServerError().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to create requirement: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateRequirement(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            Map<String, Object> result = webClient.put()
                    .uri("/api/requirement/{id}", id)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (result == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to update requirement: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequirement(@PathVariable Long id) {
        try {
            webClient.delete()
                    .uri("/api/requirement/{id}", id)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Failed to delete requirement: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }
}