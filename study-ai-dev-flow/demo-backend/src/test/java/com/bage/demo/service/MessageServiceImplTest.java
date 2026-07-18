package com.bage.demo.service;

import com.bage.demo.dto.*;
import com.bage.demo.entity.Message;
import com.bage.demo.entity.MessageEntity;
import com.bage.demo.exception.BusinessException;
import com.bage.demo.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    private MessageEntity sampleEntity;
    private MessageEntity updatedEntity;

    @BeforeEach
    void setUp() {
        sampleEntity = MessageEntity.builder()
                .id(1L)
                .content(\