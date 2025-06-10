# study-Parterns #
## 简介





TODO

## Key关键点

- 模式概念
- 组件



## 组件

### Contract

Feign在构建动态代理的时候，会去解析方法上的注解和参数，获取Http请求需要用到基本参数

而这个Contract接口的作用就是用来干解析这件事的

- Contract的默认实现是解析Feign自己原生注解的

- SpringCloud在整合Feign的时候，为了让Feign能够识别Spring MVC的注解，所以就自己实现了Contract接口

### Encoder

具体的作用就是将请求体对应的方法参数序列化成字节数组



### Client

从接口方法的参数和返回值其实可以看出，这其实就是动态代理对象最终用来执行Http请求的组件

默认实现就是通过JDK提供的HttpURLConnection来的

除了这个默认的，Feign还提供了基于HttpClient和OkHttp实现的



### InvocationHandlerFactory

对于JDK动态代理来说，必须得实现InvocationHandler才能创建动态代理

InvocationHandler的invoke方法实现就是动态代理走的核心逻辑

而InvocationHandlerFactory其实就是创建InvocationHandler的工厂



### RequestInterceptor

RequestInterceptor它其实是一个在发送请求前的一个拦截接口

通过这个接口，在发送Http请求之前再对Http请求的内容进行修改

比如我们可以设置一些接口需要的公共参数，如鉴权token之类的



### Retryer

这是一个重试的组件



## 参考链接

https://juejin.cn/post/7394456880963125299