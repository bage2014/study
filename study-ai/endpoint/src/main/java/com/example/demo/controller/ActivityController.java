package com.example.demo.controller;

import com.example.demo.entity.Activity;
import com.example.demo.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class ActivityController {

    @Autowired
    private ActivityRepository activityRepository;

    @GetMapping("/activities")
    public List<Activity> getActivities() {
        List<Activity> activities = activityRepository.findAll();
        if (activities.isEmpty()) {
            return getMockActivities();
        }
        return activities;
    }

    private List<Activity> getMockActivities() {
        return Stream.of(
                new Activity("2024-01-01", "示例活动1", "创建人1"),
                new Activity("2024-01-02", "示例活动2", "创建人2")
        ).collect(Collectors.toList());
    }
}