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
- 配置优化 
- Spring Boot Starter
- 循环依赖[如何解决]
- 事务失效
- 一些流程



## **概要** 



## **架构** 



![](https://img-blog.csdnimg.cn/img_convert/d6ab48a77bd32bfec4e73d457c7a2682.png)



### 功能模块

轻量级 - Spring 在代码量和透明度方面都很轻便。

IOC - 控制反转

AOP - 面向切面编程可以将应用业务逻辑和系统服务分离，以实现高内聚。

容器 - Spring 负责创建和管理对象（Bean）的生命周期和配置。

MVC - 对 web 应用提供了高度可配置性，其他框架的集成也十分方便。

事务管理 - 提供了用于事务管理的通用抽象层。Spring 的事务支持也可用于容器较少的环境。

JDBC 异常 - Spring 的 JDBC 抽象层提供了一个异常层次结构，简化了错误处理策略。



## 自动装配

### 局限性

 覆盖的可能性 - 您始终可以使用 <constructor-arg> 和 <property> 设置指定依赖项，这将覆盖自动装配。

基本元数据类型 - 简单属性（如原数据类型，字符串和类）无法自动装配。

令人困惑的性质 - 总是喜欢使用明确的装配，因为自动装配不太精确。



## AOP 

### 执行顺序

多个AOP 切面执行的顺序 ？



## DispatcherServlet 的工作流程



![](https://mmbiz.qpic.cn/mmbiz_png/ibLfOIwWn242drgCjBCwUTfnzr76ftCdn29LjYtpwBYmaicaKGJMg89nS0YBrlmBTxZ9KicFEqYDAK3l4s95PicjxQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

1. 向服务器发送 HTTP 请求，请求被前端控制器 DispatcherServlet 捕获。

2. DispatcherServlet 根据 -servlet.xml 中的配置对请求的 URL 进行解析，得到请求资源标识符（URI）。然后根据该 URI，调用 HandlerMapping 获得该 Handler 配置的所有相关的对象（包括 Handler 对象以及 Handler 对象对应的拦截器），最后以 HandlerExecutionChain 对象的形式返回。

3. DispatcherServlet 根据获得的 Handler ，选择一个合适的 HandlerAdapter 。（附注：如果成功获得 HandlerAdapter 后，此时将开始执行拦截器的 preHandler(...)方法）。

4. 提取 Request 中的模型数据，填充 Handler 入参，开始执行 Handler （ Controller )。 在填充 Handler 的入参过程中，根据你的配置，Spring 将帮你做一些额外的工作：Handler(Controller)执行完成后，向 DispatcherServlet 返回一个 ModelAndView 对象；

- HttpMessageConveter ： 将请求消息（如 Json、xml 等数据）转换成一个对象，将对象转换为指定的响应信息。
- 数据转换 ：对请求消息进行数据转换。如 String 转换成 Integer 、 Double 等。
- 数据根式化 ：对请求消息进行数据格式化。 如将字符串转换成格式化数字或格式化日期等。
- 数据验证 ： 验证数据的有效性（长度、格式等），验证结果存储到 BindingResult 或 Error 中。

5. 根据返回的 ModelAndView ，选择一个适合的 ViewResolver （必须是已经注册到 Spring 容器中的 ViewResolver )返回给 DispatcherServlet 。

6. ViewResolver 结合 Model 和 View ，来渲染视图。

7. 视图负责将渲染结果返回给客户端。



## 条件加载 

https://my.oschina.net/wca/blog/5523428

何时被调用回调？

@ConditionalOn XXX

@AutoConfigure XXX



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



## Bean作用域

默认的作用域是singleton。

- singleton：Spring将bean定义的范围限定为每个Spring IOC容器只有一个单实例。
- prototype：单个bean定义有任意数量的对象实例。
- request：作用域为一次http请求，该作用域仅在基于web的Spring ApplicationContext情形下有效。
- session：作用域为HTTP Session，该作用域仅在基于web的Spring ApplicationContext情形下有效。
- global-session：作用域为全局的HTTP session。该作用域也是仅在基于web的Spring ApplicationContext情形下有效。



# Bean的生命周期

https://www.zhihu.com/question/362583020/answer/2826263500?utm_id=0

