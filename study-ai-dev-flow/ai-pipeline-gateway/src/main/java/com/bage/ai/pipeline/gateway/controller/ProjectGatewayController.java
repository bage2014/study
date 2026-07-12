package com.bage.ai.pipeline.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/project")
@CrossOrigin(origins = "*")
public class ProjectGatewayController {

    private final WebClient webClient;

    public ProjectGatewayController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        try {
            Map<String, Object> result = webClient.get()
                    .uri(uriBuilder -> {
                        var builder = uriBuilder.path("/api/project")
                                .queryParam("page", page)
                                .queryParam("size", size);
                        if (keyword != null && !keyword.trim().isEmpty()) {
                            builder.queryParam("keyword", keyword);
                        }
                        return builder.build();
                    })
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to get projects: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProject(@PathVariable Long id) {
        try {
            Map<String, Object> result = webClient.get()
                    .uri("/api/project/{id}", id)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (result == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to get project: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<Map<String, Object>> getProjectDetail(@PathVariable Long id) {
        try {
            Map<String, Object> result = webClient.get()
                    .uri("/api/project/{id}/detail", id)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (result == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to get project detail: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProject(@RequestBody Map<String, Object> body) {
        try {
            Map<String, Object> result = webClient.post()
                    .uri("/api/project")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (result == null) {
                return ResponseEntity.internalServerError().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to create project: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProject(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            Map<String, Object> result = webClient.put()
                    .uri("/api/project/{id}", id)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (result == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to update project: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        try {
            webClient.delete()
                    .uri("/api/project/{id}", id)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Failed to delete project: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getProjectStats() {
        try {
            Map<String, Object> result = webClient.get()
                    .uri("/api/project/stats")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to get project stats: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }
}
