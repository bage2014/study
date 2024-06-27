# study-TiDB #

## 概述

简介 https://docs.pingcap.com/tidb/stable/overview



## 快速开始

### Docker 安装

https://hub.docker.com/r/pingcap/tidb
https://developer.aliyun.com/article/138312



### Spring boot 

https://docs.pingcap.com/tidb/stable/dev-guide-sample-application-java-spring-boot
code demo https://github.com/tidb-samples/tidb-java-springboot-jpa-quickstart



## 基本CURD

https://docs.pingcap.com/tidb/stable/basic-sql-operations

https://docs.pingcap.com/tidb/stable/dev-guide-tidb-crud-sql

### DB、Table创建

DB 创建 

```sql
CREATE DATABASE mydb;

CREATE DATABASE mydb [options];
```



创建表 

```
CREATE TABLE person (
    id INT(11),
    name VARCHAR(255),
    birthday DATE
);

```



### 表操作



