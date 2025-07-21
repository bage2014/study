package com.bage.my.app.end.point.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long senderId;  // 发送者ID
    private Long receiverId; // 接收者ID
    private String content;  // 消息内容
    private String email;   // 邮件地址(可选)
    private Boolean isEmail; // 是否是邮件
    private Boolean isRead; // 是否已读
    private Boolean isDeleted; // 是否删除
    private LocalDateTime createTime = LocalDateTime.now(); // 创建时间
    private LocalDateTime readTime; // 阅读时间
}