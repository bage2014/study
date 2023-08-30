package com.bage.study.gc.biz.gc;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JvmGcService {

    private final int MB = 1024 * 1024;
    Map<String, Object> map = new HashMap<>();

    public Object gc(Integer step) {
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

    public Object add(Integer step) {
        log.info("JvmGcController gc step = {}", step);
        if (step == null) {
            step = 100;
        }
        for (int i = 0; i < step; i++) {
            map.put(System.currentTimeMillis() + "-bage-add-" + i, new byte[MB * 10]);
        }
        return 1;
    }


    public Object info() {
        Runtime runtime = Runtime.getRuntime();

        Map<String, Object> map = new HashMap<>();
        map.put("maxMemory", runtime.maxMemory() / MB + " MB;");
        map.put("freeMemory", runtime.freeMemory() / MB + " MB;");
        map.put("totalMemory", runtime.totalMemory() / MB + " MB;");
        return map;
    }

}
