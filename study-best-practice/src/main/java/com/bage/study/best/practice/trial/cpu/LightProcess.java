package com.bage.study.best.practice.trial.cpu;

import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor
public class LightProcess implements Runnable {

    @Override
    public void run() {
        Long l = 0l;
        while(HighCpuService.CAN_RUN_LIGHT) {
            l++;
            try {
                Thread.sleep(new Random().nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(l == Long.MAX_VALUE) {
                l = 0l;
            }
        }
    }
}