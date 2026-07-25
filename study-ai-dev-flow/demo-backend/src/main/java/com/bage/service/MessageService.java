package com.bage.demo.service;

import com.bage.demo.dto.MessageCreateRequest;
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
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public Message createMessage(MessageCreateRequest request) {
        Message message = Message.builder()
                .content(request.getContent())
                .sender(request.getSender())
                .receiver(request.getReceiver())
                .build();
        Message saved = messageRepository.save(message);
        log.info("Created message with id: {}", saved.getId());
        return saved;
    }

    @Transactional(readOnly = true)
    public Message getMessageById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("消息不存在，id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Message> getMessages(String sender, String receiver, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        boolean hasSender = sender != null && !sender.isBlank();
        boolean hasReceiver = receiver != null && !receiver.isBlank();
        boolean hasStartTime = startTime != null;
        boolean hasEndTime = endTime != null;

        if (hasSender && hasReceiver && hasStartTime && hasEndTime) {
            return messageRepository.findBySenderAndReceiverAndCreatedAtBetween(sender, receiver, startTime, endTime, pageable);
        } else if (hasSender && hasReceiver) {
            return messageRepository.findBySenderAndReceiver(sender, receiver, pageable);
        } else if (hasSender && hasStartTime && hasEndTime) {
            return messageRepository.findBySenderAndCreatedAtBetween(sender, startTime, endTime, pageable);
        } else if (hasReceiver && hasStartTime && hasEndTime) {
            return messageRepository.findByReceiverAndCreatedAtBetween(receiver, startTime, endTime, pageable);
        } else if (hasSender) {
            return messageRepository.findBySender(sender, pageable);
        } else if (hasReceiver) {
            return messageRepository.findByReceiver(receiver, pageable);
        } else if (hasStartTime && hasEndTime) {
            return messageRepository.findByCreatedAtBetween(startTime, endTime, pageable);
        } else {
            return messageRepository.findAll(pageable);
        }
    }

    @Transactional
    public Message updateMessage(Long id, MessageUpdateRequest request) {
        Message message = getMessageById(id);
        if (request.getContent() != null && !request.getContent().isBlank()) {
            message.setContent(request.getContent());
        }
        if (request.getRead() != null) {
            message.setRead(request.getRead());
        }
        Message saved = messageRepository.save(message);
        log.info("Updated message with id: {}", saved.getId());
        return saved;
    }

    @Transactional
    public void deleteMessage(Long id) {
        Message message = getMessageById(id);
        messageRepository.delete(message);
        log.info("Deleted message with id: {}", id);
    }
}
