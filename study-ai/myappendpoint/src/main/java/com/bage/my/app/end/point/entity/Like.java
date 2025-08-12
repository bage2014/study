package com.bage.my.app.end.point.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "app_like", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "m3uEntryId"})
})
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int m3uEntryId; // M3uEntry的ID

    private LocalDateTime createTime = LocalDateTime.now();
}