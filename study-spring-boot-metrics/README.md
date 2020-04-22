# study-spring-boot-metrics #
study-spring-boot-metrics 学习笔记

Micrometer + Prometheus + Grafana


## 参考链接 ##

micrometer [http://micrometer.io/](http://micrometer.io/)
github [https://github.com/micrometer-metrics/micrometer](https://github.com/micrometer-metrics/micrometer)
spring boot metrics [https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready-metrics](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready-metrics)
指标和标签命名
http://www.coderdocument.com/docs/prometheus/v2.14/best_practices/metric_and_label_naming.html

## 命名转换 ##
http.server.requests

Prometheus - http_server_requests_duration_seconds。
Atlas - httpServerRequests。
Graphite - http.server.requests。
InfluxDB - http_server_requests。

http://localhost:8080/actuator/prometheus
http://localhost:8080/timer/cost
http://localhost:8080/counter/incr

## 常用指标 ##

### Timer ###


### Counter ###

Gauge

DistributionSummary

LongTaskTimer

FunctionCounter

FunctionTimer

TimeGauge