package com.bage.jdk21.scopevalue.transaction;

import java.util.ArrayList;
import java.util.List;

public abstract class StepHandler<T extends BaseContext> {

    /**
     * 配置
     */
    private Object config;
    /**
     * 下一个步骤
     */
    private StepHandler next;

    private Boolean submit;

    /**
     * 具体的处理操作
     * @param context
     * @return
     */
    protected abstract Boolean process(T context);
    public Boolean addTransactionStep(BaseContext context, Runnable runnable){
        List<Runnable> runnableList = context.getTransactionScopeValueList().get();
        if(runnableList == null){
            runnableList = new ArrayList<>();
        }
//        System.out.println("trans db old size: " + runnableList.size());
        runnableList.add(runnable);
//        System.out.println("trans db new size: " + runnableList.size());
        return true;
    }

    public Boolean execute(T context){
        boolean result = process(context);
        if(result && Boolean.TRUE.equals(submit)){
            submit(context);
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

    public StepHandler submit(BaseContext context){
        submit = true;

        List<Runnable> runnableList = context.getTransactionScopeValueList().get();
        if(runnableList == null){
            runnableList = new ArrayList<>();
        }
        if(runnableList.isEmpty()){
            return this;
        }
        System.out.println("submit trans db size: " + runnableList.size());
        // 提交事务
        int result = TransactionUtils.doSubmitDb(runnableList);
        // 情况事务内容
        runnableList.clear();
        return this;
    }
}
