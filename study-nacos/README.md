# study-nacos #
## 简介

Dynamic Naming and Configuration Service的首字母简称，一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。



## 服务端部署

https://nacos.io/zh-cn/docs/v2/quickstart/quick-start.html



## 服务端Docker 

Docker 版本 https://nacos.io/zh-cn/docs/v2/quickstart/quick-start-docker.html

参考链接： https://hub.docker.com/r/nacos/nacos-server



Docker Pull Command

```
docker pull nacos/nacos-server

[Mac M1 版本]
docker pull nacos/nacos-server:v2.1.2-slim
```

start a instance

```
docker run --name bage-nacos -e MODE=standalone -p 8849:8848 -d nacos/nacos-server:2.0.2

[mac m1]
docker run --name bage-nacos -e MODE=standalone -p 8848:8848 -p 9848:9848 -d nacos/nacos-server:v2.1.2-slim

```

Open the Nacos console in your browser

```
http://localhost:8848

http://127.0.0.1:8848/nacos/
```



## 客户端

https://nacos.io/zh-cn/docs/quick-start-spring-boot.html

https://segmentfault.com/a/1190000042043050/en

https://blog.csdn.net/keypanj2ee/article/details/118725971 配置多一个外部端口

1. 启动 `NacosConfigApplication`，调用 `curl http://localhost:8080/config/get`，返回内容是 `false`。

2. 通过调用 [Nacos Open API](https://nacos.io/zh-cn/docs/open-api.html) 向 Nacos server 发布配置：dataId 为`example`，内容为`useLocalCache=true`

   ```
   curl -X POST "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=example&group=DEFAULT_GROUP&content=useLocalCache=true"
   
   curl -X POST "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=example&group=DEFAULT_GROUP&content=useLocalCache=true"
   ```

3. 再次访问 `http://localhost:8080/config/get`，此时返回内容为`true`，说明程序中的`useLocalCache`值已经被动态更新了。

4. 获取内容dataId 为`example`，内容为`useLocalCache=true`

```
curl -X GET 'http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=example&group=DEFAULT_GROUP'


curl -X POST "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=example&group=DEFAULT_GROUP&content=hello=bage" 
```



## 监控 ## 

https://nacos.io/zh-cn/docs/v2/guide/admin/monitor-guide.html



## 参考链接

官网 https://nacos.io/zh-cn/docs/what-is-nacos.html

github https://github.com/alibaba/nacos

Nacos 基本操作 https://nacos.io/zh-cn/docs/nacos-spring.html

