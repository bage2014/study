package com.bage.demo.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageEntityTest {

    @Test
    void builder_ShouldCreateEntity() {
        MessageEntity entity = MessageEntity.builder()
                .id(1L)
                .content(\