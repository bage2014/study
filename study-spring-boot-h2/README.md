# Spring-Boot-H2 #
study-spring-boot-h2 学习笔记

## 基本请求 
```
http://localhost:8080/customer/query?id=6

http://localhost:8080/customer/insert
```


## 启动 

```

cd /Users/bage/bage/github/study/study-spring-boot-h2


JVM 参数：
-XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation  -XX:+PrintHeapAtGC -XX:NumberOfGCLogFiles=100 -XX:GCLogFileSize=10M -Xloggc:study-spring-boot-h2/my-gc-%t.gc.log

-XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation  -XX:+PrintHeapAtGC -XX:NumberOfGCLogFiles=100 -XX:GCLogFileSize=10M -Xloggc:my-gc-%t.gc.log


jdk8 :
java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation  -XX:+PrintHeapAtGC -XX:NumberOfGCLogFiles=100 -XX:GCLogFileSize=10M -Xloggc:my-gc-%t.gc.log -jar -Xms64m -Xmx256m target/study-spring-boot-h2-0.0.1-SNAPSHOT.jar

```


## 压力请求

```
压力 插入 请求 
wrk -t10 -c50 -d90s http://localhost:8080/customer/insert

```



## 参考链接 ##

