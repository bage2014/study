package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.trial.cpu.HighCpuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/cpu")
@RestController
@Slf4j
public class CpuController {
    @Autowired
    private HighCpuService service;

    @RequestMapping("/heavy/cpu100")
    public Object cpu100() {
        while(true) {

        }
    }

    @RequestMapping("/heavy/start")
    public Object startHeavyProcess() {
        int result = service.startHeavyProcess();
        return "start:" + result;
    }

    @RequestMapping("/heavy/stop")
    public Object stopHeavyProcess() {
        int result = service.stopHeavyProcess();
        return "stop:" + result;
    }

    @RequestMapping("/light/start")
    public Object startLightProcess() {
        int result = service.startLightProcess();
        return "start:" + result;
    }

    @RequestMapping("/light/stop")
    public Object stopLightProcess() {
        int result = service.stopLightProcess();
        return "stop:" + result;
    }


}
