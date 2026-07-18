package com.bage.demo.service;

import com.bage.demo.dto.MessageRequest;
import com.bage.demo.dto.MessageResponse;
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
    public MessageResponse createMessage(MessageRequest request) {
        log.info("Creating message with content: {}", request.getContent());
        Message message = Message.builder()
                .content(request.getContent())
                .timestamp(request.getTimestamp() != null ? request.getTimestamp() : LocalDateTime.now())
                .build();
        Message savedMessage = messageRepository.save(message);
        log.info("Message created with id: {}", savedMessage.getId());
        return toResponse(savedMessage);
    }

    public MessageResponse getMessageById(Long id) {
        log.info("Fetching message with id: {}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        return toResponse(message);
    }

    public Page<MessageResponse> getAllMessages(Pageable pageable) {
        log.info("Fetching all messages with pagination");
        return messageRepository.findAll(pageable).map(this::toResponse);
    }

    @Transactional
    public MessageResponse updateMessage(Long id, MessageRequest request) {
        log.info("Updating message with id: {}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        message.setContent(request.getContent());
        if (request.getTimestamp() != null) {
            message.setTimestamp(request.getTimestamp());
        }
        Message updatedMessage = messageRepository.save(message);
        log.info("Message updated with id: {}", updatedMessage.getId());
        return toResponse(updatedMessage);
    }

    @Transactional
    public void deleteMessage(Long id) {
        log.info("Deleting message with id: {}", id);
        if (!messageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Message not found with id: " + id);
        }
        messageRepository.deleteById(id);
        log.info("Message deleted with id: {}", id);
    }

    private MessageResponse toResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .build();
    }
}