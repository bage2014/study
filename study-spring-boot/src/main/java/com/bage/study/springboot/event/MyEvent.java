package com.bage.study.springboot.event;

import org.springframework.context.ApplicationEvent;

public class MyEvent extends ApplicationEvent {

    private String msg;

    public MyEvent(Object source) {
        super(source);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "MyEvent{" +
                "msg='" + msg + '\'' +
                ", source=" + source +
                '}';
    }
}
