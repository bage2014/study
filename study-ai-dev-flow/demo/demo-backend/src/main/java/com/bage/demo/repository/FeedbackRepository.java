package com.bage.demo.repository;

import com.bage.demo.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Feedback} entities.
 */
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
