package com.bage.study.best.practice.biz.flow.impl;

import com.bage.study.best.practice.biz.flow.OrderFlowStrategy;
import com.bage.study.best.practice.biz.steps.order.refund.*;

public class OrderRefundFlowStrategy implements OrderFlowStrategy<Object> {

    public static void main(String[] args) {
        OrderStepOrderRefundHandler start = new OrderStepOrderRefundHandler();
        start.next(new OrderStepSupplierRefundHandler())
                .next(new OrderStepProductRefundHandler())
                .next(new OrderStepNotifyChannelHandler())
                .next(new OrderStepOfflineLogHandler())
                .next(new OrderStepRefundEventHandler())
        ;
        start.execute("hello param");
    }
}
