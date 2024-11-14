package com.bage.study.best.practice.biz.flow.impl;

import com.bage.study.best.practice.biz.flow.OrderFlowStrategy;
import com.bage.study.best.practice.biz.model.OrderConfirmContext;
import com.bage.study.best.practice.biz.steps.order.comfirm.OrderStepOrderConfirmHandler;
import com.bage.study.best.practice.biz.steps.order.comfirm.OrderStepSupplierConfirmHandler;

import java.util.ArrayList;
import java.util.List;

public class OrderConfirmFlowStrategy implements OrderFlowStrategy<Object> {

    public static void main(String[] args) {
        OrderConfirmContext context = new OrderConfirmContext();
        ScopedValue.where(context.getTransactionStepScopeValueList(), new ArrayList<>()).run(() -> {
            OrderStepOrderConfirmHandler start = new OrderStepOrderConfirmHandler();
            start.next(new OrderStepSupplierConfirmHandler())
            ;
            start.execute(context);
        });
        List<Runnable> list = context.getTransactionStepScopeValueList().get();
        for (Runnable runnable : list) {
            runnable.run();
        }
    }
}
