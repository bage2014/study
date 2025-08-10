package com.bage.my.app.end.point.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MessageItem {
    private Long id;
    private Long senderId;  // 发送者ID
    private Long receiverId; // 接收者ID
    private String receiverAvatar; // 接收者头像
    private String content;  // 消息内容
    private String email;   // 邮件地址(可选)
    private Boolean isEmail; // 是否是邮件
    private Boolean isRead; // 是否已读
    private Boolean isDeleted; // 是否删除
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime readTime; // 阅读时间
}
