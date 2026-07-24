package com.bage.demo.repository;

import com.bage.demo.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderOrderByCreatedAtDesc(String sender);

    List<Message> findByReceiverOrderByCreatedAtDesc(String receiver);

    List<Message> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);

    List<Message> findBySenderAndReceiverOrderByCreatedAtDesc(String sender, String receiver);

    List<Message> findBySenderAndCreatedAtBetweenOrderByCreatedAtDesc(String sender, LocalDateTime start, LocalDateTime end);

    List<Message> findByReceiverAndCreatedAtBetweenOrderByCreatedAtDesc(String receiver, LocalDateTime start, LocalDateTime end);

    List<Message> findBySenderAndReceiverAndCreatedAtBetweenOrderByCreatedAtDesc(String sender, String receiver, LocalDateTime start, LocalDateTime end);
}
