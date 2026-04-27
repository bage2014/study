package com.bage.study.ai.best.practice.hello.controller;

import com.bage.study.ai.best.practice.hello.model.ChatRequest;
import com.bage.study.ai.best.practice.hello.model.ChatResponse;
import com.bage.study.ai.best.practice.hello.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        ChatResponse response = chatService.chat(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/models")
    public ResponseEntity<Map<String, Object>> getAvailableModels() {
        return ResponseEntity.ok(Map.of(
            "models", chatService.getProviderManager().getAllProviders().keySet(),
            "status", "available"
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
