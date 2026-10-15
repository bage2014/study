package com.bage.demo.repository;

import com.bage.demo.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Feedback entity providing CRUD operations on H2.
 */
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
