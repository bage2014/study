# study-spring-boot-metrics #
study-spring-boot-metrics 学习笔记

Micrometer + Prometheus + Grafana

- 安装配置 prometheus
- 安装配置 grafana


## 参考链接 ##

micrometer [http://micrometer.io/](http://micrometer.io/)
github [https://github.com/micrometer-metrics/micrometer](https://github.com/micrometer-metrics/micrometer)
spring boot metrics https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready-metrics
指标和标签命名
http://www.coderdocument.com/docs/prometheus/v2.14/best_practices/metric_and_label_naming.html

https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/htmlsingle/#production-ready-metrics-export-prometheus

https://micrometer.io/docs/concepts

https://mehmetozkaya.medium.com/monitor-spring-boot-custom-metrics-with-micrometer-and-prometheus-using-docker-62798123c714

docker 配置访问宿主机 
https://docs.docker.com/desktop/networking/

整合过程
https://zhuanlan.zhihu.com/p/583833859

## 准备
启动 prometheus 挂在 网络bage-net 下  
docker run --network bage-net --name bage-prometheus -p 9090:9090 -v /Users/bage/bage/docker-conf/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus

启动 kabana
docker run --network bage-net -d --name=bage-grafana -p 3000:3000 grafana/grafana

## 命名转换 ##
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