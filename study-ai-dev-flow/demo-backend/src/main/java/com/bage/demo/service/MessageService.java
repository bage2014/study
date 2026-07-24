package com.bage.demo.service;

import com.bage.demo.dto.MessageCreateRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.MessageUpdateRequest;
import com.bage.demo.entity.Message;
import com.bage.demo.exception.ResourceNotFoundException;
import com.bage.demo.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public MessageResponse createMessage(MessageCreateRequest request) {
        log.debug("Creating message: {}", request);
        Message message = Message.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .sender(request.getSender())
                .receiver(request.getReceiver())
                .build();
        message = messageRepository.save(message);
        return toResponse(message);
    }

    public MessageResponse getMessageById(Long id) {
        log.debug("Fetching message by id: {}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", id));
        return toResponse(message);
    }

    public Page<MessageResponse> listMessages(String sender, String receiver, 
                                               LocalDateTime startTime, LocalDateTime endTime, 
                                               Pageable pageable) {
        log.debug("Listing messages - sender: {}, receiver: {}, startTime: {}, endTime: {}",
                sender, receiver, startTime, endTime);
        
        List<Message> messages;
        
        if (sender != null && receiver != null && startTime != null && endTime != null) {
            messages = messageRepository.findBySenderAndReceiverAndCreatedAtBetweenOrderByCreatedAtDesc(sender, receiver, startTime, endTime);
        } else if (sender != null && receiver != null) {
            messages = messageRepository.findBySenderAndReceiverOrderByCreatedAtDesc(sender, receiver);
        } else if (sender != null && startTime != null && endTime != null) {
            messages = messageRepository.findBySenderAndCreatedAtBetweenOrderByCreatedAtDesc(sender, startTime, endTime);
        } else if (receiver != null && startTime != null && endTime != null) {
            messages = messageRepository.findByReceiverAndCreatedAtBetweenOrderByCreatedAtDesc(receiver, startTime, endTime);
        } else if (startTime != null && endTime != null) {
            messages = messageRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startTime, endTime);
        } else if (sender != null) {
            messages = messageRepository.findBySenderOrderByCreatedAtDesc(sender);
        } else if (receiver != null) {
            messages = messageRepository.findByReceiverOrderByCreatedAtDesc(receiver);
        } else {
            messages = messageRepository.findAll();
        }

        List<MessageResponse> content = messages.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), content.size());

        if (start >= content.size()) {
            return new PageImpl<>(List.of(), pageable, content.size());
        }

        return new PageImpl<>(content.subList(start, end), pageable, content.size());
    }

    @Transactional
    public MessageResponse updateMessage(Long id, MessageUpdateRequest request) {
        log.debug("Updating message id: {} with: {}", id, request);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", id));
        
        message.setTitle(request.getTitle());
        message.setContent(request.getContent());
        message.setSender(request.getSender());
        message.setReceiver(request.getReceiver());
        
        message = messageRepository.save(message);
        return toResponse(message);
    }

    @Transactional
    public void deleteMessage(Long id) {
        log.debug("Deleting message id: {}", id);
        if (!messageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Message", "id", id);
        }
        messageRepository.deleteById(id);
    }

    @Transactional
    public void deleteMessages(List<Long> ids) {
        log.debug("Deleting messages with ids: {}", ids);
        List<Message> messages = messageRepository.findAllById(ids);
        if (messages.size() != ids.size()) {
            throw new RuntimeException("部分消息不存在，请检查ID");
        }
        messageRepository.deleteAllById(ids);
    }

    private MessageResponse toResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .title(message.getTitle())
                .content(message.getContent())
                .sender(message.getSender())
                .receiver(message.getReceiver())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }
}
