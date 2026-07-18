package com.bage.demo.service;

import com.bage.demo.dto.MessageQueryDTO;
import com.bage.demo.entity.Message;
import com.bage.demo.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public Message createMessage(Message message) {
        log.info("Creating new message: {}", message);
        message.setId(null);
        message.setCreatedAt(LocalDateTime.now());
        Message saved = messageRepository.save(message);
        log.info("Message created successfully with id: {}", saved.getId());
        return saved;
    }

    @Override
    public Optional<Message> getMessageById(Long id) {
        log.debug("Fetching message by id: {}", id);
        return messageRepository.findById(id);
    }

    @Override
    public Page<Message> listMessages(MessageQueryDTO queryDTO) {
        log.debug("Listing messages with query: {}", queryDTO);
        Pageable pageable = PageRequest.of(
                queryDTO.getPage() != null ? queryDTO.getPage() : 0,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10
        );
        Specification<Message> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (queryDTO.getSender() != null && !queryDTO.getSender().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("sender"), queryDTO.getSender()));
            }
            if (queryDTO.getReceiver() != null && !queryDTO.getReceiver().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("receiver"), queryDTO.getReceiver()));
            }
            if (queryDTO.getMessageType() != null && !queryDTO.getMessageType().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("messageType"), queryDTO.getMessageType()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return messageRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    public Message updateMessage(Long id, Message message) {
        log.info("Updating message with id: {}", id);
        Message existing = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
        existing.setContent(message.getContent());
        existing.setSender(message.getSender());
        existing.setReceiver(message.getReceiver());
        existing.setMessageType(message.getMessageType());
        existing.setStatus(message.getStatus());
        Message updated = messageRepository.save(existing);
        log.info("Message updated successfully: {}", updated);
        return updated;
    }

    @Override
    @Transactional
    public Message updateMessageStatus(Long id, String status) {
        log.info("Updating message status for id: {} to {}", id, status);
        Message existing = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
        existing.setStatus(status);
        Message updated = messageRepository.save(existing);
        log.info("Message status updated successfully: {}", updated);
        return updated;
    }

    @Override
    @Transactional
    public void deleteMessage(Long id) {
        log.info("Deleting message with id: {}", id);
        if (!messageRepository.existsById(id)) {
            throw new RuntimeException("Message not found with id: " + id);
        }
        messageRepository.deleteById(id);
        log.info("Message deleted successfully with id: {}", id);
    }

    @Override
    @Transactional
    public int deleteMessages(List<Long> ids) {
        log.info("Batch deleting messages with ids: {}", ids);
        List<Message> messages = messageRepository.findAllById(ids);
        if (messages.isEmpty()) {
            throw new RuntimeException("No messages found for ids: " + ids);
        }
        messageRepository.deleteAll(messages);
        log.info("Batch deleted {} messages", messages.size());
        return messages.size();
    }
}