package com.bage.study.springboot.aop.annotation.gray;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GrayConfig {

    public boolean matchGraySwitch(String key) {
        return false;
    }

}
