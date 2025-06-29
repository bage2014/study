package com.bage.my.app.end.controller;

import com.bage.my.app.end.entity.Activity;
import com.bage.my.app.end.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Random;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.annotation.PostConstruct;

@RestController
public class ActivityController {
    
    @PostConstruct
    public void init() {
        for (int i = 0; i < 25; i++) {
            addMockActivity();
        }
    }

    @Autowired
    private ActivityRepository activityRepository;

    @GetMapping("/activities")
    public Page<Activity> getActivities(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Activity> activities = activityRepository.findAll(pageable);
        if (activities.isEmpty()) {
            return Page.empty();
        }
        return activities;
    }

    @PostMapping("/activities")
    public Activity addActivity(@RequestBody Activity activity) {
        return activityRepository.save(activity);
    }

    @RequestMapping("/activities/mock")
    public Activity addMockActivity() {
        Random random = new Random();
        String creator = "创建人" + random.nextInt(10000);
        String description = "示例活动" + random.nextInt(10000);
        LocalDate date = LocalDate.now().minusDays(random.nextInt(30));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Activity mockActivity = new Activity(
            date.format(formatter),
            description,
            creator
        );
        return activityRepository.save(mockActivity);
    }

    private List<Activity> getMockActivities() {
        return Stream.of(
                new Activity("2024-01-01", "示例活动1", "创建人1"),
                new Activity("2024-01-02", "示例活动2", "创建人2")
        ).collect(Collectors.toList());
    }
}