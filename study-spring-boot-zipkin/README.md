# study-spring-boot-zipkin #
Spring 集成 zipkin


## 参考链接 ##
官网 [https://zipkin.io/](https://zipkin.io/)
spring-cloud-sleuth-samples [https://github.com/spring-cloud/spring-cloud-sleuth/tree/master/spring-cloud-sleuth-samples](https://github.com/spring-cloud/spring-cloud-sleuth/tree/master/spring-cloud-sleuth-samples)



原始论文 http://bigbully.github.io/Dapper-translation/

## 启动顺序 ##

先启动 zipkin 服务端

后启动本应用的 com.bage.SampleZipkinApplication



## 访问

应用入口

```
http://localhost:3380/

http://localhost:3380/call

http://localhost:3380/traced

http://localhost:3380/start
```



zipkin

```
http://localhost:9411/

```



## 解析

### 优点

- 对应用透明，无侵入
- 稳定、高效
- 可拓展

### 组成

- collector 应用数据接收器
- storage 数据存储
- api 数据查询
- UI web页面

组成元素

- traceId ，一次完整请求的唯一标志
- id == span 的 id
- parentId 父span 的 id
- name span 的名称
- tempstamp 时间戳
- duration 耗时时长

​     

​      **annotations：基本标注列表**，一个标注可以理解成span生命周期中重要时刻的数据快照，比如一个标注中一般包含发生时刻（timestamp）、事件类型（value）、端点（endpoint）等信息

​        **事件类型**

​            **cs：客户端/消费者发起请求**

​            **cr：客户端/消费者接收到应答**

​            **sr：服务端/生产者接收到请求**

​            **ss：服务端/生产者发送应答**

​          PS：这四种事件类型的统计都应该是Zipkin提供客户端来做的，因为这些事件和业务无关，这也是为什么跟踪数据的采集适合放到中间件或者公共库来做的原因。

​      binaryAnnotations：业务标注列表，如果某些跟踪埋点需要带上部分业务数据（比如url地址、返回码和异常信息等），可以将需要的数据以键值对的形式放入到这个字段中。