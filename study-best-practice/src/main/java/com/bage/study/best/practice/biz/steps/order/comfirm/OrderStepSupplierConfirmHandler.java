package com.bage.study.best.practice.biz.steps.order.comfirm;

import com.bage.study.best.practice.biz.model.OrderConfirmContext;
import com.bage.study.best.practice.biz.steps.AbstractOrderStepHandler;

import java.util.Random;

public class OrderStepSupplierConfirmHandler extends AbstractOrderStepHandler<OrderConfirmContext> {

    @Override
    protected Boolean process(OrderConfirmContext context) {
        // 订单流程处理
        boolean result = "mock".equals(context) || (new Random().nextInt(100)) > 5;
        System.out.println(this.getClass().getSimpleName() + "-executed-" + result);
        return result;
    }

    public int transactionStep(OrderConfirmContext context) {
        System.out.println(this.getClass().getSimpleName() + "-db transaction");
        return 1;
    }
}
