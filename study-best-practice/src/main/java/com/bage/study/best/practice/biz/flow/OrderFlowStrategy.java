package com.bage.study.best.practice.biz.flow;

public interface OrderFlowStrategy<T> {

    default Boolean match(T context) {
        return Boolean.TRUE;
    }

}
