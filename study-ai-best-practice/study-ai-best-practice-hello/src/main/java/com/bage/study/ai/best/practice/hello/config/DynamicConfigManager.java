package com.bage.study.ai.best.practice.hello.config;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DynamicConfigManager {

    private final Map<String, Object> configCache = new ConcurrentHashMap<>();

    public DynamicConfigManager() {
        loadDefaultConfigs();
    }

    private void loadDefaultConfigs() {
        configCache.put("model.default", "deepseek");
        configCache.put("model.temperature.default", 0.7);
        configCache.put("model.max_tokens.default", 2048);
        configCache.put("mcp.enabled", true);
    }

    public void updateConfig(String key, Object value) {
        configCache.put(key, value);
    }

    public Object getConfig(String key) {
        return configCache.get(key);
    }

    public <T> T getConfig(String key, T defaultValue) {
        Object value = configCache.get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return (T) value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public void removeConfig(String key) {
        configCache.remove(key);
    }

    public Map<String, Object> getAllConfigs() {
        return Map.copyOf(configCache);
    }

    public void clearCache() {
        configCache.clear();
        loadDefaultConfigs();
    }
}
