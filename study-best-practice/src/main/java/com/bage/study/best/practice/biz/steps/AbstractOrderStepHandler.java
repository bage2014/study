package com.bage.study.best.practice.biz.steps;

import com.bage.study.best.practice.biz.model.BaseContext;

import java.util.List;

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
     * 配置
     */
    private T context;
    private ThreadLocal<List<Runnable>> dbOperationList = new ThreadLocal<>();
    /**
     * 具体的处理操作
     * @param context
     * @return
     */
    protected abstract Boolean process(T context);

    @Override
    public Boolean execute(T context){
        setContext(context);
        boolean result = process(context);
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

    public T getContext() {
        return context;
    }

    public void setContext(T context) {
        this.context = context;
    }

    @Override
    public int addTransactionStep(Runnable runnable) {
        List<Runnable> runnableList = dbOperationList.get();
        runnableList.add(runnable);
        return 0;
    }

    public int submit() {
        List<Runnable> runnableList = dbOperationList.get();
        // 提交事务
        int result= 0;
        if(runnableList != null && !runnableList.isEmpty()){
            result = doSubmitDb(runnableList);
        }
        // 情况事务内容
        runnableList.clear();
        return result;
    }

    private int doSubmitDb(List<Runnable> runnableList) {
        for (int i = 0; i < runnableList.size(); i++) {
            runnableList.get(i).run();
        }
        return 1;
    }
}
