package com.bage.demo.service;

import com.bage.demo.dto.MessageRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.MessageUpdateRequest;
import com.bage.demo.dto.PageResponse;
import com.bage.demo.entity.Message;
import com.bage.demo.exception.ResourceNotFoundException;
import com.bage.demo.exception.UnauthorizedException;
import com.bage.demo.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    /**
     * Create a new message.
     *
     * @param request the message creation request
     * @return the created message response
     */
    @Transactional
    public MessageResponse createMessage(MessageRequest request) {
        log.debug("Creating message from sender: {}", request.getSender());

        Message message = Message.builder()
                .content(request.getContent())
                .sender(request.getSender())
                .timestamp(request.getTimestamp())
                .deleted(false)
                .build();

        Message savedMessage = messageRepository.save(message);
        log.info("Message created with id: {}", savedMessage.getId());

        return mapToResponse(savedMessage);
    }

    /**
     * Get a message by its ID.
     *
     * @param id the message ID
     * @return the message response
     * @throws ResourceNotFoundException if the message is not found or is deleted
     */
    @Transactional(readOnly = true)
    public MessageResponse getMessageById(Long id) {
        log.debug("Fetching message by id: {}", id);

        Message message = messageRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", id));

        return mapToResponse(message);
    }

    /**
     * Get all non-deleted messages with optional filtering and pagination.
     *
     * @param page   the page number (0-based)
     * @param size   the page size
     * @param sender optional sender filter
     * @param start  optional start date for range filter
     * @param end    optional end date for range filter
     * @return a paginated response of messages
     */
    @Transactional(readOnly = true)
    public PageResponse<MessageResponse> getAllMessages(int page, int size, String sender,
                                                         LocalDateTime start, LocalDateTime end) {
        log.debug("Fetching messages - page: {}, size: {}, sender: {}, start: {}, end: {}",
                page, size, sender, start, end);

        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messagePage;

        if (sender != null && start != null && end != null) {
            // Filter by sender and date range
            List<Message> messages = messageRepository.findBySenderAndTimestampBetweenAndDeletedFalse(sender, start, end);
            return buildPageResponseFromList(messages, page, size);
        } else if (start != null && end != null) {
            // Filter by date range only
            List<Message> messages = messageRepository.findByTimestampBetweenAndDeletedFalse(start, end);
            return buildPageResponseFromList(messages, page, size);
        } else if (sender != null) {
            // Filter by sender only
            messagePage = messageRepository.findBySenderAndDeletedFalse(sender, pageable);
        } else {
            // No filters
            messagePage = messageRepository.findByDeletedFalse(pageable);
        }

        return mapToPageResponse(messagePage);
    }

    /**
     * Update an existing message.
     *
     * @param id      the message ID
     * @param request the update request with fields to modify
     * @param username the username of the requester for authorization
     * @return the updated message response
     * @throws ResourceNotFoundException if the message is not found
     * @throws UnauthorizedException if the requester is not the sender
     */
    @Transactional
    public MessageResponse updateMessage(Long id, MessageUpdateRequest request, String username) {
        log.debug("Updating message id: {} by user: {}", id, username);

        Message message = messageRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", id));

        // Authorization: only the sender can update the message
        if (!message.getSender().equals(username)) {
            log.warn("Unauthorized update attempt by user: {} on message id: {}", username, id);
            throw new UnauthorizedException("You are not authorized to update this message");
        }

        if (request.getContent() != null) {
            message.setContent(request.getContent());
        }
        if (request.getSender() != null) {
            message.setSender(request.getSender());
        }
        message.setUpdatedAt(LocalDateTime.now());

        Message updatedMessage = messageRepository.save(message);
        log.info("Message updated with id: {}", updatedMessage.getId());

        return mapToResponse(updatedMessage);
    }

    /**
     * Soft delete a message by its ID.
     *
     * @param id      the message ID
     * @param username the username of the requester for authorization
     * @throws ResourceNotFoundException if the message is not found
     * @throws UnauthorizedException if the requester is not the sender
     */
    @Transactional
    public void deleteMessage(Long id, String username) {
        log.debug("Soft deleting message id: {} by user: {}", id, username);

        Message message = messageRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", id));

        // Authorization: only the sender can delete the message
        if (!message.getSender().equals(username)) {
            log.warn("Unauthorized delete attempt by user: {} on message id: {}", username, id);
            throw new UnauthorizedException("You are not authorized to delete this message");
        }

        message.setDeleted(true);
        message.setUpdatedAt(LocalDateTime.now());
        messageRepository.save(message);
        log.info("Message soft deleted with id: {}", id);
    }

    /**
     * Map a Message entity to a MessageResponse DTO.
     */
    private MessageResponse mapToResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .sender(message.getSender())
                .timestamp(message.getTimestamp())
                .updatedAt(message.getUpdatedAt())
                .build();
    }

    /**
     * Map a Page of Message entities to a PageResponse of MessageResponse DTOs.
     */
    private PageResponse<MessageResponse> mapToPageResponse(Page<Message> messagePage) {
        List<MessageResponse> content = messagePage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return PageResponse.<MessageResponse>builder()
                .content(content)
                .page(messagePage.getNumber())
                .size(messagePage.getSize())
                .totalElements(messagePage.getTotalElements())
                .totalPages(messagePage.getTotalPages())
                .last(messagePage.isLast())
                .build();
    }

    /**
     * Build a PageResponse from a list (used when filtering by date range).
     */
    private PageResponse<MessageResponse> buildPageResponseFromList(List<Message> messages, int page, int size) {
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, messages.size());

        List<Message> pageContent;
        if (startIndex >= messages.size()) {
            pageContent = List.of();
        } else {
            pageContent = messages.subList(startIndex, endIndex);
        }

        List<MessageResponse> content = pageContent.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        int totalPages = (int) Math.ceil((double) messages.size() / size);

        return PageResponse.<MessageResponse>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(messages.size())
                .totalPages(totalPages)
                .last(endIndex >= messages.size())
                .build();
    }
}
