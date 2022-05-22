# study-jmeter-MySQL #
study-jmeter-MySQL学习笔记

## 参考链接 ##



## 简单压测

### 数据库压测

请求入口

http://localhost:8080/mysql/query?key=josh

数据库连接数

```
登陆 root 
mysql -uroot -p
  
查看当前最大连接数
show variables like 'max_connections';

设置最大连接数
set global max_connections=10;

查看当前连接情况
show status like 'Threads%';

```




