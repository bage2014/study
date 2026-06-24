package com.bage.study.grayvalidator.core.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class GrayTargetConfig {

    private String grayTargetUrl     = "http://localhost:8090/internal/gray-echo";
    private String originalTargetUrl = "http://localhost:8090";
    private GrayMode mode            = GrayMode.AOP;
    private int forwardTimeoutMs     = 3000;

    public String   getGrayTargetUrl()     { return grayTargetUrl; }
    public String   getOriginalTargetUrl() { return originalTargetUrl; }
    public GrayMode getMode()              { return mode; }
    public int      getForwardTimeoutMs()  { return forwardTimeoutMs; }

    public void setGrayTargetUrl(String v)     { this.grayTargetUrl = v; }
    public void setOriginalTargetUrl(String v) { this.originalTargetUrl = v; }
    public void setMode(GrayMode v)            { this.mode = v; }
    public void setForwardTimeoutMs(int v)     { this.forwardTimeoutMs = v; }
}