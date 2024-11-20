package com.bage.study.best.practice.biz.flow.impl;

import com.bage.study.best.practice.biz.model.BaseContext;
import com.bage.study.best.practice.biz.steps.order.place.OrderStepOrderAddHandler;
import com.bage.study.best.practice.biz.steps.order.place.OrderStepProductAddHandler;
import com.bage.study.best.practice.biz.steps.order.place.OrderStepSupplierPlaceHandler;
import com.bage.study.best.practice.biz.steps.order.place.OrderStepValidCheckHandler;

public class OrderPlaceFlowStrategy extends OrderPlaceBaseFlowStrategy {
    public static void main(String[] args) {
        BaseContext context = new BaseContext();
        OrderStepValidCheckHandler start = new OrderStepValidCheckHandler();
        start.next(new OrderStepOrderAddHandler())
                .next(new OrderStepSupplierPlaceHandler())
                .next(new OrderStepProductAddHandler())
                .submit(context)
        ;
        start.execute(context);
    }
}
