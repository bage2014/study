package com.bage.demo.controller;

import com.bage.demo.dto.CreateMessageRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.PageResponse;
import com.bage.demo.dto.UpdateMessageRequest;
import com.bage.demo.entity.Message;
import com.bage.demo.exception.ResourceNotFoundException;
import com.bage.demo.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageService messageService;

    @Test
    void createMessage_ShouldReturnCreated() throws Exception {
        CreateMessageRequest request = new CreateMessageRequest();
        request.setContent("Hello");

        MessageResponse response = new MessageResponse();
        response.setId(1L);
        response.setContent("Hello");

        given(messageService.createMessage(any(CreateMessageRequest.class))).willReturn(response);

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Hello"));
    }

    @Test
    void getAllMessages_ShouldReturnPage() throws Exception {
        MessageResponse msg1 = new MessageResponse();
        msg1.setId(1L);
        msg1.setContent("Hello");

        MessageResponse msg2 = new MessageResponse();
        msg2.setId(2L);
        msg2.setContent("World");

        Page<MessageResponse> page = new PageImpl<>(List.of(msg1, msg2));
        given(messageService.getAllMessages(anyInt(), anyInt())).willReturn(page);

        mockMvc.perform(get("/api/messages")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[1].id").value(2L));
    }

    @Test
    void getMessageById_WhenExists_ShouldReturnMessage() throws Exception {
        MessageResponse response = new MessageResponse();
        response.setId(1L);
        response.setContent("Hello");

        given(messageService.getMessageById(1L)).willReturn(response);

        mockMvc.perform(get("/api/messages/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Hello"));
    }

    @Test
    void getMessageById_WhenNotExists_ShouldReturn404() throws Exception {
        given(messageService.getMessageById(999L)).willThrow(new ResourceNotFoundException("Message not found with id: 999"));

        mockMvc.perform(get("/api/messages/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateMessage_WhenExists_ShouldReturnUpdated() throws Exception {
        UpdateMessageRequest request = new UpdateMessageRequest();
        request.setContent("Updated");

        MessageResponse response = new MessageResponse();
        response.setId(1L);
        response.setContent("Updated");

        given(messageService.updateMessage(eq(1L), any(UpdateMessageRequest.class))).willReturn(response);

        mockMvc.perform(put("/api/messages/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Updated"));
    }

    @Test
    void updateMessage_WhenNotExists_ShouldReturn404() throws Exception {
        UpdateMessageRequest request = new UpdateMessageRequest();
        request.setContent("Updated");

        given(messageService.updateMessage(eq(999L), any(UpdateMessageRequest.class)))
                .willThrow(new ResourceNotFoundException("Message not found with id: 999"));

        mockMvc.perform(put("/api/messages/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMessage_WhenExists_ShouldReturnNoContent() throws Exception {
        willDoNothing().given(messageService).deleteMessage(1L);

        mockMvc.perform(delete("/api/messages/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMessage_WhenNotExists_ShouldReturn404() throws Exception {
        willDoNothing().given(messageService).deleteMessage(999L);

        mockMvc.perform(delete("/api/messages/999"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createMessage_WithInvalidContent_ShouldReturnBadRequest() throws Exception {
        CreateMessageRequest request = new CreateMessageRequest();
        request.setContent("");

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMessages_WithNegativePage_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/messages")
                        .param("page", "-1")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMessages_WithZeroSize_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/messages")
                        .param("page", "0")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());
    }
}