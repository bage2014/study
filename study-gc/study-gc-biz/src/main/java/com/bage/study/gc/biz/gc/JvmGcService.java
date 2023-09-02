package com.bage.study.gc.biz.gc;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JvmGcService {

    private final int MB = 1024 * 1024;
    private Map<String, Object> map = new HashMap<>();

    public Object add(Integer step) {
        log.info("JvmGcController gc step = {}", step);
        if (step == null) {
            step = 10;
        }
        for (int i = 0; i < step; i++) {
            map.put(System.currentTimeMillis() + "-bage-add-" + i, new byte[MB * 10]);
        }
        return 1;
    }

    public Object newAndFinish(Integer step) {
        log.info("JvmGcController gc step = {}", step);
        if (step == null) {
            step = 10;
        }
        byte[] temp = null;
        for (int i = 0; i < step; i++) {
            temp = new byte[MB * 10];
        }
        return 1;
    }


    public Object info() {
        Runtime runtime = Runtime.getRuntime();

        Map<String, Object> result = new HashMap<>();
        result.put("maxMemory", runtime.maxMemory() / MB + " MB;");
        result.put("freeMemory", runtime.freeMemory() / MB + " MB;");
        result.put("totalMemory", runtime.totalMemory() / MB + " MB;");
        result.put("mapSize", map.size() + ";");
        return result;
    }

}
