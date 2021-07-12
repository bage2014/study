package com.bage.study.agent.hotload.service;

import java.util.List;

public class MyClassInfo {

    private String className;
    private List<String> methodNames;

    public MyClassInfo(String className, List<String> methodNames) {
        this.className = className;
        this.methodNames = methodNames;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getMethodNames() {
        return methodNames;
    }

    public void setMethodNames(List<String> methodNames) {
        this.methodNames = methodNames;
    }

    @Override
    public String toString() {
        return "MyClassInfo{" +
                "className='" + className + '\'' +
                ", methodNames=" + methodNames +
                '}';
    }
}
