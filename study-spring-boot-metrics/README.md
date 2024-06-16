# study-spring-boot-metrics #
study-spring-boot-metrics 学习笔记

Micrometer + Prometheus + Grafana

- 安装配置 prometheus
- 安装配置 grafana



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



基本架构

![img](https://yunlzheng.gitbook.io/~gitbook/image?url=https%3A%2F%2F2416223964-files.gitbook.io%2F%7E%2Ffiles%2Fv0%2Fb%2Fgitbook-legacy-files%2Fo%2Fassets%252F-LBdoxo9EmQ0bJP2BuUi%252F-LPS8BVjkRvEjV8HmbBi%252F-LPS8D1gM9qp1zu_wp8y%252Fprometheus_architecture.png%3Fgeneration%3D1540234733609534%26alt%3Dmedia&width=768&dpr=4&quality=100&sign=7865ba4f75c266c01cbb2e9f5a2fd65050587962dbee1a76b77432b55f23600c)



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


### Counter ###

Gauge

DistributionSummary

LongTaskTimer

FunctionCounter

FunctionTimer

TimeGauge