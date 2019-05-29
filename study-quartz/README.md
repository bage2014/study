# study-quartz  #
学习 Quartz 的笔记

quartz的简单使用
+ Spring Boot + Mybatis
持久化到数据库
任务的增删改查维护，动态启动暂停关闭等

## 参考链接 ##

官网网址 [http://www.quartz-scheduler.org/documentation/quartz-2.1.x/quick-start.html](http://www.quartz-scheduler.org/documentation/quartz-2.1.x/quick-start.html)
发行版本 [https://github.com/quartz-scheduler/quartz/releases](https://github.com/quartz-scheduler/quartz/releases)
QuickStart [https://github.com/quartz-scheduler/quartz/blob/master/docs/quick-start-guide.adoc](https://github.com/quartz-scheduler/quartz/blob/master/docs/quick-start-guide.adoc)

### 企业配置 ###
配置数据源 [http://www.quartz-scheduler.org/documentation/quartz-2.3.0/configuration/ConfigDataSources.html](http://www.quartz-scheduler.org/documentation/quartz-2.3.0/configuration/ConfigDataSources.html)

详细文档 [https://github.com/quartz-scheduler/quartz/blob/master/docs/index.adoc](https://github.com/quartz-scheduler/quartz/blob/master/docs/index.adoc)

Samples [https://github.com/quartz-scheduler/quartz/blob/master/docs/examples/index.adoc](https://github.com/quartz-scheduler/quartz/blob/master/docs/examples/index.adoc)

[https://github.com/ChamithKodikara/quartz-demo](https://github.com/ChamithKodikara/quartz-demo)


### 使用 ###
- 程序启动类 com.bage.jdbc.springboot.QuartzDemoApplication
- 开始一个job [http://localhost:8080/scheduler/start](http://localhost:8080/scheduler/start)
- 暂停一个job [http://localhost:8080/scheduler/pause](http://localhost:8080/scheduler/pause)