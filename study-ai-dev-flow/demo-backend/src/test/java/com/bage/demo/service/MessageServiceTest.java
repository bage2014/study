package com.bage.demo.service;

import com.bage.demo.dto.CreateMessageRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.UpdateMessageRequest;
import com.bage.demo.entity.Message;
import com.bage.demo.exception.ResourceNotFoundException;
import com.bage.demo.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    @Test
    void createMessage_ShouldReturnMessageResponse() {
        CreateMessageRequest request = new CreateMessageRequest();
        request.setContent("Hello");

        Message savedMessage = new Message();
        savedMessage.setId(1L);
        savedMessage.setContent("Hello");

        given(messageRepository.save(any(Message.class))).willReturn(savedMessage);

        MessageResponse response = messageService.createMessage(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getContent()).isEqualTo("Hello");
    }

    @Test
    void getAllMessages_ShouldReturnPage() {
        Message msg1 = new Message();
        msg1.setId(1L);
        msg1.setContent("Hello");

        Message msg2 = new Message();
        msg2.setId(2L);
        msg2.setContent("World");

        Page<Message> page = new PageImpl<>(List.of(msg1, msg2));
        given(messageRepository.findAll(any(PageRequest.class))).willReturn(page);

        Page<MessageResponse> result = messageService.getAllMessages(0, 10);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
        assertThat(result.getContent().get(1).getContent()).isEqualTo("World");
    }

    @Test
    void getMessageById_WhenExists_ShouldReturnMessageResponse() {
        Message message = new Message();
        message.setId(1L);
        message.setContent("Hello");

        given(messageRepository.findById(1L)).willReturn(Optional.of(message));

        MessageResponse response = messageService.getMessageById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getContent()).isEqualTo("Hello");
    }

    @Test
    void getMessageById_WhenNotExists_ShouldThrowResourceNotFoundException() {
        given(messageRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.getMessageById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Message not found with id: 999");
    }

    @Test
    void updateMessage_WhenExists_ShouldReturnUpdatedMessageResponse() {
        Message existingMessage = new Message();
        existingMessage.setId(1L);
        existingMessage.setContent("Old");

        UpdateMessageRequest request = new UpdateMessageRequest();
        request.setContent("Updated");

        Message updatedMessage = new Message();
        updatedMessage.setId(1L);
        updatedMessage.setContent("Updated");

        given(messageRepository.findById(1L)).willReturn(Optional.of(existingMessage));
        given(messageRepository.save(any(Message.class))).willReturn(updatedMessage);

        MessageResponse response = messageService.updateMessage(1L, request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getContent()).isEqualTo("Updated");
    }

    @Test
    void updateMessage_WhenNotExists_ShouldThrowResourceNotFoundException() {
        UpdateMessageRequest request = new UpdateMessageRequest();
        request.setContent("Updated");

        given(messageRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.updateMessage(999L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Message not found with id: 999");
    }

    @Test
    void deleteMessage_WhenExists_ShouldDelete() {
        Message message = new Message();
        message.setId(1L);
        message.setContent("Hello");

        given(messageRepository.findById(1L)).willReturn(Optional.of(message));
        willDoNothing().given(messageRepository).delete(message);

        messageService.deleteMessage(1L);

        verify(messageRepository).findById(1L);
        verify(messageRepository).delete(message);
    }

    @Test
    void deleteMessage_WhenNotExists_ShouldThrowResourceNotFoundException() {
        given(messageRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.deleteMessage(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Message not found with id: 999");
    }

    @Test
    void createMessage_WithNullContent_ShouldThrowException() {
        CreateMessageRequest request = new CreateMessageRequest();
        request.setContent(null);

        assertThatThrownBy(() -> messageService.createMessage(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Content cannot be empty");
    }

    @Test
    void createMessage_WithEmptyContent_ShouldThrowException() {
        CreateMessageRequest request = new CreateMessageRequest();
        request.setContent("");

        assertThatThrownBy(() -> messageService.createMessage(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Content cannot be empty");
    }

    @Test
    void getAllMessages_WithNegativePage_ShouldThrowException() {
        assertThatThrownBy(() -> messageService.getAllMessages(-1, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page index must not be less than zero");
    }

    @Test
    void getAllMessages_WithZeroSize_ShouldThrowException() {
        assertThatThrownBy(() -> messageService.getAllMessages(0, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page size must not be less than one");
    }
}