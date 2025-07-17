package com.bage.my.app.end.point.repository;

import com.bage.my.app.end.point.entity.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {
    AppVersion findTopByOrderByVersionDesc();
}