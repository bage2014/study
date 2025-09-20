package com.bage.my.app.end.point.service.impl;

import com.bage.my.app.end.point.entity.Trajectory;
import com.bage.my.app.end.point.repository.TrajectoryRepository;
import com.bage.my.app.end.point.service.MapTrajectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class MapTrajectoryServiceImpl implements MapTrajectoryService {
    
    @Autowired
    private TrajectoryRepository trajectoryRepository;
    
    @Override
    public Trajectory saveTrajectory(Trajectory trajectory) {
        // 如果没有设置时间，则使用当前时间
        if (trajectory.getTime() == null) {
            trajectory.setTime(LocalDateTime.now());
        }
        return trajectoryRepository.save(trajectory);
    }
    
    @Override
    public void initMockData() {
        Random random = new Random();
        // 上海市长宁区大致经纬度范围
        double minLat = 31.18;
        double maxLat = 31.24;
        double minLng = 121.37;
        double maxLng = 121.45;
        String[] addresses = {"长宁区仙霞路", "长宁区延安西路", "长宁区虹桥路", "长宁区新华路", "长宁区天山路"};

        for (int i = 0; i < 25; i++) {
            double latitude = minLat + random.nextDouble() * (maxLat - minLat);
            double longitude = minLng + random.nextDouble() * (maxLng - minLng);
            LocalDateTime time = LocalDateTime.now()
                .minusDays(random.nextInt(7))
                .withHour(random.nextInt(24))
                .withMinute(random.nextInt(60))
                .withSecond(random.nextInt(60));
            String address = addresses[random.nextInt(addresses.length)];
            Trajectory trajectory = new Trajectory(latitude, longitude, time, address);
            trajectoryRepository.save(trajectory);
        }
    }
    
    @Override
    public Page<Trajectory> findTrajectoriesByTimeRange(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        if (start != null && end != null) {
            return trajectoryRepository.findByTimeBetween(start, end, pageable);
        } else if (start != null) {
            return trajectoryRepository.findByTimeAfter(start, pageable);
        } else if (end != null) {
            return trajectoryRepository.findByTimeBefore(end, pageable);
        } else {
            return findAllTrajectories(pageable);
        }
    }
    
    @Override
    public Page<Trajectory> findAllTrajectories(Pageable pageable) {
        return trajectoryRepository.findAll(pageable);
    }
}