package com.bage.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("my.properties")
public class MyProperties {

    private String config;
    private boolean enabled;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "MyProperties{" +
                "config='" + config + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
