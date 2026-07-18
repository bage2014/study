package com.bage.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 消息实体类
 * 对应数据库中的消息表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "message")
public class MessageEntity {

    /**
     * 消息ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 发送者标识
     */
    @Column(name = "sender", nullable = false, length = 100)
    private String sender;

    /**
     * 接收者标识
     */
    @Column(name = "receiver", nullable = false, length = 100)
    private String receiver;

    /**
     * 消息内容
     */
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * 消息标题（可选）
     */
    @Column(name = "title", length = 255)
    private String title;

    /**
     * 消息状态：0-未读，1-已读，2-已删除
     */
    @Column(name = "status", nullable = false)
    @Builder.Default
    private Integer status = 0;

    /**
     * 创建时间
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 插入前自动设置创建时间和更新时间
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 更新时自动设置更新时间
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}