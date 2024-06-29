package com.bage.study.springboot.listeners;

import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationListener;

public class MySpringEventListener implements ApplicationListener<SpringApplicationEvent> {

        @Override
        public void onApplicationEvent(SpringApplicationEvent event) {
            // event handling
            System.out.println( "MySpringEventListener  > " + event );
        }

}
