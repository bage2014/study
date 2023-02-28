package com.bage.study.springboot.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Custom2EventConsumer {

    @EventListener
    public void onApplicationEvent(MyEvent event) {
        System.out.println("Custom2EventConsumer:onApplicationEvent:" + event.toString());
    }

}

