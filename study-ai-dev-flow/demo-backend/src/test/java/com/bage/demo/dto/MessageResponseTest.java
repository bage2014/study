package com.bage.demo.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {

    @Test
    void shouldCreateMessageResponseWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        MessageResponse response = new MessageResponse();
        response.setId(1L);
        response.setContent(\