# study-itv2023 #



## Load test 

JMeter 工具基本使用

如何发起请求

如何校验响应

上下文连接，比如登陆到个人订单详情

结果查看

常用工具对比？



## POI Search

ES 高级使用



## split db&tl

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





## 参考链接

Java 体系 https://github.com/whx123/JavaHome

分库分表经典15连问 http://blog.csdn.net/BASK2311/article/details/128312076


