package com.bage.demo.controller;

import com.bage.demo.dto.ApiResponse;
import com.bage.demo.dto.MessageRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<ApiResponse<MessageResponse>> createMessage(@Valid @RequestBody MessageRequest request) {
        log.info("Received create message request");
        MessageResponse response = messageService.createMessage(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> getMessageById(@PathVariable Long id) {
        log.info("Received get message request for id: {}", id);
        MessageResponse response = messageService.getMessageById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MessageResponse>>> getAllMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Received get all messages request - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<MessageResponse> response = messageService.getAllMessages(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> updateMessage(
            @PathVariable Long id,
            @Valid @RequestBody MessageRequest request) {
        log.info("Received update message request for id: {}", id);
        MessageResponse response = messageService.updateMessage(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable Long id) {
        log.info("Received delete message request for id: {}", id);
        messageService.deleteMessage(id);
        return ResponseEntity.ok(ApiResponse.success());
    }
}