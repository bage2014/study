package com.bage.demo.controller;

import com.bage.demo.dto.MessageRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.exception.ResourceNotFoundException;
import com.bage.demo.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    private ObjectMapper objectMapper;
    private MessageResponse testResponse;
    private MessageRequest testRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testResponse = MessageResponse.builder()
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
    @DisplayName("POST /api/messages - 成功创建消息")
    void createMessage_ShouldReturnCreatedMessage() throws Exception {
        when(messageService.createMessage(any(MessageRequest.class))).thenReturn(testResponse);

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(testResponse.getId()))
                .andExpect(jsonPath("$.data.content").value(testResponse.getContent()));

        verify(messageService, times(1)).createMessage(any(MessageRequest.class));
    }

    @Test
    @DisplayName("POST /api/messages - 内容为空时返回错误")
    void createMessage_ShouldReturnErrorWhenContentBlank() throws Exception {
        MessageRequest invalidRequest = MessageRequest.builder()
                .content("")
                .build();

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));

        verify(messageService, never()).createMessage(any(MessageRequest.class));
    }

    @Test
    @DisplayName("GET /api/messages/{id} - 成功获取消息")
    void getMessageById_ShouldReturnMessage() throws Exception {
        when(messageService.getMessageById(1L)).thenReturn(testResponse);

        mockMvc.perform(get("/api/messages/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(testResponse.getId()))
                .andExpect(jsonPath("$.data.content").value(testResponse.getContent()));

        verify(messageService, times(1)).getMessageById(1L);
    }

    @Test
    @DisplayName("GET /api/messages/{id} - 消息不存在时返回404")
    void getMessageById_ShouldReturnNotFound() throws Exception {
        when(messageService.getMessageById(999L)).thenThrow(new ResourceNotFoundException("Message not found"));

        mockMvc.perform(get("/api/messages/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));

        verify(messageService, times(1)).getMessageById(999L);
    }

    @Test
    @DisplayName("GET /api/messages - 成功获取消息列表")
    void getAllMessages_ShouldReturnMessageList() throws Exception {
        Page<MessageResponse> messagePage = new PageImpl<>(Arrays.asList(testResponse));
        when(messageService.getAllMessages(any())).thenReturn(messagePage);

        mockMvc.perform(get("/api/messages")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content[0].id").value(testResponse.getId()));

        verify(messageService, times(1)).getAllMessages(any());
    }

    @Test
    @DisplayName("PUT /api/messages/{id} - 成功更新消息")
    void updateMessage_ShouldReturnUpdatedMessage() throws Exception {
        MessageResponse updatedResponse = MessageResponse.builder()
                .id(1L)
                .content("Updated content")
                .timestamp(LocalDateTime.now())
                .build();
        MessageRequest updateRequest = MessageRequest.builder()
                .content("Updated content")
                .build();

        when(messageService.updateMessage(eq(1L), any(MessageRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/messages/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content").value("Updated content"));

        verify(messageService, times(1)).updateMessage(eq(1L), any(MessageRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/messages/{id} - 成功删除消息")
    void deleteMessage_ShouldReturnSuccess() throws Exception {
        doNothing().when(messageService).deleteMessage(1L);

        mockMvc.perform(delete("/api/messages/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(messageService, times(1)).deleteMessage(1L);
    }

    @Test
    @DisplayName("DELETE /api/messages/{id} - 消息不存在时返回404")
    void deleteMessage_ShouldReturnNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Message not found")).when(messageService).deleteMessage(999L);

        mockMvc.perform(delete("/api/messages/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));

        verify(messageService, times(1)).deleteMessage(999L);
    }
}