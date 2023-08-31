package com.bage.study.gc.jdk11;

import com.bage.study.gc.biz.gc.JvmGcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/jvm")
@RestController
@Slf4j
public class JvmGcController {
    @Autowired
    private JvmGcService service;

    @RequestMapping("/add")
    public Object add(@RequestParam(value = "step", required = false) Integer step) {
        return service.add(step);
    }


    @RequestMapping("/info")
    public Object info() {
        return service.info();
    }

}
