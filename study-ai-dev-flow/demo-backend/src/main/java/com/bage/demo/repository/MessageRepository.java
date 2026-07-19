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

    Page<Message> findByReceiver(String receiver, Pageable pageable);

    Page<Message> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Message> findBySenderAndReceiver(String sender, String receiver, Pageable pageable);

    Page<Message> findBySenderAndCreatedAtBetween(String sender, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Message> findByReceiverAndCreatedAtBetween(String receiver, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Message> findBySenderAndReceiverAndCreatedAtBetween(String sender, String receiver, LocalDateTime start, LocalDateTime end, Pageable pageable);
}