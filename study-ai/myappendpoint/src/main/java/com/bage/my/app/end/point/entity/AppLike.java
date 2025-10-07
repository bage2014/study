package com.bage.my.app.end.point.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "app_like")
public class AppLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long refId; // 引用ID，用于区分不同的收藏类型

    private String likeType; // 喜欢类型，用于区分不同的收藏类型

    private LocalDateTime createTime = LocalDateTime.now();
}