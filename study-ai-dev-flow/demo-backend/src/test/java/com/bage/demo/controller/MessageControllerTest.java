package com.bage.demo.controller;

import com.bage.demo.dto.MessageRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.MessageUpdateRequest;
import com.bage.demo.dto.PageResponse;
import com.bage.demo.exception.ResourceNotFoundException;
import com.bage.demo.exception.UnauthorizedException;
import com.bage.demo.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

    private MessageResponse messageResponse;
    private MessageRequest messageRequest;
    private MessageUpdateRequest messageUpdateRequest;
    private PageResponse<MessageResponse> pageResponse;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        messageResponse = MessageResponse.builder()
                .id(1L)
                .content("Test Content")
                .sender("testUser")
                .timestamp(now)
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

        pageResponse = PageResponse.<MessageResponse>builder()
                .content(Collections.singletonList(messageResponse))
                .page(0)
                .size(10)
                .totalElements(1L)
                .totalPages(1)
                .build();
    }

    @Test
    void createMessage_ShouldReturnCreated() throws Exception {
        given(messageService.createMessage(any(MessageRequest.class))).willReturn(messageResponse);

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Test Content"));
    }

    @Test
    void createMessage_WithInvalidBody_ShouldReturnBadRequest() throws Exception {
        MessageRequest invalidRequest = MessageRequest.builder().build();

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMessageById_ShouldReturnOk() throws Exception {
        given(messageService.getMessageById(1L)).willReturn(messageResponse);

        mockMvc.perform(get("/api/messages/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Test Content"));
    }

    @Test
    void getMessageById_NotFound_ShouldReturn404() throws Exception {
        given(messageService.getMessageById(999L)).willThrow(new ResourceNotFoundException("Message", "id", 999L));

        mockMvc.perform(get("/api/messages/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllMessages_ShouldReturnOk() throws Exception {
        given(messageService.getAllMessages(anyInt(), anyInt(), any(), any(), any()))
                .willReturn(pageResponse);

        mockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].content").value("Test Content"));
    }

    @Test
    void updateMessage_ShouldReturnOk() throws Exception {
        given(messageService.updateMessage(eq(1L), any(MessageUpdateRequest.class), anyString()))
                .willReturn(messageResponse);

        mockMvc.perform(put("/api/messages/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void updateMessage_NotFound_ShouldReturn404() throws Exception {
        doThrow(new ResourceNotFoundException("Message", "id", 999L))
                .when(messageService).updateMessage(eq(999L), any(MessageUpdateRequest.class), anyString());

        mockMvc.perform(put("/api/messages/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageUpdateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateMessage_Unauthorized_ShouldReturn403() throws Exception {
        given(messageService.updateMessage(eq(1L), any(MessageUpdateRequest.class), anyString()))
                .willThrow(new UnauthorizedException("Unauthorized"));

        mockMvc.perform(put("/api/messages/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageUpdateRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteMessage_ShouldReturnNoContent() throws Exception {
        doNothing().when(messageService).deleteMessage(eq(1L), anyString());

        mockMvc.perform(delete("/api/messages/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMessage_NotFound_ShouldReturn404() throws Exception {
        doThrow(new ResourceNotFoundException("Message", "id", 999L)).when(messageService).deleteMessage(eq(999L), anyString());

        mockMvc.perform(delete("/api/messages/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMessage_Unauthorized_ShouldReturn403() throws Exception {
        doThrow(new UnauthorizedException("Unauthorized")).when(messageService).deleteMessage(eq(1L), anyString());

        mockMvc.perform(delete("/api/messages/1"))
                .andExpect(status().isForbidden());
    }
}
