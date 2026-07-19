package com.bage.demo.service;

import com.bage.demo.dto.MessageCreateRequest;
import com.bage.demo.dto.MessageDTO;
import com.bage.demo.dto.MessagePageResponse;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.MessageUpdateRequest;
import com.bage.demo.entity.Message;
import com.bage.demo.exception.ResourceNotFoundException;
import com.bage.demo.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    public MessageResponse createMessage(MessageCreateRequest request) {
        log.info("Creating message from sender: {} to receiver: {}", request.getSender(), request.getReceiver());
        Message message = Message.builder()
                .content(request.getContent())
                .sender(request.getSender())
                .receiver(request.getReceiver())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        message = messageRepository.save(message);
        return convertToResponse(message);
    }

    public MessagePageResponse listMessages(String sender, String receiver, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        log.info("Fetching messages with filters - sender: {}, receiver: {}, startDate: {}, endDate: {}", sender, receiver, startDate, endDate);
        Page<Message> messages;
        if (sender != null && receiver != null && startDate != null && endDate != null) {
            messages = messageRepository.findBySenderAndReceiverAndCreatedAtBetween(sender, receiver, startDate, endDate, pageable);
        } else if (sender != null && receiver != null) {
            messages = messageRepository.findBySenderAndReceiver(sender, receiver, pageable);
        } else if (sender != null) {
            messages = messageRepository.findBySender(sender, pageable);
        } else if (receiver != null) {
            messages = messageRepository.findByReceiver(receiver, pageable);
        } else if (startDate != null && endDate != null) {
            messages = messageRepository.findByCreatedAtBetween(startDate, endDate, pageable);
        } else {
            messages = messageRepository.findAll(pageable);
        }
        
        return MessagePageResponse.builder()
                .content(messages.getContent().stream().map(this::convertToResponse).toList())
                .page(messages.getNumber())
                .size(messages.getSize())
                .totalElements(messages.getTotalElements())
                .totalPages(messages.getTotalPages())
                .first(messages.isFirst())
                .last(messages.isLast())
                .build();
    }

    public MessageResponse getMessageById(Long id) {
        log.info("Fetching message by id: {}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        return convertToResponse(message);
    }

    @Transactional
    public MessageResponse updateMessage(Long id, MessageUpdateRequest request) {
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
        if (!messageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Message not found with id: " + id);
        }
        messageRepository.deleteById(id);
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

    private MessageDTO convertToDTO(Message message) {
        return MessageDTO.builder()
                .id(message.getId())
                .content(message.getContent())
                .sender(message.getSender())
                .receiver(message.getReceiver())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }
}