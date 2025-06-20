package com.example.demo.repository;

import com.example.demo.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}