package com.bage.study.grayvalidator.core.config;

import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OrderUserConfig {

    private final Map<String, String> orderUserMap;

    public OrderUserConfig() {
        Map<String, String> map = new HashMap<>();

        map.put("20240601001", "10001");
        map.put("20240601002", "10001");
        map.put("20240901007", "10001");
        map.put("20241201100", "10001");

        map.put("20240601010", "10002");
        map.put("20240801020", "10002");
        map.put("20241201200", "10002");

        map.put("20231201888", "88888");
        map.put("20241101888", "88888");

        map.put("20250101099", "99001");
        map.put("20250115099", "99001");

        map.put("20240301555", "55555");
        map.put("20240401666", "66666");
        map.put("20240501777", "77777");

        this.orderUserMap = Map.copyOf(map);
    }

    public Map<String, String> getOrderUserMap() {
        return orderUserMap;
    }
}