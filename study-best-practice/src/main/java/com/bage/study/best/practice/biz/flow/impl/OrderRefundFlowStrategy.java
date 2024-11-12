package com.bage.study.best.practice.biz.flow.impl;

import com.bage.study.best.practice.biz.flow.OrderFlowStrategy;
import com.bage.study.best.practice.biz.steps.order.comfirm.OrderStepOrderConfirmHandler;
import com.bage.study.best.practice.biz.steps.order.place.OrderStepOrderAddHandler;
import com.bage.study.best.practice.biz.steps.order.place.OrderStepSupplierPlaceHandler;
import com.bage.study.best.practice.biz.steps.order.refund.*;

public class OrderRefundFlowStrategy implements OrderFlowStrategy {

    public static void main(String[] args) {
        OrderStepOrderRefundHandler start = new OrderStepOrderRefundHandler();
        start.setNext(new OrderStepSupplierRefundHandler())
                .setNext(new OrderStepProductRefundHandler())
                .setNext(new OrderStepNotifyChannelHandler())
                .setNext(new OrderStepOfflineLogHandler())
        ;
        start.execute("hello param");
    }
}
