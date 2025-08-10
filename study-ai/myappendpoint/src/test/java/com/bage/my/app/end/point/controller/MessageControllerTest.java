package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.dto.MessageQueryRequest;
import com.bage.my.app.end.point.entity.ApiResponse;
import com.bage.my.app.end.point.entity.Message;
import com.bage.my.app.end.point.model.response.MessageListResponse;
import com.bage.my.app.end.point.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    void testQueryMessagesWithNullParam() {
        // 准备测试数据
        List<Message> messageList = createSampleMessages();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Message> messagePage = new PageImpl<>(messageList, pageable, messageList.size());

        // 模拟服务层返回
        when(messageService.findAll(any(Specification.class), any(Pageable.class))).thenReturn(messagePage);

        // 执行测试
        ApiResponse<MessageListResponse> response = messageController.queryMessages(null);

        // 验证结果
        assertTrue(response.getCode() == 0);
        assertNotNull(response.getData());
        assertEquals(messageList.size(), response.getData().getTotalElements());
        assertEquals(1, response.getData().getTotalPages());
        assertEquals(0, response.getData().getCurrentPage());
        assertEquals(10, response.getData().getPageSize());
    }

    @Test
    void testQueryMessagesWithParams() {
        // 准备测试数据
        Long receiverId = 123L;
        Boolean isRead = false;
        String startTime = "2023-01-01T00:00:00";
        String endTime = "2023-12-31T23:59:59";
        int page = 1;
        int size = 5;

        MessageQueryRequest param = new MessageQueryRequest();
        param.setReceiverId(receiverId);
        param.setIsRead(isRead);
        param.setStartTime(startTime);
        param.setEndTime(endTime);
        param.setPage(page);
        param.setSize(size);

        List<Message> messageList = createSampleMessages();
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messagePage = new PageImpl<>(messageList, pageable, messageList.size());

        // 模拟服务层返回
        when(messageService.findAll(any(Specification.class), any(Pageable.class))).thenReturn(messagePage);

        // 执行测试
        ApiResponse<MessageListResponse> response = messageController.queryMessages(param);

        // 验证结果
        assertTrue(response.getCode() == 0);
        assertNotNull(response.getData());
        assertEquals(messageList.size(), response.getData().getTotalElements());
        assertEquals((int) Math.ceil((double) messageList.size() / size), response.getData().getTotalPages());
        assertEquals(page, response.getData().getCurrentPage());
        assertEquals(size, response.getData().getPageSize());
    }

    @Test
    void testQueryMessagesWithNoResults() {
        // 准备测试数据
        MessageQueryRequest param = new MessageQueryRequest();
        param.setReceiverId(999L); // 不存在的接收者ID
        param.setPage(0);
        param.setSize(10);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Message> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        // 模拟服务层返回
        when(messageService.findAll(any(Specification.class), any(Pageable.class))).thenReturn(emptyPage);

        // 执行测试
        ApiResponse<MessageListResponse> response = messageController.queryMessages(param);

        // 验证结果
        assertTrue(response.getCode() == 0);
        assertNotNull(response.getData());
        assertEquals(0, response.getData().getTotalElements());
        assertEquals(0, response.getData().getTotalPages());
        assertEquals(0, response.getData().getCurrentPage());
        assertEquals(10, response.getData().getPageSize());
        assertTrue(response.getData().getMessages().isEmpty());
    }

    private List<Message> createSampleMessages() {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Message message = new Message();
            message.setId((long) i);
            message.setReceiverId(123L);
            message.setIsRead(i % 2 == 0);
            message.setCreateTime(LocalDateTime.now().minusDays(i));
            message.setContent("Test message " + i);
            messages.add(message);
        }
        return messages;
    }
}