# study-itv2023 #

## Lock 

Java锁机制 https://www.cnblogs.com/jtxw/p/16326065.html

 在代码层面，每个Object都有一个锁，这个锁存放在对象头中，锁记录了当前对象被哪个线程锁占用。





## LOG





## MQ

## Schedule

## Spring + Boot + Cloud 

## Mybaits + Plus 

## MySQL

MySQL 精选 60 道面试题（含答案） https://blog.csdn.net/hahazz233/article/details/125372412

常见（MySQL）面试题（含答案） https://blog.csdn.net/m0_63592370/article/details/126039076

最完整MySQL数据库面试题（2020最新版）https://zhuanlan.zhihu.com/p/112857507

https://www.ewbang.com/community/article/details/961524446.html



## JVM 

JVM 实践 

## 

## Load Test 

JMeter 工具基本使用

如何发起请求

如何校验响应

上下文连接，比如登陆到个人订单详情

结果查看

常用工具对比？



## POI Search

ES 高级使用



## Split DB&Table

Schedule:

创建DB+Table

初始化： 300万、500万、一千万、5千万、1亿的表数据

分别查询、命中主键、唯一索引、常规索引的耗时情况



查看表存储空间、表容量？



数据库分库分表

- 高频写入读取模拟【高并发条件下】
- 大数据量【千万级别】
- 单表行数超过`500万`行或者单表容量超过`2GB`，才推荐进行分库分表
- DB + ES 一起使用
- 将用户信息冗余同步到ES**，同步发送到ES，然后通过ES来查询（**推荐）



### 准备工作

Mysql 安装【Docker版本】

```
Mac-pro:	
docker run --name bage-mysql-pro -v /Users/bage/bage/docker-data/mysql-pro:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=bage -p 3306:3306 -d mysql/mysql-server
```





## 项目总结

### 分库分表

### 架构升级2.0

### 团队建设+管理

### 技术点

POI 搜索

低价、转票

邮件抓取

优惠券抵扣

出票策略



## 参考链接

Java 体系 https://github.com/whx123/JavaHome

bage面试 https://github.com/bage2014/interview

牛客网 https://www.nowcoder.com/experience/639

牛客网题库 https://www.nowcoder.com/exam/company

分库分表经典15连问 http://blog.csdn.net/BASK2311/article/details/128312076

2023Java最新真实面试题汇总（持续更新）https://blog.csdn.net/chuanchengdabing/article/details/118527318

2023年Java面试题及答案整理 https://www.rstk.cn/news/2473.html?action=onClick

2023各大厂Java面试题来喽！面试必看！ https://baijiahao.baidu.com/s?id=1757886422930954006&wfr=spider&for=pc

119道题 https://mikechen.cc/19105.html

知乎汇总 https://zhuanlan.zhihu.com/p/477276448

阿里 https://blog.csdn.net/shy111111111/article/details/129317020