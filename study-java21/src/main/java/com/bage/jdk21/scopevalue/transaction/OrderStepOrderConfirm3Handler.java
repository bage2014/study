package com.bage.jdk21.scopevalue.transaction;

public class OrderStepOrderConfirm3Handler extends StepHandler<BaseContext> {

    OrderConfirmDomain1Service domain1Service = new OrderConfirmDomain1Service();
    @Override
    protected Boolean process(BaseContext context) {
        // 订单流程处理
        domain1Service.process();

        addTransactionStep(context,()->domain1Service.transaction(this.getClass().getSimpleName()));
        return true;
    }

}
