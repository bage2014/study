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
  
查看当前最大连接数[默认151？]
show variables like 'max_connections';

设置最大连接数
set global max_connections=10;

查看当前连接情况
show status like 'Threads%';

```





### 过滤结果

只看错误结果树

勾选 Error 



### 常见错误

Jmeter 报错

```
java.net.SocketException: Socket closed

```

解决思路

参考链接： https://blog.csdn.net/weixin_44898291/article/details/119023467

如果在 HTTP Request Sampler 的 Basic 里勾选了Use KeepAlive，那么建议在 Advanced 页签下：

1、Implementation 选为 HttpClient4

2、Timeouts 中的 Connect 一般设置一个10~60秒的值，表示连接的空闲超时时间，避免由于没收到被压测端的响应回来的 Keep-Alive 的 Header 导致的连接断开

这个值的单位是毫秒：15s*1000=15000s


Jmeter 报错

```
java.net.SocketException: Too many open files

```

解决思路

参考链接：  https://www.jianshu.com/p/d6f7d1557f20

```
查看
launchctl limit

更新
sudo launchctl limit maxfiles 4096 unlimited
```