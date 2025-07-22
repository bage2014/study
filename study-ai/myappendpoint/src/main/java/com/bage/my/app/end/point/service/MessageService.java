package com.bage.my.app.end.point.service;

import com.bage.my.app.end.point.entity.Message;
import com.bage.my.app.end.point.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private JavaMailSender mailSender;
    

    public Page<Message> findAll(Specification<Message> spec, Pageable pageable){
        return messageRepository.findAll(spec, pageable);
    }

    public Message save(Message message){
        return messageRepository.save(message);
    }

    public Message sendMessage(Long senderId, Long receiverId, String content) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setIsEmail(false);
        return messageRepository.save(message);
    }
    
    public Message sendEmail(Long senderId, String email, String content) {
        // 发送邮件
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("系统消息");
        mailMessage.setText(content);
        mailSender.send(mailMessage);
        
        // 保存记录
        Message message = new Message();
        message.setSenderId(senderId);
        message.setEmail(email);
        message.setContent(content);
        message.setIsEmail(true);
        return messageRepository.save(message);
    }
    
    public Message markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow();
        message.setIsRead(true);
        message.setReadTime(LocalDateTime.now());
        return messageRepository.save(message);
    }
    
    public void deleteMessage(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow();
        message.setIsDeleted(true);
        messageRepository.save(message);
    }
}