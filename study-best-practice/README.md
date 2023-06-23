# study-best-practice


## MySQL

查看负载 

```
show global status
```

查看查询 

```
show processlist;
```

查看连接数

```
 SHOW GLOBAL VARIABLES WHERE Variable_name='max_connections';

 SHOW STATUS WHERE Variable_name like 'Threads_connect%';

```

调整

```
SET GLOBAL max_connections=10;
```

## WRK
wrk 压测
https://formulae.brew.sh/formula/wrk
https://github.com/wg/wrk

安装
```
brew install wrk
```

请求
This runs a benchmark for 30 seconds, using 12 threads, and keeping 400 HTTP connections open.
```
wrk -t10 -c400 -d60s http://localhost:8000/user/insert

```

Output:
```
Running 30s test @ http://127.0.0.1:8080/index.html
12 threads and 400 connections
Thread Stats   Avg      Stdev     Max   +/- Stdev
Latency   635.91us    0.89ms  12.92ms   93.69%
Req/Sec    56.20k     8.07k   62.00k    86.54%
22464657 requests in 30.00s, 17.76GB read
Requests/sec: 748868.53
Transfer/sec:    606.33MB
```

请求 实践
```
bage@bagedeMacBook-Pro ~ % wrk -t12 -c200 -d30s http://localhost:8000/user/insert

Running 30s test @ http://localhost:8000/user/insert
12 threads and 200 connections
Thread Stats   Avg      Stdev     Max   +/- Stdev
Latency   827.00ms  256.50ms   1.99s    86.84%
Req/Sec    21.02     13.00    99.00     79.95%
6705 requests in 30.10s, 1.18MB read
Socket errors: connect 0, read 89, write 0, timeout 100
Requests/sec:    222.74
Transfer/sec:     40.25KB



bage@bagedeMacBook-Pro ~ % wrk -t12 -c400 -d30s http://localhost:8000/user/insert

Running 30s test @ http://localhost:8000/user/insert
12 threads and 400 connections
Thread Stats   Avg      Stdev     Max   +/- Stdev
Latency     1.45s   349.10ms   2.00s    72.86%
Req/Sec    22.62     13.43   100.00     59.04%
7287 requests in 30.07s, 1.29MB read
Socket errors: connect 0, read 1486, write 0, timeout 788
Requests/sec:    242.33
Transfer/sec:     43.79KB


bage@bagedeMacBook-Pro ~ % wrk -t100 -c400 -d30s http://localhost:8000/user/insert

Running 30s test @ http://localhost:8000/user/insert
  100 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.33s   396.91ms   2.00s    66.59%
    Req/Sec     2.44      3.56    29.00     86.15%
  2306 requests in 30.10s, 416.61KB read
  Socket errors: connect 0, read 933, write 0, timeout 1387
Requests/sec:     76.61
Transfer/sec:     13.84KB
```


## Tomcat 

线程配置 
https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#appendix.application-properties.server

https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#application-properties.server.server.tomcat.threads.max


## Spring
https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#appendix.application-properties

## Jmeter
吞吐量上不去？

## Druid 

基本配置和使用 

https://github.com/alibaba/druid

https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter



查看监控 

http://localhost:8000/druid/index.html



http://localhost:8000/druid/sql.html





## metrics

## 准备
启动 prometheus 挂在 网络bage-net 下  
docker run --network bage-net -d --name bage-prometheus -p 9090:9090 -v /Users/bage/bage/docker-conf/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus

启动 kabana
docker run --network bage-net -d --name=bage-grafana -p 3000:3000 grafana/grafana

本地指标
http://localhost:8000/actuator/prometheus

Promethoes
http://localhost:9090

查看指标
http://localhost:9090/targets

Grafana
http://localhost:3000/

admin/admin 登陆 

配置数据源 Data Sources
http://bage-prometheus:9090

画图
sum(rate(bage_user_request_count_total[1m]))

sum_over_time(bage_user_request_count_total[1m])

文档编写目的

当前进度

后期规划

项目代办

## 参考链接

官网文档 XXX

## 快速开始

下载运行

```
Publish Remote

```

## 应用实践

下载运行

```
Publish Remote

```

## 原理解析

基本原理