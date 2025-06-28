package com.example.demo.controller;

import com.example.demo.entity.Trajectory;
import com.example.demo.repository.TrajectoryRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
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

        for (int i = 0; i < 25; i++) {
            double latitude = minLat + random.nextDouble() * (maxLat - minLat);
            double longitude = minLng + random.nextDouble() * (maxLng - minLng);
            LocalDateTime time = LocalDateTime.of(2024, 1, (i % 30) + 1, (i % 24), 0);
            Trajectory trajectory = new Trajectory(latitude, longitude, time);
            trajectoryRepository.save(trajectory);
        }
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public Page<Map<String, Object>> getTrajectoryData(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "time"));
        Page<Trajectory> trajectoryPage = trajectoryRepository.findAll(pageable);
        List<Map<String, Object>> pageContent = new ArrayList<>();
        for (Trajectory trajectory : trajectoryPage.getContent()) {
            Map<String, Object> location = new HashMap<>();
            location.put("latitude", trajectory.getLatitude());
            location.put("longitude", trajectory.getLongitude());
            location.put("time", trajectory.getTime());
            pageContent.add(location);
        }
        return new PageImpl<>(pageContent, pageable, trajectoryPage.getTotalElements());
    }

    @PostMapping("/save")
    public Trajectory saveTrajectory(@RequestBody Trajectory trajectory) {
        return trajectoryRepository.save(trajectory);
    }
}