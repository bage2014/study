package com.bage.demo.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageUpdateDTOTest {

    @Test
    void messageUpdateDTONoArgsConstructor_ShouldCreateEmptyDTO() {
        MessageUpdateDTO dto = new MessageUpdateDTO();
        assertNotNull(dto);
        assertNull(dto.getTitle());
        assertNull(dto.getContent());
    }

    @Test
    void messageUpdateDTOSettersAndGetters_ShouldWorkCorrectly() {
        MessageUpdateDTO dto = new MessageUpdateDTO();
        dto.setTitle(\