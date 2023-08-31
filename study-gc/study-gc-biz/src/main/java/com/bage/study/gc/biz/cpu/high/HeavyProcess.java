package com.bage.study.gc.biz.cpu.high;

import lombok.AllArgsConstructor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@AllArgsConstructor
public class HeavyProcess implements Runnable {

    private long length;

    @Override
    public void run() {
        while (HighCpuService.CAN_RUN_HEAVY) {
            String data = "";
            for (int i = 0; i < length; i++) {
                data += UUID.randomUUID().toString();
            }

            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            digest.update(data.getBytes());
        }
    }
}