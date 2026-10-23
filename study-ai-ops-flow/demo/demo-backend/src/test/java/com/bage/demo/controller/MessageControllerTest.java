package com.bage.demo.controller;

import com.bage.demo.dto.MessageCreateRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.MessageUpdateRequest;
import com.bage.demo.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;

    private MessageResponse createResponse(Long id, String content) {
        return MessageResponse.builder()
                .id(id)
                .content(content)
                .sender("user1")
                .receiver("user2")
                .read(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createMessage_ShouldReturnCreated() throws Exception {
        MessageCreateRequest request = MessageCreateRequest.builder()
                .content("New Message")
                .sender("user1")
                .receiver("user2")
                .build();
        MessageResponse response = createResponse(1L, "New Message");

        when(messageService.createMessage(any(MessageCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("New Message"));

        verify(messageService, times(1)).createMessage(any(MessageCreateRequest.class));
    }

    @Test
    void getMessage_ShouldReturnMessage() throws Exception {
        MessageResponse response = createResponse(1L, "Hello World");
        when(messageService.getMessage(1L)).thenReturn(response);

        mockMvc.perform(get("/api/messages/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("Hello World"));

        verify(messageService, times(1)).getMessage(1L);
    }

    @Test
    void getAllMessages_ShouldReturnPage() throws Exception {
        Page<MessageResponse> page = new PageImpl<>(Arrays.asList(
                createResponse(1L, "First"),
                createResponse(2L, "Second")
        ));
        when(messageService.getAllMessages(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].content").value("Second"));

        verify(messageService, times(1)).getAllMessages(any(Pageable.class));
    }

    @Test
    void updateMessage_ShouldReturnUpdated() throws Exception {
        MessageUpdateRequest updateRequest = MessageUpdateRequest.builder()
                .content("Updated")
                .read(true)
                .build();
        MessageResponse response = createResponse(1L, "Updated");
        when(messageService.updateMessage(eq(1L), any(MessageUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/messages/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("Updated"));

        verify(messageService, times(1)).updateMessage(eq(1L), any(MessageUpdateRequest.class));
    }

    @Test
    void deleteMessage_ShouldReturnNoContent() throws Exception {
        doNothing().when(messageService).deleteMessage(1L);

        mockMvc.perform(delete("/api/messages/1"))
                .andExpect(status().isNoContent());

        verify(messageService, times(1)).deleteMessage(1L);
    }

    @Test
    void deleteMessage_WhenNotFound_ShouldReturnServerError() throws Exception {
        doThrow(new RuntimeException("Message not found")).when(messageService).deleteMessage(99L);

        mockMvc.perform(delete("/api/messages/99"))
                .andExpect(status().is5xxServerError());

        verify(messageService, times(1)).deleteMessage(99L);
    }
}
