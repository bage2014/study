package com.bage.study.best.practice.biz.model;

import lombok.Data;

import java.util.List;

@Data
public class BaseContext {
    private final ScopedValue<List<Runnable>> transactionStepScopeValueList = ScopedValue.newInstance();
    private Object param;

}
