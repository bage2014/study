package com.bage.study.springboot.aop.annotation.flow.copy;

public class FlowCopyParam {

    private FlowCopy annotation;
    private Class fromClass;
    private String fromMethod;
    private Object fromResponse;

    private String toMethod;

    private FlowCopyConfig config;
    private Object[] args;
    private Class<?>[] parameterTypes;
    private String beanName;
    private Object bean;
    private Exception proceedException;

    public FlowCopy getAnnotation() {
        return annotation;
    }

    public void setAnnotation(FlowCopy annotation) {
        this.annotation = annotation;
    }

    public Class getFromClass() {
        return fromClass;
    }

    public void setFromClass(Class fromClass) {
        this.fromClass = fromClass;
    }

    public String getFromMethod() {
        return fromMethod;
    }

    public void setFromMethod(String fromMethod) {
        this.fromMethod = fromMethod;
    }

    public Object getFromResponse() {
        return fromResponse;
    }

    public void setFromResponse(Object fromResponse) {
        this.fromResponse = fromResponse;
    }

    public String getToMethod() {
        return toMethod;
    }

    public void setToMethod(String toMethod) {
        this.toMethod = toMethod;
    }

    public FlowCopyConfig getConfig() {
        return config;
    }

    public void setConfig(FlowCopyConfig config) {
        this.config = config;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Exception getProceedException() {
        return proceedException;
    }

    public void setProceedException(Exception proceedException) {
        this.proceedException = proceedException;
    }

}
