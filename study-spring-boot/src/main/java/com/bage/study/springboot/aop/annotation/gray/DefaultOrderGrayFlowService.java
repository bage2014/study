package com.bage.study.springboot.aop.annotation.gray;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Slf4j
public class DefaultOrderGrayFlowService implements OrderGrayFlowService {
    @Autowired
    private OrderGrayFlowCommonLogic commonLogic;
    @Autowired
    private GrayConfig grayConfig;

    @Override
    public boolean isEffect(OrderGrayFlow annotation, Method method, Object[] args) {
        String key = commonLogic.getKeyBySpEL(annotation, method, args);
        String configKey = "gray.switch.new.flow." + method.getName() + "." + method.getClass().getSimpleName();
        boolean matchGraySwitch = grayConfig.matchGraySwitch(configKey);
        log.info("method = {}, matchGraySwitch1 = {}, key = {}", method.getName(), matchGraySwitch, key);
        if (matchGraySwitch) {
            return true;
        }
        configKey = "gray.switch.new.flow." + method.getName();
        matchGraySwitch = grayConfig.matchGraySwitch(configKey);
        log.info("method = {}, matchGraySwitch2 = {}, key = {}", method.getName(), matchGraySwitch, key);
        return matchGraySwitch;
    }
}
