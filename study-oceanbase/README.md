
# study-ocean base

## 基本使用

参考链接：

https://open.oceanbase.com/docs/observer-cn/V3.1.3/10000000000096649

基本使用

https://www.oceanbase.com/docs/community-observer-cn-10000000000014908

https://www.oceanbase.com/docs/community-observer-cn-10000000000014793 

Docker 

https://open.oceanbase.com/quickStart

集成 Spring boot  

https://en.oceanbase.com/docs/common-oceanbase-cloud-10000000001053560 
https://en.oceanbase.com/docs/common-oceanbase-database-10000000001106148 
https://help.aliyun.com/document_detail/2249866.html



## 基本概念



## 环境部署 

### docker版本

```
下载
docker pull oceanbase/oceanbase-ce

部署
docker run -p 2881:2881 --name bage-oceanbase-ce -d oceanbase/oceanbase-ce

连接 
docker exec -it bage-oceanbase-ce ob-mysql sys # 使用 root 用户登录集群的 sys 租户
docker exec -it bage-oceanbase-ce ob-mysql root # 使用 root 用户登录集群的 test 租户
docker exec -it bage-oceanbase-ce ob-mysql test # 使用 test 用户登录集群的 test 租户
```



## CRUD

```
// 创建DB 
CREATE DATABASE mydb DEFAULT CHARACTER SET UTF8;

use mydb;

// 创建表 
CREATE TABLE customer (id int primary key, first_name VARCHAR(64), last_name VARCHAR(64));

// 查看表
DESCRIBE customer;

// 查看数据
INSERT customer(id,first_name,last_name) VALUES(1,'lu','bage'),(2,'lu2','bage2');


```



