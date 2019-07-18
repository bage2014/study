package com.bage.study.mybatis.springboot.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }) })
public class SqlTimeCostPlugin implements Interceptor {
  private Properties properties = new Properties();
  public Object intercept(Invocation invocation) throws Throwable {
    long start = System.currentTimeMillis();
    // implement pre processing if need
    Object returnObject = invocation.proceed();
    // implement post processing if need
    long end = System.currentTimeMillis();

    System.out.println("time cost: " + (end - start));
    return returnObject;
  }

  @Override
  public Object plugin(Object target) {
    System.out.println("target:" + target);
    Object wrap = Plugin.wrap(target, this);
    return wrap;
  }

  public void setProperties(Properties properties) {
    System.out.println("properties:" + properties);
    this.properties = properties;
  }
}