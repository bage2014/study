package com.bage.jdk21.scopevalue.transaction;

import java.util.List;


public class BaseContext {
    private final ScopedValue<List<Runnable>> transactionScopeValueList = ScopedValue.newInstance();
    private Object param;

    public ScopedValue<List<Runnable>> getTransactionScopeValueList() {
        return transactionScopeValueList;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }
}
