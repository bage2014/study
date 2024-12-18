package com.bage.study.spring.boot.aop;

import jakarta.transaction.Transactional;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AopHelper {

    private Map<Object, Object> map;

    public static void main(String[] args) {

        // 假设 myService 已经被代理
        if (AopUtils.isCglibProxy(AopHelper.class)) {
            System.out.println("这是一个 CGLIB 代理对象");
        }

        // AopContext
        String keyword = "";
        ((AopHelper) AopContext.currentProxy()).transactionTask(keyword);


        // ReflectionUtils
        AopHelper bean = new AopHelper();
        bean.setMapAttribute(new HashMap<>());
        Field field = ReflectionUtils.findField(AopHelper.class, "mapAttribute");
        ReflectionUtils.makeAccessible(field);
        Object value = ReflectionUtils.getField(field, bean);
        System.out.println(value);



    }

    private void setMapAttribute(HashMap<Object, Object> map) {
        this.map = map;
    }


    public void noTransactionTask(){    // 注意这里 调用了代理类的方法
    }

    @Transactional
    void transactionTask(String keyword) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {        //logger
            //error tracking
        }
        System.out.println(keyword);
    }



}
