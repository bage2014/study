package com.bage.demo.repository;

import com.bage.demo.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findBySender(String sender, Pageable pageable);

    Page<Message> findByReceiver(String receiver, Pageable pageable);

    Page<Message> findByMessageType(String messageType, Pageable pageable);

    Page<Message> findBySenderAndReceiver(String sender, String receiver, Pageable pageable);

    Page<Message> findBySenderAndMessageType(String sender, String messageType, Pageable pageable);

    Page<Message> findByReceiverAndMessageType(String receiver, String messageType, Pageable pageable);

    Page<Message> findBySenderAndReceiverAndMessageType(String sender, String receiver, String messageType, Pageable pageable);

    List<Message> findByIdIn(List<Long> ids);

    long countByIdIn(List<Long> ids);
}