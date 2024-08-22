# study-Loki


## 参考链接

官网文档

https://grafana.com/docs/loki/latest/

https://hub.docker.com/r/grafana/loki

## 快速开始

下载运行 【Docker 版本 】

```
docker pull grafana/loki
```



## 应用实践



### Grafana 集成

https://grafana.com/docs/grafana/latest/datasources/loki/



### Spring Boot 集成

https://mp.weixin.qq.com/s/Z03k3nvUEdzdSxzA2Z52dA

https://www.baeldung.com/spring-boot-loki-grafana-logging

https://grafana.com/docs/grafana-cloud/connect-externally-hosted/data-sources/loki/#configure-derived-fields

```
{traceId='467242cf-6ab0-4824-a0d7-20a85f402154'} | json
```



### 日志查询 

写入日志

```
http://localhost:8080/hello?name=hhhffsghddd
```

日志查询

```
http://localhost:3000/explore

Log browser
{app="name_IS_UNDEFINED"}

{traceId='fdead198-adf2-4e3f-aa7c-a03462be2aff'} | json
```

字段解析

https://grafana.com/docs/grafana-cloud/connect-externally-hosted/data-sources/loki/#configure-derived-fields

```
"traceId": "(\w{8}(-\w{4}){3}-\w{12})"

datasourceUid: my_jaeger_uid
          matcherRegex: "traceId=(\\w+)"
          name: traceId
          # url will be interpreted as query for the datasource
          url: '$${__value.raw}'
          # optional for URL Label to set a custom display label for the link.
          urlDisplayLabel: 'View Trace'
```



### 应用埋点





## 原理解析

基本原理
