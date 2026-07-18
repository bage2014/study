package com.bage.demo.controller;

import com.bage.demo.dto.MessageDTO;
import com.bage.demo.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
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
    void createMessage_ShouldReturnCreatedMessage() throws Exception {
        MessageDTO requestDTO = new MessageDTO();
        requestDTO.setContent("Hello World");
        requestDTO.setSender("John");

        MessageDTO responseDTO = new MessageDTO();
        responseDTO.setId(1L);
        responseDTO.setContent("Hello World");
        responseDTO.setSender("John");

        when(messageService.createMessage(any(MessageDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Hello World"))
                .andExpect(jsonPath("$.sender").value("John"));
    }

    @Test
    void getMessageById_ShouldReturnMessage() throws Exception {
        Long id = 1L;
        MessageDTO dto = new MessageDTO();
        dto.setId(id);
        dto.setContent("Test");
        dto.setSender("Alice");

        when(messageService.getMessageById(id)).thenReturn(dto);

        mockMvc.perform(get("/api/messages/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.content").value("Test"))
                .andExpect(jsonPath("$.sender").value("Alice"));
    }

    @Test
    void getMessageById_ShouldReturn404WhenNotFound() throws Exception {
        Long id = 999L;
        when(messageService.getMessageById(id)).thenThrow(new RuntimeException("Message not found"));

        mockMvc.perform(get("/api/messages/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllMessages_ShouldReturnList() throws Exception {
        MessageDTO dto1 = new MessageDTO();
        dto1.setId(1L);
        dto1.setContent("First");
        dto1.setSender("Alice");

        MessageDTO dto2 = new MessageDTO();
        dto2.setId(2L);
        dto2.setContent("Second");
        dto2.setSender("Bob");

        when(messageService.getAllMessages()).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void updateMessage_ShouldReturnUpdatedMessage() throws Exception {
        Long id = 1L;
        MessageDTO requestDTO = new MessageDTO();
        requestDTO.setContent("Updated");
        requestDTO.setSender("Alice");

        MessageDTO responseDTO = new MessageDTO();
        responseDTO.setId(id);
        responseDTO.setContent("Updated");
        responseDTO.setSender("Alice");

        when(messageService.updateMessage(eq(id), any(MessageDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/messages/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.content").value("Updated"));
    }

    @Test
    void deleteMessage_ShouldReturnNoContent() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/api/messages/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMessage_ShouldReturn404WhenNotFound() throws Exception {
        Long id = 999L;
        mockMvc.perform(delete("/api/messages/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void createMessage_ShouldReturn400WhenContentEmpty() throws Exception {
        MessageDTO requestDTO = new MessageDTO();
        requestDTO.setContent("");
        requestDTO.setSender("John");

        mockMvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMessage_ShouldReturn400WhenSenderEmpty() throws Exception {
        MessageDTO requestDTO = new MessageDTO();
        requestDTO.setContent("Hello");
        requestDTO.setSender("");

        mockMvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateMessage_ShouldReturn400WhenContentEmpty() throws Exception {
        Long id = 1L;
        MessageDTO requestDTO = new MessageDTO();
        requestDTO.setContent("");
        requestDTO.setSender("Alice");

        mockMvc.perform(put("/api/messages/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllMessages_ShouldReturnEmptyList() throws Exception {
        when(messageService.getAllMessages()).thenReturn(List.of());

        mockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }
}