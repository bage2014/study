package com.bage.my.app.end.point.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "app_user") // 将表名改为非关键字
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private int loginAttempts = 0;
    private LocalDateTime lockTime;
    // 新增字段
    private String email;
    private String gender;
    private LocalDate birthDate;
    private String avatarUrl;

}