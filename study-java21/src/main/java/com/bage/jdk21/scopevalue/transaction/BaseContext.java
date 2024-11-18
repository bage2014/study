package com.bage.jdk21.scopevalue.transaction;

import java.util.List;


public class BaseContext extends BaseDbContext{
    private Object param;

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }
}
