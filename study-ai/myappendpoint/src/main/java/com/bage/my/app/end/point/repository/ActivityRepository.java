package com.bage.my.app.end.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bage.my.app.end.point.entity.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}