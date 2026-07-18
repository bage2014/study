package com.bage.demo.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageRequestTest {

    @Test
    void constructor_ShouldSetContent() {
        MessageRequest request = new MessageRequest(\