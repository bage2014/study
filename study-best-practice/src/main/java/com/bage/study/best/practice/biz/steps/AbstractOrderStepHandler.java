package com.bage.study.best.practice.biz.steps;

/**
 * 抽象的订单步骤处理过程
 */
public abstract class AbstractOrderStepHandler implements OrderStepHandler{
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

    public Object getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public AbstractOrderStepHandler setNext(AbstractOrderStepHandler next) {
        this.next = next;
        return next;
    }

    public AbstractOrderStepHandler getNext() {
        return next;
    }
}
