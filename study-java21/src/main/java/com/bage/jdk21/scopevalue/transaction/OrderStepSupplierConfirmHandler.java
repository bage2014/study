package com.bage.jdk21.scopevalue.transaction;

import java.util.Random;

public class OrderStepSupplierConfirmHandler extends StepHandler<BaseContext> {
    OrderConfirmDomain2Service domain2Service = new OrderConfirmDomain2Service();
    @Override
    protected Boolean process(BaseContext context) {
        // 订单流程处理
        domain2Service.process();
        return true;
    }

    @Override
    public Integer transaction(BaseContext context) {
        return domain2Service.transaction();
    }
}
