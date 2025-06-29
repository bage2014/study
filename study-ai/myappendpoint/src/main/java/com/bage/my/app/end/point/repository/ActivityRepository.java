package com.bage.my.app.end.repository;

import com.bage.my.app.end.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}