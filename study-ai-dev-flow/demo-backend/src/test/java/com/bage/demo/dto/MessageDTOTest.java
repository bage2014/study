package com.bage.demo.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MessageDTOTest {

    @Test
    void messageDTOBuilder_ShouldCreateDTOWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        MessageDTO dto = MessageDTO.builder()
                .id(1L)
                .content(\