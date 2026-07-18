package com.bage.demo.service.impl;

import com.bage.demo.dto.MessageDTO;
import com.bage.demo.entity.Message;
import com.bage.demo.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    void createMessage_ShouldReturnCreatedDTO() {
        MessageDTO inputDTO = new MessageDTO();
        inputDTO.setContent("Hello");
        inputDTO.setSender("Alice");

        Message savedMessage = new Message();
        savedMessage.setId(1L);
        savedMessage.setContent("Hello");
        savedMessage.setSender("Alice");

        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        MessageDTO result = messageService.createMessage(inputDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Hello", result.getContent());
        assertEquals("Alice", result.getSender());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void getMessageById_ShouldReturnDTOWhenExists() {
        Long id = 1L;
        Message message = new Message();
        message.setId(id);
        message.setContent("Test");
        message.setSender("Bob");

        when(messageRepository.findById(id)).thenReturn(Optional.of(message));

        MessageDTO result = messageService.getMessageById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Test", result.getContent());
        assertEquals("Bob", result.getSender());
    }

    @Test
    void getMessageById_ShouldThrowExceptionWhenNotFound() {
        Long id = 999L;
        when(messageRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> messageService.getMessageById(id));
    }

    @Test
    void getAllMessages_ShouldReturnList() {
        Message msg1 = new Message();
        msg1.setId(1L);
        msg1.setContent("First");
        msg1.setSender("Alice");

        Message msg2 = new Message();
        msg2.setId(2L);
        msg2.setContent("Second");
        msg2.setSender("Bob");

        when(messageRepository.findAll()).thenReturn(List.of(msg1, msg2));

        List<MessageDTO> result = messageService.getAllMessages();

        assertEquals(2, result.size());
        assertEquals("First", result.get(0).getContent());
        assertEquals("Second", result.get(1).getContent());
    }

    @Test
    void getAllMessages_ShouldReturnEmptyListWhenNoMessages() {
        when(messageRepository.findAll()).thenReturn(List.of());

        List<MessageDTO> result = messageService.getAllMessages();

        assertTrue(result.isEmpty());
    }

    @Test
    void updateMessage_ShouldUpdateAndReturnDTO() {
        Long id = 1L;
        MessageDTO inputDTO = new MessageDTO();
        inputDTO.setContent("Updated");
        inputDTO.setSender("Alice");

        Message existingMessage = new Message();
        existingMessage.setId(id);
        existingMessage.setContent("Old");
        existingMessage.setSender("Bob");

        Message updatedMessage = new Message();
        updatedMessage.setId(id);
        updatedMessage.setContent("Updated");
        updatedMessage.setSender("Alice");

        when(messageRepository.findById(id)).thenReturn(Optional.of(existingMessage));
        when(messageRepository.save(any(Message.class))).thenReturn(updatedMessage);

        MessageDTO result = messageService.updateMessage(id, inputDTO);

        assertNotNull(result);
        assertEquals("Updated", result.getContent());
        assertEquals("Alice", result.getSender());
        verify(messageRepository, times(1)).findById(id);
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void updateMessage_ShouldThrowExceptionWhenNotFound() {
        Long id = 999L;
        MessageDTO inputDTO = new MessageDTO();
        inputDTO.setContent("Updated");
        inputDTO.setSender("Alice");

        when(messageRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> messageService.updateMessage(id, inputDTO));
    }

    @Test
    void deleteMessage_ShouldDeleteExistingMessage() {
        Long id = 1L;
        when(messageRepository.existsById(id)).thenReturn(true);

        messageService.deleteMessage(id);

        verify(messageRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteMessage_ShouldThrowExceptionWhenNotFound() {
        Long id = 999L;
        when(messageRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> messageService.deleteMessage(id));
    }
}