package com.bage.demo.service.impl;

import com.bage.demo.dto.MessageCreateRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.MessageUpdateRequest;
import com.bage.demo.entity.Message;
import com.bage.demo.repository.MessageRepository;
import com.bage.demo.service.MessageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default {@link MessageService} implementation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public MessageResponse createMessage(MessageCreateRequest request) {
        Message message = Message.builder()
                .content(request.getContent())
                .sender(request.getSender())
                .receiver(request.getReceiver())
                .build();
        Message saved = messageRepository.save(message);
        log.info("Created message with id={}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MessageResponse getMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message not found: " + id));
        return toResponse(message);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageResponse> getAllMessages(Pageable pageable) {
        return messageRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Transactional
    public MessageResponse updateMessage(Long id, MessageUpdateRequest request) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message not found: " + id));
        if (request.getContent() != null) {
            message.setContent(request.getContent());
        }
        if (request.getRead() != null) {
            message.setRead(request.getRead());
        }
        Message saved = messageRepository.save(message);
        log.info("Updated message with id={}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteMessage(Long id) {
        if (!messageRepository.existsById(id)) {
            throw new EntityNotFoundException("Message not found: " + id);
        }
        messageRepository.deleteById(id);
        log.info("Deleted message with id={}", id);
    }

    private MessageResponse toResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .sender(message.getSender())
                .receiver(message.getReceiver())
                .read(message.getRead())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }
}
