package com.bage.starter;

public class MyExitsBean {

    MyProperties properties;

    public MyExitsBean(MyProperties properties) {
        this.properties = properties;
    }

    public MyProperties getProperties() {
        return properties;
    }

    public void setProperties(MyProperties properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "MyExitsBean{" +
                "properties=" + properties +
                '}';
    }
}
