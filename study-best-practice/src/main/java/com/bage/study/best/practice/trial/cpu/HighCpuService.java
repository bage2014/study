package com.bage.study.best.practice.trial.cpu;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class HighCpuService {

    private static int countHeavy = 0;
    private static int countLight = 0;
    public static boolean CAN_RUN_HEAVY = false;
    public static boolean CAN_RUN_LIGHT = false;

    public int startHeavyProcess() {
        CAN_RUN_HEAVY = true;
        countHeavy++;
        Thread heavyThread = new Thread(new HeavyProcess(10000));
        heavyThread.setName("Heavy-Process-" + countHeavy);
        heavyThread.start();
        return countHeavy;
    }


    public int stopHeavyProcess() {
        CAN_RUN_HEAVY = false;
        return countHeavy;
    }


    public int startLightProcess() {
        CAN_RUN_LIGHT = true;
        countLight++;
        Thread lightThread1 = new Thread(new LightProcess());
        lightThread1.setName("Light-Process-" + countLight + "-1");
        lightThread1.start();
        Thread lightThread2 = new Thread(new LightProcess());
        lightThread2.setName("Light-Process-" + countLight + "-2");
        lightThread2.start();
        Thread lightThread3 = new Thread(new LightProcess());
        lightThread3.setName("Light-Process-" + countLight + "-3");
        lightThread3.start();
        return countLight;
    }

    public int stopLightProcess() {
        CAN_RUN_LIGHT = false;
        return countLight;
    }

}