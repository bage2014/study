package com.bage.study.best.practice.biz.steps;

import com.bage.study.best.practice.biz.model.BaseContext;

/**
 * 抽象的订单步骤处理过程
 */
public abstract class AbstractOrderStepHandler<T extends BaseContext> implements OrderStepHandler<T>{
    /**
     * 配置
     */
    private Object config;
    /**
     * 下一个步骤
     */
    private AbstractOrderStepHandler next;

    /**
     * 具体的处理操作
     * @param context
     * @return
     */
    protected abstract Boolean process(T context);

    @Override
    public Boolean execute(T context){
        boolean result = process(context);
        if(result){
            context.getTransactionStepScopeValueList()
                    .get()
                    .add(()->{
                        transactionStep(context);
                    });
        }
        if(result && next != null){
            next.execute(context);
        }
        return result;
    }

    @Override
    public Boolean match(T context) {
        return Boolean.TRUE;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(Object config) {
        this.config = config;
    }

    public void setNext(AbstractOrderStepHandler next) {
        this.next = next;
    }

    public AbstractOrderStepHandler next(AbstractOrderStepHandler next) {
        this.next = next;
        return next;
    }

    public AbstractOrderStepHandler getNext() {
        return next;
    }

    public void submitTransaction() {

    }
}
