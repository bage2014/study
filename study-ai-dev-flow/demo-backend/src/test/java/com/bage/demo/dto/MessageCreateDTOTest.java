package com.bage.demo.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageCreateDTOTest {

    @Test
    void builder_ShouldCreateDTO() {
        MessageCreateDTO dto = MessageCreateDTO.builder()
                .content(\