![](https://picx.zhimg.com/80/v2-b1579aad1db6b6b7ea7376d19454d7b1_1440w.webp?source=1940ef5c)



1. 处理名称，检查缓存

2. 处理父子容器

3. 处理 dependsOn

4. 选择 scope 策略

5. 创建 bean (关键阶段)

6. 类型转换处理

7. 销毁 bean



## 高阶使用

### BeanFactoryPostProcessor

- 动态注册Bean
- 动态修改Bean 



### ApplicationListener

- ContextRefreshedEvent Spring 刷新事件 
- ServletRequestHandledEvent http服务调用事件



### XXAware

- ApplicationEventPublisherAware

  生效通过实现该接口，我们能在setApplicationEventPublisher方法拿到applicationEventPublisher，这个对象是用来向容器发布一些事件的

  

  ApplicationStartupAware

  ApplicationContextAware

  BeanNameAware 等 



### 环境与profile

- 不同环境不同配置



### SpEL 表达式

- 切面参数解析【比如cacheable】



### Event

- Spring各种事件回调监听
- 自定义事件发布和订阅
- 事件同步处理和异步处理配置



### Async 

- 异步方法异步类



### 循环依赖 

https://mp.weixin.qq.com/s/Un8pyET2XDXpDY4FnRbwXw

https://www.kancloud.cn/imnotdown1019/java_core_full/2177474

https://xie.infoq.cn/article/e3b46dc2c0125ab812f9aa977

三个缓存 

![](https://mmbiz.qpic.cn/sz_mmbiz_png/GvtDGKK4uYkMCL2KK3DtAry0FgPGAV5zUtuzhdmu4ML4EKric1pBuLdjmM7Fus6rxnKRrJjicJnFzge53FicxwpFQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)



另外一个说明：

A《-》B 相互依赖情况下：

**一级缓存**，A 实例化，需要B，此时去初始化B，放入一级缓存；然后赋值属性给到A；能解决A-》B问题，但是反过来还没有处理好；

**二级缓存**，A 实例化，放入二级缓存；后续填充属性，发现需要B，此时去初始化B，放入一级缓存；然后赋值属性给到A；能解决A-》B问题；同时，在使用到B过程中，发现依赖A，需要填充A属性，此时，去二级缓存中拿A实例，能解决B-》A问题

**三级缓存**，为解决AOP代理问题



## 事务失效

- 方法的访问权限必须是public，其他private等权限，事务失效
- 方法被定义成了final的，这样会导致事务失效。
- 在同一个类中的方法直接内部调用，会导致事务失效。
- 一个方法如果没交给spring管理，就不会生成spring事务。
- 如果自己try...catch误吞了异常，事务失效。
- 错误的传播
- 多线程调用，两个方法不在同一个线程中，获取到的数据库连接不一样的。
- 表的存储引擎不支持事务



## 一些流程

https://juejin.cn/post/7471660829956571186



## 参考链接 

【2023-07-13】https://zhuanlan.zhihu.com/p/497330696



【2023-07-13】https://www.jianshu.com/p/b2adcdb59013

【2023-07-13】https://www.cnblogs.com/zhuxiong/p/7653506.html

【2023-07-13】https://blog.csdn.net/qq_33373609/article/details/121747756

http://hk.javashuo.com/article/p-pupuntul-cy.html





【2023-05-30】 https://mp.weixin.qq.com/s?__biz=MzU5OTc3NDM2Mw==&mid=2247483928&idx=1&sn=a7dd0674a661402975f7d13a2c3f2095&chksm=feae87ecc9d90efa3a8b642314c9b40ce136201fdfe55534d70e0fc984f3eff861774fa9cb17&scene=27

【2023-05-30】http://e.betheme.net/article/show-162498.html

【2023-05-30】https://zhuanlan.zhihu.com/p/93594713

https://ac.nowcoder.com/discuss/955061?type=2&order=0&pos=4&page=1&channel=-1&source_id=discuss_center_2_nctrack 【无答案】







## Bilibili 

2023-05-19 

https://www.bilibili.com/video/BV1jR4y187Yn/?p=6&vd_source=72424c3da68577f00ea40a9e4f9001a1

