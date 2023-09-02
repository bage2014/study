package com.bage.study.gc.jdk11;

import com.bage.study.gc.biz.cpu.high.HighCpuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/cpu/high")
@RestController
@Slf4j
public class HighCpuController {
    @Autowired
    private HighCpuService service;

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
