package com.bage.demo.service;

import com.bage.demo.dto.MessageCreateRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.MessageUpdateRequest;
import com.bage.demo.entity.Message;
import com.bage.demo.exception.ResourceNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    private Message message;
    private MessageCreateRequest createRequest;
    private MessageUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        message = Message.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .sender("testUser")
                .receiver("receiver")
                .createdAt(now)
                .updatedAt(now)
                .build();

        createRequest = MessageCreateRequest.builder()
                .title("New Title")
                .content("New Content")
                .sender("testUser")
                .receiver("receiver")
                .build();

        updateRequest = MessageUpdateRequest.builder()
                .title("Updated Title")
                .content("Updated Content")
                .sender("testUser")
                .receiver("receiver")
                .build();
    }

    @Test
    void createMessage_ShouldReturnMessageResponse() {
        given(messageRepository.save(any(Message.class))).willReturn(message);

        MessageResponse response = messageService.createMessage(createRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getContent()).isEqualTo("Test Content");
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void getMessageById_WhenExists_ShouldReturnMessageResponse() {
        given(messageRepository.findById(1L)).willReturn(Optional.of(message));

        MessageResponse response = messageService.getMessageById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getContent()).isEqualTo("Test Content");
    }

    @Test
    void getMessageById_WhenNotExists_ShouldThrowResourceNotFoundException() {
        given(messageRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.getMessageById(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void listMessages_ShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        given(messageRepository.findAll()).willReturn(Collections.singletonList(message));

        Page<MessageResponse> response = messageService.listMessages(null, null, null, null, pageable);

        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getTotalElements()).isEqualTo(1);
    }

    @Test
    void updateMessage_WhenExists_ShouldReturnUpdatedMessageResponse() {
        Message updatedMessage = Message.builder()
                .id(1L)
                .title("Updated Title")
                .content("Updated Content")
                .sender("testUser")
                .receiver("receiver")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        given(messageRepository.findById(1L)).willReturn(Optional.of(message));
        given(messageRepository.save(any(Message.class))).willReturn(updatedMessage);

        MessageResponse response = messageService.updateMessage(1L, updateRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    void updateMessage_WhenNotExists_ShouldThrowResourceNotFoundException() {
        given(messageRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.updateMessage(999L, updateRequest))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteMessage_WhenExists_ShouldDelete() {
        given(messageRepository.existsById(1L)).willReturn(true);

        messageService.deleteMessage(1L);

        verify(messageRepository).deleteById(1L);
    }

    @Test
    void deleteMessage_WhenNotExists_ShouldThrowResourceNotFoundException() {
        given(messageRepository.existsById(999L)).willReturn(false);

        assertThatThrownBy(() -> messageService.deleteMessage(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
