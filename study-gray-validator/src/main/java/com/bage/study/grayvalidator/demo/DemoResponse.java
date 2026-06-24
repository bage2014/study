package com.bage.study.grayvalidator.demo;

public record DemoResponse(String source, String resource, Object extra) {

    public static DemoResponse original(String resource) {
        return new DemoResponse("ORIGINAL", resource, null);
    }

    public static DemoResponse gray(String resource, Object extra) {
        return new DemoResponse("GRAY", resource, extra);
    }
}