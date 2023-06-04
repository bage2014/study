# study- Spring  #
## 简介





## Key关键点

- 行业调研、优势、代替方案等
- 架构
- 动态Bean
- 生命周期
- 容器信息获取spring 动态注册bean【或者修改bean】 
- 条件加载【加载bean】 
- 启动事件监听【事件机制完成服务启动后的信息整理】 
- 事件机制获取HTTP请求调用详情【http请求监控】  
- 钩子Aware
- order + 配置优先级
- profile 环境
- 自动装配
- AOP【有哪些类型的通知（Advice）】
- Spring Boot 启动过程



## **概要** 



## **架构** 



## 条件加载 

https://my.oschina.net/wca/blog/5523428

何时被调用回调？



## 启动过程

https://blog.csdn.net/well_nature/article/details/125580649

https://www.cnblogs.com/enhance/p/16988798.html



```
public ConfigurableApplicationContext run(String... args) {
   StopWatch stopWatch = new StopWatch();
   stopWatch.start();
   ConfigurableApplicationContext context = null;
   Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList<>();
   configureHeadlessProperty();
   SpringApplicationRunListeners listeners = getRunListeners(args);
   listeners.starting();
   try {
      ApplicationArguments applicationArguments = new DefaultApplicationArguments(
            args);
      ConfigurableEnvironment environment = prepareEnvironment(listeners,
            applicationArguments);
      configureIgnoreBeanInfo(environment);
      Banner printedBanner = printBanner(environment);
      context = createApplicationContext();
      exceptionReporters = getSpringFactoriesInstances(
            SpringBootExceptionReporter.class,
            new Class[] { ConfigurableApplicationContext.class }, context);
      prepareContext(context, environment, listeners, applicationArguments,
            printedBanner);
      refreshContext(context);
      afterRefresh(context, applicationArguments);
      stopWatch.stop();
      if (this.logStartupInfo) {
         new StartupInfoLogger(this.mainApplicationClass)
               .logStarted(getApplicationLog(), stopWatch);
      }
      listeners.started(context);
      callRunners(context, applicationArguments);
   }
   catch (Throwable ex) {
      handleRunFailure(context, ex, exceptionReporters, listeners);
      throw new IllegalStateException(ex);
   }

   try {
      listeners.running(context);
   }
   catch (Throwable ex) {
      handleRunFailure(context, ex, exceptionReporters, null);
      throw new IllegalStateException(ex);
   }
   return context;
}
```



## Bilibili 

2023-05-19 

https://www.bilibili.com/video/BV1jR4y187Yn/?p=6&vd_source=72424c3da68577f00ea40a9e4f9001a1
