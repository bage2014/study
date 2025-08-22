package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.Message;
import com.bage.my.app.end.point.service.MessageService;
import com.bage.my.app.end.point.util.AuthUtil;
import com.bage.my.app.end.point.util.JsonUtil;

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

import com.bage.my.app.end.point.dto.MessageItem;
import com.bage.my.app.end.point.dto.MessageQueryRequest;
import com.bage.my.app.end.point.entity.ApiResponse;
import com.bage.my.app.end.point.model.response.MessageListResponse;
import com.bage.my.app.end.point.repository.UserRepository;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserRepository userRepository;
    
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
        
        for (int i = 1; i <= 24; i++) {
            Message message = new Message();
            message.setSenderId((long) random.nextInt(10) + 1);
            message.setReceiverId((long) random.nextInt(10) + 1);
            message.setContent(messageContents[random.nextInt(messageContents.length)] + "--" + i);
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
    public ApiResponse<Message> sendMessage( 
                             @RequestParam(value = "receiverId",required = false) Long receiverId,
                             @RequestParam String content) {
        Long senderId = AuthUtil.getCurrentUserId();
        receiverId = receiverId == null ? 0 : receiverId;
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
    public ApiResponse<MessageListResponse> queryMessages(@RequestBody(required = false) MessageQueryRequest param) {
        log.info("queryMessages: {}", param);
        // 如果参数为null，创建一个默认的参数对象
        MessageQueryRequest params = param == null ? new MessageQueryRequest() : param;
    
        // 将页码从1-indexed转换为0-indexed
        int page = Math.max(0, params.getPage() - 1); // 确保页码不会小于0
        Pageable pageable = PageRequest.of(page, params.getSize());
        // if(params.getReceiverId() == null){
        //     params.setReceiverId(0L);
        // }

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
    
        // 执行分页查询
        Page<Message> messagePage = messageService.findAll(spec, pageable);
        List<Message> content = messagePage.getContent();
        log.info("queryMessages content: {}", JsonUtil.toJson(content));

        // 将 Message 列表转换为 MessageItem 列表
        List<MessageItem> messageItems = content.stream()
            .map(message -> {
                MessageItem item = new MessageItem();
                item.setId(message.getId());
                item.setSenderId(message.getSenderId());
                item.setReceiverId(message.getReceiverId());
                userRepository.findById(message.getSenderId()).ifPresent(user -> {
                    item.setSenderName(user.getUsername());
                    item.setSenderAvatar(user.getAvatarUrl());
                });
                
                // // 设置头像字段（示例中设为null，实际应用中可能需要从其他服务获取）
                // item.setSenderName("发送者名称");
                // item.setSenderAvatar("https://avatars.githubusercontent.com/u/18094768?s=400&u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&v=4");
                item.setContent(message.getContent());
                item.setEmail(message.getEmail());
                item.setIsEmail(message.getIsEmail());
                item.setIsRead(message.getIsRead());
                item.setIsDeleted(message.getIsDeleted());
                item.setCreateTime(message.getCreateTime());
                item.setReadTime(message.getReadTime());
                return item;
            })
            .collect(java.util.stream.Collectors.toList());
    
        // 包装查询结果到MessageListResponse对象
        MessageListResponse response = new MessageListResponse(
            messageItems,
            messagePage.getTotalElements(),
            messagePage.getTotalPages(),
            messagePage.getNumber()+1,
            messagePage.getSize()
        );
    
        return ApiResponse.success(response);
    }
}