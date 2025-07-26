package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.Message;
import com.bage.my.app.end.point.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.bage.my.app.end.point.dto.MessageQueryRequest;
import com.bage.my.app.end.point.entity.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {
    @Autowired
    private MessageService messageService;
    
    @PostConstruct
    public void initDefaultMessages() {
        Random random = new Random();
        String[] messageContents = {
            "你好，最近怎么样？",
            "项目进展如何？",
            "会议安排在明天10点",
            "请查收附件",
            "紧急：服务器需要维护"
        };
        
        for (int i = 1; i <= 50; i++) {
            Message message = new Message();
            message.setSenderId((long) random.nextInt(10) + 1);
            message.setReceiverId((long) random.nextInt(10) + 1);
            message.setContent(messageContents[random.nextInt(messageContents.length)]);
            message.setIsRead(random.nextBoolean());
            message.setCreateTime(LocalDateTime.now().minusDays(random.nextInt(30)));
            
            if (message.getIsRead()) {
                message.setReadTime(message.getCreateTime().plusHours(random.nextInt(24)));
            }
            
            messageService.save(message);
        }
        log.info("已初始化50条默认消息");
    }
    
    @RequestMapping("/send")
    public ApiResponse<Message> sendMessage(@RequestParam Long senderId, 
                             @RequestParam Long receiverId,
                             @RequestParam String content) {
        return ApiResponse.success(messageService.sendMessage(senderId, receiverId, content));
    }
    
    @RequestMapping("/sendEmail")
    public ApiResponse<Message> sendEmail(@RequestParam Long senderId,
                           @RequestParam String email,
                           @RequestParam String content) {
        return ApiResponse.success(messageService.sendEmail(senderId, email, content));
    }
    
    @RequestMapping("/{id}/read")
    public ApiResponse<Message> markAsRead(@PathVariable Long id) {
        return ApiResponse.success(messageService.markAsRead(id));
    }
    
    @RequestMapping("/{id}")
    public ApiResponse<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ApiResponse.success(null);
    }
    
    @RequestMapping("/query")
    public ApiResponse<List<Message>> queryMessages(@RequestBody(required = false) MessageQueryRequest param) {
        log.info("queryMessages: {}", param);
        // 如果参数为null，创建一个默认的参数对象
        MessageQueryRequest params = param == null ? new MessageQueryRequest() : param;
        
        Pageable pageable = PageRequest.of(params.getPage(), params.getSize());
        
        Specification<Message> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (params.getReceiverId() != null) {
                predicates.add(cb.equal(root.get("receiverId"), params.getReceiverId()));
            }
            
            if (params.getIsRead() != null) {
                predicates.add(cb.equal(root.get("isRead"), params.getIsRead()));
            }
            
            if (params.getStartTime() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createTime"), 
                    LocalDateTime.parse(params.getStartTime())));
            }
            
            if (params.getEndTime() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createTime"), 
                    LocalDateTime.parse(params.getEndTime())));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        Page<Message> messages = messageService.findAll(spec, pageable);
        // log.info("queryMessages: {}", messages.getContent());
        return ApiResponse.success(messages.getContent(), messages.getTotalElements(), (long) messages.getPageable().getPageNumber(), (long) messages.getPageable().getPageSize());
    }
}