package com.bage.my.app.end.point.service;

import com.bage.my.app.end.point.entity.Message;
import com.bage.my.app.end.point.repository.MessageRepository;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    @Test
    void testFindAll_withValidSpecificationAndPageable_shouldReturnMessages() {
        // 准备测试数据
        Pageable pageable = PageRequest.of(0, 10);
        // 创建包含receiverId=-1L的消息
        Message messageWithNegativeId = createMockMessage(3L, 100L, -1L, "Test message with receiverId=-1");
        // 创建其他消息
        List<Message> allMockMessages = Arrays.asList(
                messageWithNegativeId,
                createMockMessage(1L, 100L, 200L, "Test message 1"),
                createMockMessage(2L, 100L, 200L, "Test message 2")
        );
        // 仅包含receiverId=-1L的消息列表
        List<Message> filteredMessages = List.of(messageWithNegativeId);
        // 期望返回的结果应该只包含过滤后的消息
        Page<Message> expectedPage = new PageImpl<>(filteredMessages, pageable, filteredMessages.size());

        // 创建Specification
        Specification<Message> spec = (root, query, cb) -> cb.equal(root.get("receiverId"), -1L);

        // 更精确地设置Mock行为 - 当传入特定的spec时返回过滤后的结果
        when(messageRepository.findAll(spec, pageable)).thenReturn(expectedPage);

        // 执行测试
        Page<Message> result = messageService.findAll(spec, pageable);
        System.out.println("result: " + result.getContent());
        
        // 验证结果 - 现在应该是1条记录
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        assertEquals(filteredMessages, result.getContent());

        // 验证方法调用
        verify(messageRepository, times(1)).findAll(spec, pageable);
    }

    @Test
    void testFindAll_withEmptyResult_shouldReturnEmptyPage() {
        // 准备测试数据
        Pageable pageable = PageRequest.of(0, 10);
        Page<Message> expectedPage = new PageImpl<>(List.of(), pageable, 0);

        // 创建Specification
        Specification<Message> spec = (root, query, cb) -> cb.equal(root.get("receiverId"), 999L);

        // 设置Mock行为
        when(messageRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        // 执行测试
        Page<Message> result = messageService.findAll(spec, pageable);

        // 验证结果
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        assertEquals(0, result.getContent().size());

        // 验证方法调用
        verify(messageRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void testFindAll_withDifferentPageable_shouldReturnCorrectPage() {
        // 准备测试数据
        Pageable pageable = PageRequest.of(1, 5);
        List<Message> mockMessages = Arrays.asList(
                createMockMessage(6L, 100L, 200L, "Test message 6"),
                createMockMessage(7L, 100L, 200L, "Test message 7")
        );
        Page<Message> expectedPage = new PageImpl<>(mockMessages, pageable, 12); // 总共有12条记录

        // 创建Specification
        Specification<Message> spec = (root, query, cb) -> cb.equal(root.get("senderId"), 100L);

        // 设置Mock行为
        when(messageRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        // 执行测试
        Page<Message> result = messageService.findAll(spec, pageable);

        // 验证结果
        assertEquals(12, result.getTotalElements());
        assertEquals(3, result.getTotalPages()); // 12条记录，每页5条，共3页
        assertEquals(1, result.getNumber()); // 当前是第2页（从0开始计数）
        assertEquals(5, result.getSize());
        assertEquals(2, result.getContent().size());

        // 验证方法调用
        verify(messageRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    private Message createMockMessage(Long id, Long senderId, Long receiverId, String content) {
        Message message = new Message();
        message.setId(id);
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setIsEmail(false);
        message.setIsRead(false);
        message.setIsDeleted(false);
        message.setCreateTime(LocalDateTime.now());
        return message;
    }
}