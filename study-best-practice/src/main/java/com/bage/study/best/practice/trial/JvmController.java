package com.bage.study.best.practice.trial;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/jvm")
@RestController
@Slf4j
public class JvmController {

    private Map<String, Object> map = new HashMap<>();

    @RequestMapping("/gc/full/count")
    public Object gcFullCount(@PathVariable(value = "times", required = false) Integer times) {
        log.info("JvmGcController gc times = {}", times);
        Map<String, Object> temp = new HashMap<>();
        return 1;
    }

    @RequestMapping("/gc/full/cost")
    public Object gcFullCost(@PathVariable(value = "times", required = false) Integer times) {
        log.info("JvmGcController gc times = {}", times);
        Map<String, Object> temp = new HashMap<>();
        return 1;
    }

    @RequestMapping("/gc/young/count")
    public Object gcYoungCount(@PathVariable(value = "times", required = false) Integer times) {
        log.info("JvmGcController gc times = {}", times);
        Map<String, Object> temp = new HashMap<>();
        return 1;
    }

    @RequestMapping("/gc/young/cost")
    public Object gcYoungCost(@PathVariable(value = "times", required = false) Integer times) {
        log.info("JvmGcController gc times = {}", times);
        Map<String, Object> temp = new HashMap<>();
        return 1;
    }

}
