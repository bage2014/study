package com.bage.study.nacos;

import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoshuang
 */
@Component
public class ConfigListener {
    
    /**
     * 基于注解监听配置
     *
     * @param newContent
     * @throws Exception
     */
    @NacosConfigListener(dataId = "example", timeout = 500)
    public void onChange(String newContent) {
        System.out.println("newContent : \n" + newContent);
    }
    
}