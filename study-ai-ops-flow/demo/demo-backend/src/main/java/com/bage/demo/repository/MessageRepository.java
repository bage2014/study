package com.bage.demo.repository;

import com.bage.demo.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Message} entities.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
