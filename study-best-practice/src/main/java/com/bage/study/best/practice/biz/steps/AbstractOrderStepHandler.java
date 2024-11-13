package com.bage.study.best.practice.biz.steps;

/**
 * 抽象的订单步骤处理过程
 */
public abstract class AbstractOrderStepHandler implements OrderStepHandler<Object>{
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
    protected abstract Boolean process(Object context);

    @Override
    public Boolean execute(Object context){
        boolean result = process(context);
        if(result && next != null){
            next.execute(context);
        }
        return result;
    }

    @Override
    public Boolean match(Object context) {
        return Boolean.TRUE;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(String config) {
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
}
