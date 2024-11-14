package com.bage.jdk21.scopevalue.transaction;

import java.util.Random;

public class OrderStepOrderConfirmHandler extends StepHandler<BaseContext> {

    OrderConfirmDomain1Service domain1Service = new OrderConfirmDomain1Service();
    @Override
    protected Boolean process(BaseContext context) {
        // 订单流程处理
        domain1Service.process();
        return true;
    }

    @Override
    public Integer transaction(BaseContext context) {
        return domain1Service.transaction();
    }

}
