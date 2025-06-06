package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.trial.gc.JvmGcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/jvm")
@RestController
@Slf4j
public class JvmController {

    @Autowired
    private JvmGcService jvmGcService;

    @RequestMapping("/gc/full/count")
    public Object gcFullCount(@RequestParam(value = "times", required = false) Integer times) {
        log.info("JvmGcController gcFullCount times = {}", times);
        jvmGcService.fullGc(times);
        return 1;
    }

    @RequestMapping("/gc/full/cost")
    public Object gcFullCost(@RequestParam(value = "times", required = false) Integer times) {
        log.info("JvmGcController gcFullCost times = {}", times);
        jvmGcService.fullGc(times);
        return 1;
    }

    @RequestMapping("/gc/young/count")
    public Object gcYoungCount(@RequestParam(value = "times", required = false) Integer times) {
        log.info("JvmGcController gcYoungCount times = {}", times);
        jvmGcService.youngGc(times);
        return 1;
    }

    @RequestMapping("/gc/young/cost")
    public Object gcYoungCost(@RequestParam(value = "times", required = false) Integer times) {
        log.info("JvmGcController gcYoungCost times = {}", times);
        jvmGcService.youngGc(times);
        return 1;
    }

}
