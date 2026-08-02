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

/**
 * Repository for accessing Message entities.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByContentContainingIgnoreCase(String keyword);

    Page<Message> findByContentContainingIgnoreCase(String keyword, Pageable pageable);

    List<Message> findByCreatedAtAfter(LocalDateTime createdAt);

    Page<Message> findByCreatedAtAfter(LocalDateTime createdAt, Pageable pageable);

    List<Message> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    Page<Message> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE " +
            "(:keyword IS NULL OR LOWER(m.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:start IS NULL OR m.createdAt >= :start) AND " +
            "(:end IS NULL OR m.createdAt <= :end)")
    Page<Message> search(@Param("keyword") String keyword,
                         @Param("start") LocalDateTime start,
                         @Param("end") LocalDateTime end,
                         Pageable pageable);
}
