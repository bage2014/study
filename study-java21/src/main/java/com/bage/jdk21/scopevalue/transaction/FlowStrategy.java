package com.bage.jdk21.scopevalue.transaction;

public interface FlowStrategy<T> {

    default Boolean match(T context) {
        return Boolean.TRUE;
    }

}
