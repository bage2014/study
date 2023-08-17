package com.bage.study.springboot.aop.annotation.flow.copy;

public class FlowCopyFinishParam {

    private Class fromClass; // 流量复制 发起原类
    private Object fromResponse; // 原结果
    private String fromMethod; // 原方法名
    private Class toClass; // 流量复制 接收执行类
    private Object toResponse; // 复制类的执行结果
    private String toMethod; // 执行的方法名
    private Object[] args; // 请求入参

    public Class getFromClass() {
        return fromClass;
    }

    public void setFromClass(Class fromClass) {
        this.fromClass = fromClass;
    }

    public Object getFromResponse() {
        return fromResponse;
    }

    public void setFromResponse(Object fromResponse) {
        this.fromResponse = fromResponse;
    }

    public String getFromMethod() {
        return fromMethod;
    }

    public void setFromMethod(String fromMethod) {
        this.fromMethod = fromMethod;
    }

    public Class getToClass() {
        return toClass;
    }

    public void setToClass(Class toClass) {
        this.toClass = toClass;
    }

    public Object getToResponse() {
        return toResponse;
    }

    public void setToResponse(Object toResponse) {
        this.toResponse = toResponse;
    }

    public String getToMethod() {
        return toMethod;
    }

    public void setToMethod(String toMethod) {
        this.toMethod = toMethod;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
