# study-jmeter #
study-jmeter学习笔记

MySQL 压力测试:  [README-MySQL.md](README-MySQL.md)

## 参考链接 ##
- 官网 [http://jmeter.apache.org/](http://jmeter.apache.org/)
- 中文网 http://www.jmeter.com.cn/
- 入门 https://jmeter.apache.org/usermanual/get-started.html

## 环境搭建 ##
安装jdk后，下载解压直接打开 /bin/jmeter.bat

## 关键点

- 快速入门
- 断言、数据模拟、登陆态、重定向、缓存、页面交互
- 结果查看、结果分析
- 高阶配置项

## Quick Start

基本使用

https://jmeter.apache.org/usermanual/build-web-test-plan.html

高阶使用

https://jmeter.apache.org/usermanual/build-adv-web-test-plan.html



## 简单使用 ##

### 启动 ###
打开 `/bin/jmeter.bat`

### 添加线程组 ###
hello-jmeter -> 添加 -> Threads(User) -> 线程组

线程数：100
Ramp-Up Period(in seconds)：10
循环次数：永远，**设置调度器**，持续时间 30，启动延迟 3

Rame-Up Period(in seconds):表示JMeter每隔多少秒发动并发。理解成准备时长：设置虚拟用户数需要多长时间全部启动。如果线程数是20，准备时长为10，那么需要10秒钟启动20个数量，也就是每秒钟启动2个线程。

如果线程数为200 ，循环次数为10 ，那么每个线程发送10次请求。总请求数为200*10=2000 。如果勾选了“永远”，那么所有线程会一直发送请求，直到选择停止运行脚本。

### 添加HTTP请求 ###
右键 【添加】-->【配置元件】-->【HTTP请求默认值】

协议：http
服务器名称或IP：127.0.0.1
端口号：8080
路径：/hello

### 添加察看结果树 ###
右键 【添加】-->【监听器】-->【察看结果树】。

### 聚合报告 ###
右键 【添加】-->【监听器】-->【聚合报告】。
聚合报告各指标：

Label：每个 JMeter 的 element（例如 HTTP Request）都有一个 Name 属性，这里显示的就是 Name 属性的值

Samples：表示你这次测试中一共发出了多少个请求，如果模拟10个用户，每个用户迭代10次，那么这里显示100

Average：平均响应时间——默认情况下是单个 Request 的平均响应时间，单位为毫秒。当使用了 Transaction Controller 时，也可以以Transaction 为单位显示平均响应时间

Median：中位数，也就是 50％ 用户的响应时间

90% Line：90％ 用户的响应时间

Min：最小响应时间

Max：最大响应时间

Error%：本次测试中出现错误的请求的数量/请求的总数

Throughput：吞吐量——默认情况下表示每秒完成的请求数（Request per Second），当使用了 Transaction Controller 时，也可以表示类似 LoadRunner 的 Transaction per Second 数

KB/Sec：每秒从服务器端接收到的数据量，相当于LoadRunner中的Throughput/Sec

### 启动 ###
右键 【启动】，即可查看测试情况

## 常见错误 ##

### 参考链接 ###
- JMeter测试问题:departmentAddress already in use [https://blog.csdn.net/ceo158/article/details/9311065](https://blog.csdn.net/ceo158/article/details/9311065)
- JMeter Exception: java.net.BindException: Address already in use: connect [http://twit88.com/blog/2008/07/28/jmeter-exception-javanetbindexception-departmentAddress-already-in-use-connect/](http://twit88.com/blog/2008/07/28/jmeter-exception-javanetbindexception-departmentAddress-already-in-use-connect/)
- Address already in use : connect 的解决办法 [https://blog.csdn.net/qq_31441637/article/details/80422901](https://blog.csdn.net/qq_31441637/article/details/80422901)



## 入门

https://jmeter.apache.org/usermanual/index.html



### 前置知识 

Thread Group, 

HTTP Request, 

HTTP Request Defaults, 

and Graph Results.

Synchronizing Timer 并发数

Test Plan -> Thread Group -> HTTP Request

### Building a Test Plan

基础 web 请求 https://jmeter.apache.org/usermanual/build-web-test-plan.html

高阶 web 请求  https://jmeter.apache.org/usermanual/build-adv-web-test-plan.html



### Cookie 支持

 [HTTP Cookie Manager](https://jmeter.apache.org/usermanual/component_reference.html#HTTP_Cookie_Manager) 



### 结果监听

https://jmeter.apache.org/usermanual/component_reference.html#listeners



### 多用户模拟请求



### 请求头

[HTTP Header Manager](https://jmeter.apache.org/usermanual/component_reference.html#HTTP_Header_Manager) 



### 报告

report 生成 https://jmeter.apache.org/usermanual/generating-dashboard.html



### 变量

User variables



### 自带函数

https://jmeter.apache.org/usermanual/functions.html



## 同步器

可以积攒请求，达到并发后在一起发起

Synchronizing Timer 设置每分钟 请求次数？【PQS 】

Constant Throughput Timer 设置每分钟 请求次数？【PQS 】

## Plugins 
插件管理 https://jmeter-plugins.org/wiki/PluginsManager/
插件 https://jmeter-plugins.org/wiki/ThroughputShapingTimer/

## Bilibili

https://www.bilibili.com/video/BV1Ux4y1L7Lj/?spm_id_from=333.337.search-card.all.click&vd_source=72424c3da68577f00ea40a9e4f9001a1