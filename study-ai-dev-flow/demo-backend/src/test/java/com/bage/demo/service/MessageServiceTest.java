package com.bage.demo.service;

import com.bage.demo.dto.MessageRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.MessageUpdateRequest;
import com.bage.demo.dto.PageResponse;
import com.bage.demo.entity.Message;
import com.bage.demo.exception.ResourceNotFoundException;
import com.bage.demo.exception.UnauthorizedException;
import com.bage.demo.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    private Message message;
    private MessageRequest messageRequest;
    private MessageUpdateRequest messageUpdateRequest;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        message = Message.builder()
                .id(1L)
                .content("Test Content")
                .sender("testUser")
                .timestamp(now)
                .deleted(false)
                .updatedAt(now)
                .build();

        messageRequest = MessageRequest.builder()
                .content("New Content")
                .sender("testUser")
                .timestamp(now)
                .build();

        messageUpdateRequest = MessageUpdateRequest.builder()
                .content("Updated Content")
                .sender("testUser")
                .build();
    }

    @Test
    void createMessage_ShouldReturnMessageResponse() {
        given(messageRepository.save(any(Message.class))).willReturn(message);

        MessageResponse response = messageService.createMessage(messageRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getContent()).isEqualTo("Test Content");
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void getMessageById_WhenExists_ShouldReturnMessageResponse() {
        given(messageRepository.findByIdAndDeletedFalse(1L)).willReturn(Optional.of(message));

        MessageResponse response = messageService.getMessageById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getContent()).isEqualTo("Test Content");
    }

    @Test
    void getMessageById_WhenNotExists_ShouldThrowResourceNotFoundException() {
        given(messageRepository.findByIdAndDeletedFalse(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.getMessageById(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAllMessages_ShouldReturnPageResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Message> messagePage = new PageImpl<>(Collections.singletonList(message), pageable, 1);
        given(messageRepository.findByDeletedFalse(any(Pageable.class))).willReturn(messagePage);

        PageResponse<MessageResponse> response = messageService.getAllMessages(0, 10, null, null, null);

        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getTotalElements()).isEqualTo(1);
    }

    @Test
    void getAllMessages_WithSenderFilter_ShouldReturnFilteredPageResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Message> messagePage = new PageImpl<>(Collections.singletonList(message), pageable, 1);
        given(messageRepository.findBySenderAndDeletedFalse(eq("testUser"), any(Pageable.class))).willReturn(messagePage);

        PageResponse<MessageResponse> response = messageService.getAllMessages(0, 10, "testUser", null, null);

        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
    }

    @Test
    void updateMessage_WhenExists_ShouldReturnUpdatedMessageResponse() {
        given(messageRepository.findByIdAndDeletedFalse(1L)).willReturn(Optional.of(message));
        given(messageRepository.save(any(Message.class))).willReturn(message);

        MessageResponse response = messageService.updateMessage(1L, messageUpdateRequest, "testUser");

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    void updateMessage_WhenNotExists_ShouldThrowResourceNotFoundException() {
        given(messageRepository.findByIdAndDeletedFalse(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.updateMessage(999L, messageUpdateRequest, "testUser"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateMessage_WhenUnauthorized_ShouldThrowUnauthorizedException() {
        given(messageRepository.findByIdAndDeletedFalse(1L)).willReturn(Optional.of(message));

        assertThatThrownBy(() -> messageService.updateMessage(1L, messageUpdateRequest, "otherUser"))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void deleteMessage_WhenExists_ShouldSoftDelete() {
        given(messageRepository.findByIdAndDeletedFalse(1L)).willReturn(Optional.of(message));
        given(messageRepository.save(any(Message.class))).willReturn(message);

        messageService.deleteMessage(1L, "testUser");

        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void deleteMessage_WhenNotExists_ShouldThrowResourceNotFoundException() {
        given(messageRepository.findByIdAndDeletedFalse(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.deleteMessage(999L, "testUser"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteMessage_WhenUnauthorized_ShouldThrowUnauthorizedException() {
        given(messageRepository.findByIdAndDeletedFalse(1L)).willReturn(Optional.of(message));

        assertThatThrownBy(() -> messageService.deleteMessage(1L, "otherUser"))
                .isInstanceOf(UnauthorizedException.class);
    }
}
