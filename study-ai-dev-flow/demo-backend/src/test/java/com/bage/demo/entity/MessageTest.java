package com.bage.demo.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void constructor_ShouldSetAllFields() {
        LocalDateTime now = LocalDateTime.now();
        Message message = new Message(1L, \