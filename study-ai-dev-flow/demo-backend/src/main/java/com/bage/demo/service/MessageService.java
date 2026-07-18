package com.bage.demo.service;

import com.bage.demo.dto.MessageRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.entity.Message;
import com.bage.demo.exception.DuplicateResourceException;
import com.bage.demo.exception.ResourceNotFoundException;
import com.bage.demo.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public MessageResponse createMessage(MessageRequest request) {
        log.debug("Creating message with content: {}", request.getContent());

        Message message = Message.builder()
                .content(request.getContent())
                .timestamp(request.getTimestamp())
                .build();

        Message savedMessage = messageRepository.save(message);
        log.info("Message created with id: {}", savedMessage.getId());

        return mapToResponse(savedMessage);
    }

    public MessageResponse getMessageById(Long id) {
        log.debug("Fetching message by id: {}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", id));
        return mapToResponse(message);
    }

    public Page<MessageResponse> getAllMessages(Pageable pageable) {
        log.debug("Fetching all messages with pageable: {}", pageable);
        return messageRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Transactional
    public MessageResponse updateMessage(Long id, MessageRequest request) {
        log.debug("Updating message with id: {}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", id));

        message.setContent(request.getContent());
        message.setTimestamp(request.getTimestamp());

        Message updatedMessage = messageRepository.save(message);
        log.info("Message updated with id: {}", updatedMessage.getId());

        return mapToResponse(updatedMessage);
    }

    @Transactional
    public void deleteMessage(Long id) {
        log.debug("Deleting message with id: {}", id);
        if (!messageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Message", "id", id);
        }
        messageRepository.deleteById(id);
        log.info("Message deleted with id: {}", id);
    }

    private MessageResponse mapToResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .build();
    }
}
