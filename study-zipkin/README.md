# study-zipkin #
zipkin 使用入门

## 参考链接 ##
官网 [https://zipkin.io/](https://zipkin.io/)

## 环境搭建 ##

### server ###
- 下载 server 的代码 
下载链接 
[https://search.maven.org/remote_content?g=io.zipkin&a=zipkin-server&v=LATEST&c=exec](https://search.maven.org/remote_content?g=io.zipkin&a=zipkin-server&v=LATEST&c=exec)

- 启动
java -jar zipkin-server-2.16.2-exec.jar

- 等待客户端启动 + 访问 api 路径之后
查看链路调用情况 [http://localhost:9411/](http://localhost:9411/)

### client ###

启动 brave.webmvc.Frontend 类启动
访问 api 路径 [http://localhost:8081/api](http://localhost:8081/api)





