package com.bage.demo.repository;

import com.bage.demo.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Find all non-deleted messages
    List<Message> findByDeletedFalse();

    // Find non-deleted message by id
    Optional<Message> findByIdAndDeletedFalse(Long id);

    // Find non-deleted messages with pagination
    Page<Message> findByDeletedFalse(Pageable pageable);

    // Find non-deleted messages by sender with pagination
    Page<Message> findBySenderAndDeletedFalse(String sender, Pageable pageable);

    // Find non-deleted messages within a date range
    @Query("SELECT m FROM Message m WHERE m.deleted = false AND m.timestamp BETWEEN :start AND :end")
    List<Message> findByTimestampBetweenAndDeletedFalse(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Find non-deleted messages by sender and date range
    @Query("SELECT m FROM Message m WHERE m.deleted = false AND m.sender = :sender AND m.timestamp BETWEEN :start AND :end")
    List<Message> findBySenderAndTimestampBetweenAndDeletedFalse(@Param("sender") String sender,
                                                                  @Param("start") LocalDateTime start,
                                                                  @Param("end") LocalDateTime end);
}
