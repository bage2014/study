package com.bage.study.guava;

import com.google.common.util.concurrent.RateLimiter;

public class LimiterDemo {

    private static  int permitsPerSecond = 100;

    public static void main(String[] args) {

        final RateLimiter rateLimiter = RateLimiter.create(permitsPerSecond);

    }
}
