package com.studyskills.monitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Dashboard 页面（Vue 前端通过 /api/skill-usage/** 获取数据）
 */
@Controller
public class DashboardController {

    @GetMapping("/")
    public String dashboard() {
        return "dashboard";
    }
}
