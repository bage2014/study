package com.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sample")
@Tag(name = "Sample Controller", description = "Sample endpoints for testing OpenAPI auto-reflection")
public class SampleController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get sample greeting", description = "Returns a simple greeting message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, String>> getGreeting() {
        return ResponseEntity.ok(Map.of("message", "Hello from SampleController!"));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create sample item", description = "Echoes back the provided name as a created item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Map<String, String>> createItem(@RequestBody Map<String, String> request) {
        String name = request.getOrDefault("name", "unknown");
        return ResponseEntity.ok(Map.of("created", name));
    }
}