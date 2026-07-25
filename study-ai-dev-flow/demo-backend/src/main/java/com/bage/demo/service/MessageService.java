package com.bage.demo.service;

import com.bage.demo.dto.MessageCreateRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.MessageUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {

    MessageResponse createMessage(MessageCreateRequest request);

    MessageResponse getMessageById(Long id);

    Page<MessageResponse> getMessages(String sender, String receiver, Pageable pageable);

    MessageResponse updateMessage(Long id, MessageUpdateRequest request);

    void deleteMessage(Long id);
}