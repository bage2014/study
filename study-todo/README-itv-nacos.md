# study-nacos #
## 简介

Dynamic Naming and Configuration Service的首字母简称，一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。





## Key关键点

- 架构
- 服务端
- 客户端
- 监控 



## 架构



## 服务端

https://nacos.io/zh-cn/docs/v2/quickstart/quick-start.html



## 服务端Docker 

Docker 版本 https://nacos.io/zh-cn/docs/v2/quickstart/quick-start-docker.html

参考链接： https://hub.docker.com/r/nacos/nacos-server



Docker Pull Command

```
docker pull nacos/nacos-server
```

start a instance

```
docker run --name nacos-quick -e MODE=standalone -p 8849:8848 -d nacos/nacos-server:2.0.2

docker run --name bage-nacos -e MODE=standalone -p 8849:8848 -d nacos/nacos-server
```

Open the Nacos console in your browser

```
http://localhost:8849 
http://127.0.0.1:8848/nacos/
```



## 客户端

https://nacos.io/zh-cn/docs/quick-start-spring-boot.html





## 监控 ## 

https://nacos.io/zh-cn/docs/v2/guide/admin/monitor-guide.html



## 参考链接

官网 https://nacos.io/zh-cn/docs/what-is-nacos.html

github https://github.com/alibaba/nacos

