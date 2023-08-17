package com.bage.study.springboot.aop.annotation.flow.copy;


import java.util.List;

public class FlowCopyConfig {

    private Class toClass; // 类名，不能为空
    private String toMethod;  // 方法名，【 * 标识所有】

    private int percent; // 与下面的 total 一起协作，流量复制占比 percent/total，比如 1/1000 表示复制千分之一流量
    private int total;
    private boolean sync;  // 是否同步，默认 false， 默认是异步操作

    private List<FlowCopyFinishListener> listener;  // 后置监听器


    public Class getToClass() {
        return toClass;
    }

    public void setToClass(Class toClass) {
        this.toClass = toClass;
    }

    public String getToMethod() {
        return toMethod;
    }

    public void setToMethod(String toMethod) {
        this.toMethod = toMethod;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public List<FlowCopyFinishListener> getListener() {
        return listener;
    }

    public void setListener(List<FlowCopyFinishListener> listener) {
        this.listener = listener;
    }
}
