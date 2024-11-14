package com.bage.jdk21.scopevalue.transaction;

public abstract class StepHandler<T extends BaseContext> {

    /**
     * 配置
     */
    private Object config;
    /**
     * 下一个步骤
     */
    private StepHandler next;

    /**
     * 具体的处理操作
     * @param context
     * @return
     */
    protected abstract Boolean process(T context);
    protected abstract Integer transaction(T context);

    public Boolean execute(T context){
        boolean result = process(context);
        if(result){
            context.getTransactionScopeValueList()
                    .get()
                    .add(()->{
                        transaction(context);
                    });
        }
        if(result && next != null){
            next.execute(context);
        }
        return result;
    }

    public Boolean match(T context) {
        return Boolean.TRUE;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(Object config) {
        this.config = config;
    }

    public void setNext(StepHandler next) {
        this.next = next;
    }

    public StepHandler next(StepHandler next) {
        this.next = next;
        return next;
    }

    public StepHandler getNext() {
        return next;
    }

}
