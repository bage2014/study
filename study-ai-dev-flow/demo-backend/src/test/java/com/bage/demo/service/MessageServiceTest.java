package com.bage.demo.service;

import com.bage.demo.dto.MessageCreateRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.MessageUpdateRequest;
import com.bage.demo.entity.Message;
import com.bage.demo.repository.MessageRepository;
import com.bage.demo.service.impl.MessageServiceImpl;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    private Message message;
    private MessageCreateRequest createRequest;
    private MessageUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        message = Message.builder()
                .id(1L)
                .content("Hello World")
                .sender("user1")
                .receiver("user2")
                .read(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        createRequest = MessageCreateRequest.builder()
                .content("New Message")
                .sender("user1")
                .receiver("user2")
                .build();

        updateRequest = MessageUpdateRequest.builder()
                .content("Updated Message")
                .read(true)
                .build();
    }

    @Test
    void createMessage_ShouldReturnResponse() {
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        MessageResponse response = messageService.createMessage(createRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Hello World", response.getContent());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void getMessage_WhenExists_ShouldReturnResponse() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        MessageResponse response = messageService.getMessage(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Hello World", response.getContent());
        verify(messageRepository, times(1)).findById(1L);
    }

    @Test
    void getMessage_WhenNotExists_ShouldThrowException() {
        when(messageRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> messageService.getMessage(99L));
        verify(messageRepository, times(1)).findById(99L);
    }

    @Test
    void getAllMessages_ShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Message> messagePage = new PageImpl<>(Arrays.asList(message));
        when(messageRepository.findAll(pageable)).thenReturn(messagePage);

        Page<MessageResponse> responses = messageService.getAllMessages(pageable);

        assertNotNull(responses);
        assertEquals(1, responses.getTotalElements());
        assertEquals("Hello World", responses.getContent().get(0).getContent());
        verify(messageRepository, times(1)).findAll(pageable);
    }

    @Test
    void updateMessage_WhenExists_ShouldReturnUpdatedResponse() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        MessageResponse response = messageService.updateMessage(1L, updateRequest);

        assertNotNull(response);
        verify(messageRepository, times(1)).findById(1L);
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void updateMessage_WhenNotExists_ShouldThrowException() {
        when(messageRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> messageService.updateMessage(99L, updateRequest));
        verify(messageRepository, times(1)).findById(99L);
        verify(messageRepository, never()).save(any(Message.class));
    }

    @Test
    void deleteMessage_WhenExists_ShouldDelete() {
        when(messageRepository.existsById(1L)).thenReturn(true);
        doNothing().when(messageRepository).deleteById(1L);

        assertDoesNotThrow(() -> messageService.deleteMessage(1L));
        verify(messageRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMessage_WhenNotExists_ShouldThrowException() {
        when(messageRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> messageService.deleteMessage(99L));
        verify(messageRepository, never()).deleteById(99L);
    }
}
