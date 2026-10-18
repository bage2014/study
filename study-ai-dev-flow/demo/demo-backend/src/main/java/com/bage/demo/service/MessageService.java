package com.bage.demo.service;

import com.bage.demo.dto.MessageCreateRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.MessageUpdateRequest;
import java.util.List;

/**
 * Service contract for message operations.
 */
public interface MessageService {

    MessageResponse createMessage(MessageCreateRequest request);

    MessageResponse updateMessage(Long id, MessageUpdateRequest request);

    List<MessageResponse> getAllMessages();
}
