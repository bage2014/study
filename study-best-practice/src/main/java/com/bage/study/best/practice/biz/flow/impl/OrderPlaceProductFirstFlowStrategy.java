package com.bage.study.best.practice.biz.flow.impl;

import com.bage.study.best.practice.biz.steps.order.comfirm.OrderStepOrderConfirmHandler;
import com.bage.study.best.practice.biz.steps.order.comfirm.OrderStepSupplierConfirmHandler;

public class OrderPlaceProductFirstFlowStrategy extends OrderPlaceBaseFlowStrategy  {
    public static void main(String[] args) {
        OrderStepOrderConfirmHandler start = new OrderStepOrderConfirmHandler();
        start.next(new OrderStepSupplierConfirmHandler())
                .next(new OrderStepOrderConfirmHandler())
        ;
        start.execute("mock");
    }
}
