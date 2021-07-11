package com.bage.agent;

import java.util.List;

public class ClassInfo {

    private String className;
    private List<String> methodNames;

    public ClassInfo(String className, List<String> methodNames) {
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
        return "ClassInfo{" +
                "className='" + className + '\'' +
                ", methodNames=" + methodNames +
                '}';
    }
}
