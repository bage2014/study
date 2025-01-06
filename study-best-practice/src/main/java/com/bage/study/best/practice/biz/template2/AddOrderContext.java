package com.bage.study.best.practice.biz.template2;

public class AddOrderContext extends Context{
    private String addOrderContext;

    public String getAddOrderContext() {
        return addOrderContext;
    }

    public void setAddOrderContext(String addOrderContext) {
        this.addOrderContext = addOrderContext;
    }

    @Override
    public String toString() {
        return "AddOrderContext{" +
                "addOrderContext='" + addOrderContext + '\'' +
                '}';
    }
}
