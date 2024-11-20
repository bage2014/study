package com.bage.jdk21.scopevalue.transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionsFlowStrategy implements FlowStrategy<Object> {

    public static void main(String[] args) {
        BaseContext context = new BaseContext();
        ScopedValue.where(context.getTransactionScopeValueList(), new ArrayList<>()).run(() -> {
            OrderStepOrderConfirmHandler start = new OrderStepOrderConfirmHandler();
            start.next(new OrderStepSupplierConfirmHandler())
                    .submit(context)
                    .next(new OrderStepOrderConfirm2Handler())
                    .next(new OrderStepOrderConfirm3Handler())
                    .submit(context)
                    .next(new OrderStepOrderConfirm4Handler())
                    .submit(context)
            ;
            start.execute(context);
        });
    }
}
