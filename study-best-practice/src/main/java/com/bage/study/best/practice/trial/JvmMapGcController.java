package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.trial.gc.WeakHashMapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/jvm/map")
@RestController
@Slf4j
public class JvmMapGcController {

    @Autowired
    private WeakHashMapService service;
    private final int MB = 1024 * 1024;

    @RequestMapping("/addAndFinish/{step}")
    public Object addAndFinish(@PathVariable(value = "step", required = false) Integer step) {
        log.info("addAndFinish step = {}", step);
        if (step == null) {
            step = 100;
        }
        for (int i = 0; i < step; i++) {
            byte[] bytes = new byte[MB * 10];
        }
        return 1;
    }

    @RequestMapping("/size")
    public Object size() {
        int size = service.size();
        log.info("size = {}", size);
        return size;
    }

    @RequestMapping("/add")
    public Object add(@RequestParam(value = "step", required = false) Integer step) {
        StringBuilder sb = new StringBuilder();
        step = step == null ? 100: step;
        for (int i = 0; i < step; i++) {
            sb.append(",").append(UUID.randomUUID().toString());
        }
        service.put(UUID.randomUUID().toString(), sb.toString());
        log.info("size = {}", step);
        return step;
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
