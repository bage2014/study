package com.bage.demo.service;

import com.bage.demo.dto.MessageRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.entity.Message;
import com.bage.demo.exception.ResourceNotFoundException;
import com.bage.demo.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    private MessageService messageService;

    private Message testMessage;
    private MessageRequest testRequest;

    @BeforeEach
    void setUp() {
        testMessage = Message.builder()
                .id(1L)
                .content("Test content")
                .timestamp(LocalDateTime.now())
                .build();

        testRequest = MessageRequest.builder()
                .content("Test content")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("createMessage - 成功创建消息")
    void createMessage_ShouldReturnMessageResponse() {
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage);

        MessageResponse response = messageService.createMessage(testRequest);

        assertNotNull(response);
        assertEquals(testMessage.getId(), response.getId());
        assertEquals(testMessage.getContent(), response.getContent());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    @DisplayName("getMessageById - 成功获取消息")
    void getMessageById_ShouldReturnMessageResponse() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(testMessage));

        MessageResponse response = messageService.getMessageById(1L);

        assertNotNull(response);
        assertEquals(testMessage.getId(), response.getId());
        assertEquals(testMessage.getContent(), response.getContent());
        verify(messageRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getMessageById - 消息不存在时抛出异常")
    void getMessageById_ShouldThrowExceptionWhenNotFound() {
        when(messageRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> messageService.getMessageById(999L));
        verify(messageRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("getAllMessages - 成功获取消息列表")
    void getAllMessages_ShouldReturnPageOfMessageResponses() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Message> messagePage = new PageImpl<>(Arrays.asList(testMessage));
        when(messageRepository.findAll(pageable)).thenReturn(messagePage);

        Page<MessageResponse> response = messageService.getAllMessages(pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        verify(messageRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("updateMessage - 成功更新消息")
    void updateMessage_ShouldReturnUpdatedMessage() {
        Message updatedMessage = Message.builder()
                .id(1L)
                .content("Updated content")
                .timestamp(LocalDateTime.now())
                .build();
        MessageRequest updateRequest = MessageRequest.builder()
                .content("Updated content")
                .build();

        when(messageRepository.findById(1L)).thenReturn(Optional.of(testMessage));
        when(messageRepository.save(any(Message.class))).thenReturn(updatedMessage);

        MessageResponse response = messageService.updateMessage(1L, updateRequest);

        assertNotNull(response);
        assertEquals(updatedMessage.getContent(), response.getContent());
        verify(messageRepository, times(1)).findById(1L);
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    @DisplayName("updateMessage - 消息不存在时抛出异常")
    void updateMessage_ShouldThrowExceptionWhenNotFound() {
        when(messageRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> messageService.updateMessage(999L, testRequest));
        verify(messageRepository, times(1)).findById(999L);
        verify(messageRepository, never()).save(any(Message.class));
    }

    @Test
    @DisplayName("deleteMessage - 成功删除消息")
    void deleteMessage_ShouldDeleteMessageSuccessfully() {
        when(messageRepository.existsById(1L)).thenReturn(true);
        doNothing().when(messageRepository).deleteById(1L);

        assertDoesNotThrow(() -> messageService.deleteMessage(1L));
        verify(messageRepository, times(1)).existsById(1L);
        verify(messageRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteMessage - 消息不存在时抛出异常")
    void deleteMessage_ShouldThrowExceptionWhenNotFound() {
        when(messageRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> messageService.deleteMessage(999L));
        verify(messageRepository, times(1)).existsById(999L);
        verify(messageRepository, never()).deleteById(999L);
    }

    @Test
    @DisplayName("createMessage - 自动设置时间戳")
    void createMessage_ShouldSetTimestampWhenNotProvided() {
        MessageRequest requestWithoutTimestamp = MessageRequest.builder()
                .content("Test content")
                .build();
        Message savedMessage = Message.builder()
                .id(2L)
                .content("Test content")
                .timestamp(LocalDateTime.now())
                .build();

        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        MessageResponse response = messageService.createMessage(requestWithoutTimestamp);

        assertNotNull(response);
        assertNotNull(response.getTimestamp());
        verify(messageRepository, times(1)).save(any(Message.class));
    }
}