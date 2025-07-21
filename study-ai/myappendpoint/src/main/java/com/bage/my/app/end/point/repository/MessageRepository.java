package com.bage.my.app.end.point.repository;

import com.bage.my.app.end.point.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}