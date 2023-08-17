package com.bage.study.springboot.aop.annotation.flow.copy;


import java.lang.annotation.*;


/**
 * @author rh_lu
 * 流量复制切面,<br/>
 * 同步异步逻辑，<br/>
 * 不支持循环流量拷贝，否则会死循环<br/>
 * <p>
 * 生效条件：
 * 1. 基于Spring Aop 需要满足 AOP 生效条件 <br/>
 * 2. 需要实现相同的接口 【或 存在一样的方法和参数定义】 <br/>
 * 3. 都基于Sring 容器托管 bean
 * <p>
 * 否则，请自己实现 手动复制流量 逻辑
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface FlowCopy {

    /**
     * class 需要依托Spring Bean 托管
     * @return
     */
    Class toClass();

    /**
     * 不配置则名字是一样的
     * @return
     */
    String toMethod() default "";

}
