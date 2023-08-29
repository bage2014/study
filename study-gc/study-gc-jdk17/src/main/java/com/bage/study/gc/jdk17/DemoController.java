package com.bage.study.gc.jdk17;

import com.bage.study.gc.biz.cpu.DemoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/startProcess")
public class DemoController {

    private final DemoService service;

    @GetMapping("/heavy")
    public void startHeavyProcess(){
        service.startHeavyProcess();
    }

    @GetMapping("/light")
    public void startLightProcess(){
        service.startLightProcess();
    }
}