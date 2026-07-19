package com.bage.demo.service;

import com.bage.demo.dto.CreateMessageRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.UpdateMessageRequest;
import com.bage.demo.entity.Message;
import com.bage.demo.exception.ResourceNotFoundException;
import com.bage.demo.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public MessageResponse createMessage(CreateMessageRequest request) {
        log.info("Creating message from sender: {} to receiver: {}", request.getSender(), request.getReceiver());
        
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        
        Message message = Message.builder()
                .content(request.getContent())
                .sender(request.getSender() != null ? request.getSender() : "unknown")
                .receiver(request.getReceiver() != null ? request.getReceiver() : "unknown")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        message = messageRepository.save(message);
        return convertToResponse(message);
    }

    public Page<MessageResponse> getAllMessages(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than one");
        }
        
        Pageable pageable = PageRequest.of(page, size);
        return messageRepository.findAll(pageable).map(this::convertToResponse);
    }

    public MessageResponse getMessageById(Long id) {
        log.info("Fetching message by id: {}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        return convertToResponse(message);
    }

    @Transactional
    public MessageResponse updateMessage(Long id, UpdateMessageRequest request) {
        log.info("Updating message with id: {}", id);
        Message existingMessage = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        
        if (request.getContent() != null) {
            existingMessage.setContent(request.getContent());
        }
        if (request.getSender() != null) {
            existingMessage.setSender(request.getSender());
        }
        if (request.getReceiver() != null) {
            existingMessage.setReceiver(request.getReceiver());
        }
        existingMessage.setUpdatedAt(LocalDateTime.now());
        existingMessage = messageRepository.save(existingMessage);
        return convertToResponse(existingMessage);
    }

    @Transactional
    public void deleteMessage(Long id) {
        log.info("Deleting message with id: {}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        messageRepository.delete(message);
    }

    private MessageResponse convertToResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .sender(message.getSender())
                .receiver(message.getReceiver())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }
}