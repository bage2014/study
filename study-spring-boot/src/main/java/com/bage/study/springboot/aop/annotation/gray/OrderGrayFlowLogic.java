package com.bage.study.springboot.aop.annotation.gray;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author rh_lu
 */
@Slf4j
@Component
public class OrderGrayFlowLogic implements ApplicationContextAware {
    private ApplicationContext context;

    public boolean isGray(OrderGrayFlow annotation, Method method, Object[] args) {
        Map<String, OrderGrayFlowService> beansOfType = context.getBeansOfType(OrderGrayFlowService.class);
        for (OrderGrayFlowService value : beansOfType.values()) {
            if (value.isEffect(annotation, method, args)) {
                log.info("match service = {}", value);
                return true;
            }
        }
        return false;
    }

    public Object getBean(OrderGrayFlow annotation, Method method) {
        Class toClass = annotation.toClass();
        if (method.getDeclaringClass() == toClass) {
            log.warn("grayToClass config illegal, toClass = {}, method.class = {}", toClass, method.getDeclaringClass());
            return null;
        }          // 获取Bean 实例
        String[] beansOfTypeNames = context.getBeanNamesForType(toClass);
        if (beansOfTypeNames == null || beansOfTypeNames.length <= 0) {
            log.warn("can not get bean instance from context, copyToClass = {}", toClass.getName());
            return null;
        }
        return context.getBean(beansOfTypeNames[0]);
    }

    public Object invokeSync(Object bean, OrderGrayFlow annotation, Method method, Object[] args) {         // 同步，直接执行
        Object invoke = null;
        try {
            String methodName = "".equals(annotation.toMethod()) ? method.getName() : annotation.toMethod();
            Class grayToClass = annotation.toClass();
            Method toMethod = grayToClass.getMethod(methodName, method.getParameterTypes());
            invoke = toMethod.invoke(bean, args);
            log.info("toMethod is invoked, bean = {}", bean);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return invoke;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
