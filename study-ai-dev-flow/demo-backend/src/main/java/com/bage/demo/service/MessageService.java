package com.bage.demo.service;

import com.bage.demo.dto.MessageCreateRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.MessageUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {

    MessageResponse createMessage(MessageCreateRequest request);

    MessageResponse getMessage(Long id);

    Page<MessageResponse> getAllMessages(Pageable pageable);

    MessageResponse updateMessage(Long id, MessageUpdateRequest request);

    void deleteMessage(Long id);
}
