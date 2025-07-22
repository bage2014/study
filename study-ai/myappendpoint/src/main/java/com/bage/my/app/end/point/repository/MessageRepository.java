package com.bage.my.app.end.point.repository;

import com.bage.my.app.end.point.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {
    Page<Message> findByReceiverId(Long receiverId, Pageable pageable);
}