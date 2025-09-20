package com.bage.my.app.end.point.service;

import com.bage.my.app.end.point.entity.Trajectory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

public interface MapTrajectoryService {
    
    /**
     * 保存位置轨迹
     */
    Trajectory saveTrajectory(Trajectory trajectory);
    
    /**
     * 初始化模拟数据
     */
    void initMockData();
    
    /**
     * 根据时间区间查询轨迹数据
     */
    Page<Trajectory> findTrajectoriesByTimeRange(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    /**
     * 查询所有轨迹数据
     */
    Page<Trajectory> findAllTrajectories(Pageable pageable);
}