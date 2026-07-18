package com.bage.demo.repository;

import com.bage.demo.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findBySender(String sender, Pageable pageable);

    Page<Message> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Message> findBySenderAndTimestampBetween(String sender, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}