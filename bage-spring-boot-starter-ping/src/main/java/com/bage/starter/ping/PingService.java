package com.bage.starter.ping;

public class PingService {

    public String ping(String msg) {
        return String.format("PingController ping , msg = %s", msg);
    }
}
