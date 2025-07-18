package com.bage.study.springboot.aop.annotation.gray;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
public class GrayConfig {

    public boolean matchGraySwitch(String key) {
        int nexted = new Random().nextInt(100);
        return nexted < 50;
    }

}
