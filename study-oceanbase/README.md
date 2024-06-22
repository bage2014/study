
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

https://www.oceanbase.com/docs/common-oceanbase-cloud-10000000001780095

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
docker run -p 2881:2881 -v $PWD/bage/docker-data/oceanbase/ob:/root/ob -v $PWD/bage/docker-data/oceanbase/obd:/root/.obd --name bage-oceanbase-ce -d oceanbase/oceanbase-ce

连接 
# 使用 sys 用户登录集群的 sys 租户
docker exec -it bage-oceanbase-ce ob-mysql sys 

# 使用 root 用户登录集群的 root 租户
docker exec -it bage-oceanbase-ce ob-mysql root 

# 使用 test 用户登录集群的 test 租户
docker exec -it bage-oceanbase-ce ob-mysql test 
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

// 准备数据
INSERT customer(id,first_name,last_name) VALUES(1,'lu','bage'),(2,'lu2','bage2');

// 查看数据
select * from customer;

```



## 常见问题

```
org.hibernate.HibernateException: Access to DialectResolutionInfo cannot be null when 'hibernate.dialect' not set
```





```
Caused by: java.lang.ClassNotFoundException: com.fasterxml.jackson.databind.exc.InvalidDefinitionException
```



## 参考链接

https://stackoverflow.com/questions/26548505/org-hibernate-hibernateexception-access-to-dialectresolutioninfo-cannot-be-null





https://stackoverflow.com/questions/49813666/table-dbname-hibernate-sequence-doesnt-exist