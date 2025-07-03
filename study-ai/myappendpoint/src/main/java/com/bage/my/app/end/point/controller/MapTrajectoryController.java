package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.Trajectory;
import com.bage.my.app.end.point.repository.TrajectoryRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping(value = "/trajectorys")
public class MapTrajectoryController {
    @Autowired
    private TrajectoryRepository trajectoryRepository;

    @PostConstruct
    public void init() {
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

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public Page<Map<String, Object>> getTrajectoryData(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, 
                                                     @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "time"));
        Page<Trajectory> trajectoryPage;
        try {
            LocalDateTime start = startTime != null ? LocalDateTime.parse(startTime) : null;
            LocalDateTime end = endTime != null ? LocalDateTime.parse(endTime) : null;
            if (start != null && end != null) {
                trajectoryPage = trajectoryRepository.findByTimeBetween(start, end, pageable);
            } else if (start != null) {
                trajectoryPage = trajectoryRepository.findByTimeAfter(start, pageable);
            } else if (end != null) {
                trajectoryPage = trajectoryRepository.findByTimeBefore(end, pageable);
            } else {
                trajectoryPage = trajectoryRepository.findAll(pageable);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("时间格式不正确，请使用 ISO-8601 格式: yyyy-MM-ddTHH:mm:ss");
        }
        List<Map<String, Object>> pageContent = new ArrayList<>();
        for (Trajectory trajectory : trajectoryPage.getContent()) {
            Map<String, Object> location = new HashMap<>();
            location.put("latitude", trajectory.getLatitude());
            location.put("longitude", trajectory.getLongitude());
            location.put("time", trajectory.getTime());
            location.put("address", trajectory.getAddress());
            pageContent.add(location);
        }
        return new PageImpl<>(pageContent, pageable, trajectoryPage.getTotalElements());
    }

    @PostMapping("/save")
    public Trajectory saveTrajectory(@RequestBody Trajectory trajectory) {
        trajectory.setTime(LocalDateTime.now());
        return trajectoryRepository.save(trajectory);
    }
}