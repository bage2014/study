package com.bage.demo.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {

    @Test
    void constructor_ShouldSetAllFields() {
        LocalDateTime now = LocalDateTime.now();
        MessageResponse response = new MessageResponse(1L, \