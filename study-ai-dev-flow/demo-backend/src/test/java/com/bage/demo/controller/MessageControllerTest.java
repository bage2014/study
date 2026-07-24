package com.bage.demo.controller;

import com.bage.demo.dto.MessageCreateRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.MessageUpdateRequest;
import com.bage.demo.exception.ResourceNotFoundException;
import com.bage.demo.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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

    private MessageResponse sampleResponse;
    private MessageCreateRequest sampleCreateRequest;
    private MessageUpdateRequest sampleUpdateRequest;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        sampleResponse = MessageResponse.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .sender("testUser")
                .receiver("receiver")
                .createdAt(now)
                .updatedAt(now)
                .build();

        sampleCreateRequest = MessageCreateRequest.builder()
                .title("New Title")
                .content("New Content")
                .sender("testUser")
                .receiver("receiver")
                .build();

        sampleUpdateRequest = MessageUpdateRequest.builder()
                .title("Updated Title")
                .content("Updated Content")
                .sender("testUser")
                .receiver("receiver")
                .build();
    }

    @Test
    void shouldCreateMessageSuccessfully() throws Exception {
        when(messageService.createMessage(any(MessageCreateRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"));

        verify(messageService, times(1)).createMessage(any(MessageCreateRequest.class));
    }

    @Test
    void shouldReturn400WhenTitleIsBlank() throws Exception {
        MessageCreateRequest invalidRequest = MessageCreateRequest.builder()
                .title("")
                .content("Content")
                .sender("testUser")
                .receiver("receiver")
                .build();

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(messageService, never()).createMessage(any());
    }

    @Test
    void shouldGetMessageByIdSuccessfully() throws Exception {
        when(messageService.getMessageById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/messages/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"));

        verify(messageService, times(1)).getMessageById(1L);
    }

    @Test
    void shouldReturn404WhenMessageNotFound() throws Exception {
        when(messageService.getMessageById(99L)).thenThrow(new ResourceNotFoundException("Message", "id", 99L));

        mockMvc.perform(get("/api/messages/{id}", 99L))
                .andExpect(status().isNotFound());

        verify(messageService, times(1)).getMessageById(99L);
    }

    @Test
    void shouldGetAllMessages() throws Exception {
        Page<MessageResponse> page = new PageImpl<>(List.of(sampleResponse), PageRequest.of(0, 10), 1);
        when(messageService.listMessages(any(), any(), any(), any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("Test Title"));

        verify(messageService, times(1)).listMessages(any(), any(), any(), any(), any());
    }

    @Test
    void shouldReturnEmptyListWhenNoMessages() throws Exception {
        Page<MessageResponse> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        when(messageService.listMessages(any(), any(), any(), any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(messageService, times(1)).listMessages(any(), any(), any(), any(), any());
    }

    @Test
    void shouldUpdateMessageSuccessfully() throws Exception {
        MessageResponse updatedResponse = MessageResponse.builder()
                .id(1L)
                .title("Updated Title")
                .content("Updated Content")
                .sender("testUser")
                .receiver("receiver")
                .build();

        when(messageService.updateMessage(eq(1L), any(MessageUpdateRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/messages/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.content").value("Updated Content"));

        verify(messageService, times(1)).updateMessage(eq(1L), any(MessageUpdateRequest.class));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentMessage() throws Exception {
        when(messageService.updateMessage(eq(99L), any(MessageUpdateRequest.class)))
                .thenThrow(new ResourceNotFoundException("Message", "id", 99L));

        mockMvc.perform(put("/api/messages/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUpdateRequest)))
                .andExpect(status().isNotFound());

        verify(messageService, times(1)).updateMessage(eq(99L), any(MessageUpdateRequest.class));
    }

    @Test
    void shouldDeleteMessageSuccessfully() throws Exception {
        doNothing().when(messageService).deleteMessage(1L);

        mockMvc.perform(delete("/api/messages/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(messageService, times(1)).deleteMessage(1L);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentMessage() throws Exception {
        doThrow(new ResourceNotFoundException("Message", "id", 99L))
                .when(messageService).deleteMessage(99L);

        mockMvc.perform(delete("/api/messages/{id}", 99L))
                .andExpect(status().isNotFound());

        verify(messageService, times(1)).deleteMessage(99L);
    }
}
