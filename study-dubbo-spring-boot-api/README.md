# study-dubbo-spring-boot #
Dubbo 学习笔记

## Key

功能点、关键点 

- 服务发现
- 负载均衡
- 监控
- 协议支持
- 

https://cn.dubbo.apache.org/zh-cn/overview/core-features/



### 负载均衡

https://cn.dubbo.apache.org/zh-cn/overview/core-features/load-balance/

#### 基本策略

- 随机
- 权重
- 

#### 自定义实现

https://cn.dubbo.apache.org/zh-cn/overview/core-features/extensibility/#%E8%87%AA%E5%AE%9A%E4%B9%89%E6%89%A9%E5%B1%95%E7%A4%BA%E4%BE%8B



## 常见问题 

```
Unable to canonicalize address 127.0.0.1/<unresolved>:2181 because it's not resolvable

```

切换JDK，换回JDK8 ，可以启动时候选择JDK 



## 参考链接 ##

https://github.com/apache/dubbo-samples/tree/master/1-basic/dubbo-samples-spring-boot/dubbo-samples-spring-boot-provider




github 地址 [https://github.com/apache/incubator-dubbo](https://github.com/apache/incubator-dubbo)

官网：https://cn.dubbo.apache.org/zh-cn/overview/quickstart/

官网 + zookeeper：[http://dubbo.apache.org/zh-cn/docs/user/references/registry/zookeeper.html](http://dubbo.apache.org/zh-cn/docs/user/references/registry/zookeeper.html)

https://github.com/apache/dubbo-samples/tree/master/1-basic/dubbo-samples-spring-boot/dubbo-samples-spring-boot-provider
