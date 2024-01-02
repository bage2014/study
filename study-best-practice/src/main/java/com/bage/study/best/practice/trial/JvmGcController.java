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
public class JvmGcController {

    private final int MB = 1024 * 1024;
    private Map<String, Object> map = new HashMap<>();

    @RequestMapping("/gc/{step}")
    public Object gc(@PathVariable(value = "step", required = false) Integer step) {
        log.info("JvmGcController gc step = {}", step);
        if (step == null) {
            step = 100;
        }
        Map<String, Object> temp = new HashMap<>();
        for (int i = 0; i < step; i++) {
            temp.put(System.currentTimeMillis() + "-bage-gc-" + i, new byte[MB * 10]);
        }
        return 1;
    }

    @RequestMapping("/add/{step}")
    public Object add(@PathVariable(value = "step", required = false) Integer step) {
        log.debug("JvmGcController gc step = {}", step);
        if (step == null) {
            step = 100;
        }
        for (int i = 0; i < step; i++) {
            map.put(System.currentTimeMillis() + "-bage-add-" + i, new byte[MB * 10]);
        }
        return 1;
    }

    @RequestMapping("/addAndFinish/{step}")
    public Object addAndFinish(@PathVariable(value = "step", required = false) Integer step) {
        log.debug("JvmGcController addAndFinish step = {}", step);
        if (step == null) {
            step = 100;
        }
        for (int i = 0; i < step; i++) {
            byte[] bytes = new byte[MB * 10];
        }
        return 1;
    }


    @RequestMapping("/info")
    public Object info() {
        Runtime runtime = Runtime.getRuntime();

        Map<String, Object> map = new HashMap<>();
        map.put("maxMemory", runtime.maxMemory() / MB + " MB;");
        map.put("freeMemory", runtime.freeMemory() / MB + " MB;");
        map.put("totalMemory", runtime.totalMemory() / MB + " MB;");
        return map;
    }

}
