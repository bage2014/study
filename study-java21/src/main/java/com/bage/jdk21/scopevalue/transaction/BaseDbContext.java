package com.bage.jdk21.scopevalue.transaction;

import java.util.List;


public class BaseDbContext {
    private final ScopedValue<List<Runnable>> transactionScopeValueList = ScopedValue.newInstance();
    public ScopedValue<List<Runnable>> getTransactionScopeValueList() {
        return transactionScopeValueList;
    }

}
