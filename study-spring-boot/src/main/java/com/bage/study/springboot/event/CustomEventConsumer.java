package com.bage.study.springboot.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CustomEventConsumer implements ApplicationListener<MyEvent> {

    public void onApplicationEvent(MyEvent event) {
        System.out.println("CustomEventConsumer:onApplicationEvent:" + event.toString());
    }

}

