
## study-sentinel
## 链接

官网    https://github.com/alibaba/Sentinel/wiki/%E4%BB%8B%E7%BB%8D

使用
https://sentinelguard.io/zh-cn/docs/basic-api-resource-rule.html

如何使用

https://github.com/alibaba/Sentinel/wiki/%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8

## 依赖

<dependency>
<groupId>com.alibaba.csp</groupId>
<artifactId>sentinel-core</artifactId>
<version>1.8.4</version>
</dependency>



流量控制有以下几个角度:

- 资源的调用关系，例如资源的调用链路，资源和资源之间的关系；
- 运行指标，例如 QPS、线程池、系统负载等；
- 控制的效果，例如直接限流、冷启动、排队等。

Sentinel 的设计理念是让您自由选择控制的角度，并进行灵活组合，从而达到想要的效果。



demo https://github.com/alibaba/Sentinel/wiki/%E6%96%B0%E6%89%8B%E6%8C%87%E5%8D%97#%E5%85%AC%E7%BD%91-demo



服务端控制台

https://github.com/alibaba/Sentinel/wiki/%E6%8E%A7%E5%88%B6%E5%8F%B0#2-%E5%90%AF%E5%8A%A8%E6%8E%A7%E5%88%B6%E5%8F%B0

docer 版本
https://hub.docker.com/r/bladex/sentinel-dashboard



## Sentinel

熔断基本过程



限流基本过程？



VS 

Histrix



