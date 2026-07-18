package com.bage.demo.controller;

import com.bage.demo.dto.MessageRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponse> createMessage(@Valid @RequestBody MessageRequest request) {
        log.debug("REST request to create message");
        MessageResponse response = messageService.createMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getMessageById(@PathVariable Long id) {
        log.debug("REST request to get message by id: {}", id);
        MessageResponse response = messageService.getMessageById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<MessageResponse>> getAllMessages(
            @PageableDefault(size = 20, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("REST request to get all messages with pageable: {}", pageable);
        Page<MessageResponse> page = messageService.getAllMessages(pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateMessage(
            @PathVariable Long id,
            @Valid @RequestBody MessageRequest request) {
        log.debug("REST request to update message with id: {}", id);
        MessageResponse response = messageService.updateMessage(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        log.debug("REST request to delete message with id: {}", id);
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
