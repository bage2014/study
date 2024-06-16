# study-spring-boot-metrics #
study-spring-boot-metrics 学习笔记

Micrometer + Prometheus + Grafana

- 安装配置 prometheus
- 安装配置 grafana



## 参考链接 ##

https://micrometer.io/docs/concepts

promethos 

基本语法 https://prometheus.io/docs/prometheus/latest/getting_started/

书籍 https://github.com/yunlzheng/prometheus-book?tab=readme-ov-file、 https://yunlzheng.gitbook.io/prometheus-book



micrometer [http://micrometer.io/](http://micrometer.io/)
github [https://github.com/micrometer-metrics/micrometer](https://github.com/micrometer-metrics/micrometer)
spring boot metrics https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready-metrics
指标和标签命名

https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/htmlsingle/#production-ready-metrics-export-prometheus

https://mehmetozkaya.medium.com/monitor-spring-boot-custom-metrics-with-micrometer-and-prometheus-using-docker-62798123c714

docker 配置访问宿主机 
https://docs.docker.com/desktop/networking/

整合过程
https://zhuanlan.zhihu.com/p/583833859

## 准备
启动 prometheus 挂在 网络bage-net 下  

```
docker run --network bage-net --name bage-prometheus -p 9090:9090 -v /Users/bage/bage/docker-conf/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus
```

启动 kabana

```
docker run --network bage-net -d --name=bage-grafana -p 3000:3000 grafana/grafana

```

## 命名转换

http.server.requests

Prometheus - http_server_requests_duration_seconds。
Atlas - httpServerRequests。
Graphite - http.server.requests。
InfluxDB - http_server_requests。

http://localhost:8080/timer/cost
http://localhost:8080/counter/incr
http://localhost:8080/order

http://localhost:8080/actuator/prometheus


http://localhost:9090

查看指标
http://localhost:9090/targets

http://localhost:3000
admin/admin

## 常用指标 ##

### Timer ###

查看接口请求耗时

http://localhost:8080/timer/cost

```
rate(bage_timer_seconds_sum[1m]) / rate(bage_timer_seconds_count[1m])
```



### Counter ###

查看请求平均次数

http://localhost:8080/counter/incr

```
bage_timer_seconds_sum

```

Gauge

DistributionSummary

LongTaskTimer

FunctionCounter

FunctionTimer

TimeGauge







## Prometheus

### 基本概念

基于Golang编写

新一代的云原生监控系统

是一个开源的完整监控解决方案，其对传统监控系统的测试和告警模型进行了彻底的颠覆，形成了基于中央化的规则计算、统一分析和告警的新模型

一般会结合grafana 一起使用，用于数据的UI展示 

### 使用优势

- 独立精巧，不依赖第三方，唯一需要的就是本地磁盘
- 基于Pull模型的架构方式，可以在任何地方（本地电脑，开发环境，测试环境）搭建我们的监控系统
- 基于系统内部、业务监控；易于集成，提供了丰富的client进行监控
- 强大的数据模型，基于时间序列数据库(TSDB)存储
- 查询语言PromQL，好实现聚合查询
- 可视化、监控管理后台UI
- 高效+可拓展+开放性



### 基本架构

#### Prometheus Server

负责数据的承接，存储并且对外提供数据查询支持

提供UI 能力

#### Node Exporter

采集主机的运行指标如CPU, 内存，磁盘等信息

业务埋点信息收集

一般分为两类：直接采集、间接采集



#### AlertManager

告警管理



#### PushGateway

当Prometheus Server与Exporter无法直接进行通信时，就可以利用PushGateway来进行中转。可以通过PushGateway将内部网络的监控数据主动Push到Gateway当中



基本架构

![img](https://yunlzheng.gitbook.io/~gitbook/image?url=https%3A%2F%2F2416223964-files.gitbook.io%2F%7E%2Ffiles%2Fv0%2Fb%2Fgitbook-legacy-files%2Fo%2Fassets%252F-LBdoxo9EmQ0bJP2BuUi%252F-LPS8BVjkRvEjV8HmbBi%252F-LPS8D1gM9qp1zu_wp8y%252Fprometheus_architecture.png%3Fgeneration%3D1540234733609534%26alt%3Dmedia&width=768&dpr=4&quality=100&sign=7865ba4f75c266c01cbb2e9f5a2fd65050587962dbee1a76b77432b55f23600c)



### 时间序列-样本

在time-series中的每一个点称为一个样本（sample），样本由以下三部分组成：

- 指标(metric)：metric name和描述当前样本特征的labelsets;
- 时间戳(timestamp)：一个精确到毫秒的时间戳;
- 样本值(value)： 一个float64的浮点型数据表示当前样本的值。

```
<--------------- metric ---------------------><-timestamp -><-value->
http_request_total{status="200", method="GET"}@1434417560938 => 94355
http_request_total{status="200", method="GET"}@1434417561287 => 94334

http_request_total{status="404", method="GET"}@1434417560938 => 38473
http_request_total{status="404", method="GET"}@1434417561287 => 38544

http_request_total{status="200", method="POST"}@1434417560938 => 4748
http_request_total{status="200", method="POST"}@1434417561287 => 4785
```



### 指标

以下两种方式均表示的同一条time-series

```
api_http_requests_total{method="POST", handler="/messages"}
```

等同于：

```
{__name__="api_http_requests_total"，method="POST", handler="/messages"}
```



### 基本查询 

**匹配**

PromQL支持使用`=`和`!=`两种完全匹配模式：

- = 匹配，通过使用`label=value`可以选择那些标签满足表达式定义的时间序列；
- != 不匹配，使用`label!=value`则可以根据标签匹配排除时间序列；

```
http_requests_total{instance="localhost:9090"}
http_requests_total{instance!="localhost:9090"}

```

**正则表达式**

PromQL还可以支持使用正则表达式作为匹配条件，多个表达式之间使用`|`进行分离：

- 使用`label=~regx`表示选择那些标签符合正则表达式定义的时间序列；
- 反之使用`label!~regx`进行排除；

例如，如果想查询多个环节下的时间序列序列可以使用如下表达式：

```
http_requests_total{environment=~"staging|testing|development",method!="GET"}
```

**范围查询**

通过以下表达式可以选择最近5分钟内的所有样本数据：

```
http_requests_total{}[5m]
```

除了使用m表示分钟以外，PromQL的时间范围选择器支持其它时间单位：

- s - 秒
- m - 分钟
- h - 小时
- d - 天
- w - 周
- y - 年

**聚合操作**

```
# 查询系统所有http请求的总量
sum(http_request_total)

# 按照mode计算主机CPU的平均使用时间
avg(node_cpu) by (mode)

# 按照主机查询各个主机的CPU使用率
sum(sum(irate(node_cpu{mode!='idle'}[5m]))  / sum(irate(node_cpu[5m]))) by (instance)
```

**基本操作**

PromQL支持的所有数学运算符如下所示：

- `+` (加法)
- `-` (减法)
- `*` (乘法)
- `/` (除法)
- `%` (求余)
- `^` (幂运算)

Prometheus支持以下布尔运算符如下：

- `==` (相等)
- `!=` (不相等)
- `>` (大于)
- `<` (小于)
- `>=` (大于等于)
- `<=` (小于等于)



### 聚合函数

Prometheus还提供了下列内置的聚合操作符，这些操作符作用域瞬时向量。可以将瞬时表达式返回的样本数据进行聚合，形成一个新的时间序列。

- `sum` (求和)
- `min` (最小值)
- `max` (最大值)
- `avg` (平均值)
- `stddev` (标准差)
- `stdvar` (标准方差)
- `count` (计数)
- `count_values` (对value进行计数)
- `bottomk` (后n条时序)
- `topk` (前n条时序)
- `quantile` (分位数)

规则：

```
<aggr-op>([parameter,] <vector expression>) [without|by (<label list>)]
```

例如：

```
sum(http_requests_total) without (instance)
```

等价于：

```
sum(http_requests_total) by (code,handler,job,method)
```