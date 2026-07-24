package com.bage.demo.controller;

import com.bage.demo.dto.MessageRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.MessageUpdateRequest;
import com.bage.demo.dto.PageResponse;
import com.bage.demo.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponse> createMessage(@Valid @RequestBody MessageRequest request) {
        log.debug("REST request to create a new message: {}", request);
        MessageResponse createdMessage = messageService.createMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getMessage(@PathVariable Long id) {
        log.debug("REST request to get message with id: {}", id);
        MessageResponse messageDTO = messageService.getMessageById(id);
        return ResponseEntity.ok(messageDTO);
    }

    @GetMapping
    public ResponseEntity<PageResponse<MessageResponse>> getAllMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sender,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        log.debug("REST request to get all messages - page: {}, size: {}, sender: {}, start: {}, end: {}",
                page, size, sender, start, end);
        PageResponse<MessageResponse> messages = messageService.getAllMessages(page, size, sender, start, end);
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateMessage(
            @PathVariable Long id,
            @Valid @RequestBody MessageUpdateRequest request,
            @RequestHeader(value = "X-Username", defaultValue = "defaultUser") String username) {
        log.debug("REST request to update message with id: {} by user: {}", id, username);
        MessageResponse updatedMessage = messageService.updateMessage(id, request, username);
        return ResponseEntity.ok(updatedMessage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long id,
            @RequestHeader(value = "X-Username", defaultValue = "defaultUser") String username) {
        log.debug("REST request to delete message with id: {} by user: {}", id, username);
        messageService.deleteMessage(id, username);
        return ResponseEntity.noContent().build();
    }
}
