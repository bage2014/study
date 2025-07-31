package com.bage.my.app.end.point.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_token") // 将表名改为非关键字
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String token;

    private String refreshToken;

    private LocalDateTime tokenExpireTime;

    private LocalDateTime refreshTokenExpireTime;

}