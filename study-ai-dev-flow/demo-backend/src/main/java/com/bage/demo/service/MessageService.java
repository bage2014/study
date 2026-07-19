package com.bage.demo.service;

import com.bage.demo.dto.MessageDTO;
import com.bage.demo.entity.Message;
import com.bage.demo.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public MessageDTO createMessage(MessageDTO messageDTO) {
        log.info("Creating message from sender: {} to receiver: {}", messageDTO.getSender(), messageDTO.getReceiver());
        Message message = new Message();
        message.setContent(messageDTO.getContent());
        message.setSender(messageDTO.getSender());
        message.setReceiver(messageDTO.getReceiver());
        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());
        message = messageRepository.save(message);
        return convertToDTO(message);
    }

    public Page<MessageDTO> getMessages(String sender, String receiver, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
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
        return messages.map(this::convertToDTO);
    }

    public MessageDTO getMessageById(Long id) {
        log.info("Fetching message by id: {}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
        return convertToDTO(message);
    }

    @Transactional
    public MessageDTO updateMessage(Long id, MessageDTO messageDTO) {
        log.info("Updating message with id: {}", id);
        Message existingMessage = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
        if (messageDTO.getContent() != null) {
            existingMessage.setContent(messageDTO.getContent());
        }
        if (messageDTO.getSender() != null) {
            existingMessage.setSender(messageDTO.getSender());
        }
        if (messageDTO.getReceiver() != null) {
            existingMessage.setReceiver(messageDTO.getReceiver());
        }
        existingMessage.setUpdatedAt(LocalDateTime.now());
        existingMessage = messageRepository.save(existingMessage);
        return convertToDTO(existingMessage);
    }

    @Transactional
    public void deleteMessage(Long id) {
        log.info("Deleting message with id: {}", id);
        if (!messageRepository.existsById(id)) {
            throw new RuntimeException("Message not found with id: " + id);
        }
        messageRepository.deleteById(id);
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