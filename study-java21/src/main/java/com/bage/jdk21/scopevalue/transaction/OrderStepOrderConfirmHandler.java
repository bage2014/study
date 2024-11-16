package com.bage.jdk21.scopevalue.transaction;

public class OrderStepOrderConfirmHandler extends StepHandler<BaseContext> {

    OrderConfirmDomain1Service domain1Service = new OrderConfirmDomain1Service();
    @Override
    protected Boolean process(BaseContext context) {
        // 订单流程处理
        domain1Service.process();

        addTransactionStep(context,()->domain1Service.process());
        return true;
    }

}
