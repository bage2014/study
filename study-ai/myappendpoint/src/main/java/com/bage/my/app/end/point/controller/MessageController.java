package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.Message;
import com.bage.my.app.end.point.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;
    
    @PostMapping("/send")
    public Message sendMessage(@RequestParam Long senderId, 
                             @RequestParam Long receiverId,
                             @RequestParam String content) {
        return messageService.sendMessage(senderId, receiverId, content);
    }
    
    @PostMapping("/sendEmail")
    public Message sendEmail(@RequestParam Long senderId,
                           @RequestParam String email,
                           @RequestParam String content) {
        return messageService.sendEmail(senderId, email, content);
    }
    
    @PostMapping("/{id}/read")
    public Message markAsRead(@PathVariable Long id) {
        return messageService.markAsRead(id);
    }
    
    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
    }
}