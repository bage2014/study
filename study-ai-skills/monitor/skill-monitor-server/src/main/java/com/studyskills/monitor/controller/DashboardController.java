package com.studyskills.monitor.controller;

import com.studyskills.monitor.service.SkillUsageService;
import com.studyskills.monitor.service.SystemOpsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final SkillUsageService service;
    private final SystemOpsService systemOps;

    @GetMapping("/")
    public String dashboard(@RequestParam(defaultValue = "7") int days, Model model) {
        model.addAttribute("days", days);
        model.addAttribute("overview", service.overview(days));
        model.addAttribute("topSkills", service.topSkills(days, 10));
        model.addAttribute("dailyTrend", service.dailyTrend(Math.max(days, 14)));
        model.addAttribute("triggerDist", service.triggerDistribution(days));
        model.addAttribute("clientDist", service.clientDistribution(days));
        model.addAttribute("recent", service.recent(20));
        model.addAttribute("system", systemOps.status());
        return "dashboard";
    }
}
