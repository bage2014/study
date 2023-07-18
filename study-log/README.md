# study-log #
Java Log 相关知识点、体系




## 参考链接 ##

- 基本介绍 https://www.jianshu.com/p/ca3d96e64607 
- Java 原生日志 https://www.liaoxuefeng.com/wiki/1252599548343744/1264738568571776
- Log 介绍 https://www.cnblogs.com/xingele0917/p/4120320.html

日志门面 slf4j  https://www.slf4j.org/manual.html


## 背景



## java 日志 API

## 开源实现



## JUC

http://www.dtmao.cc/java/118169.html

![](https://img-blog.csdnimg.cn/abe2e4a9d3c846a595bd48147031eaa7.png)



1. 初始化LogManager
   - LogManager加载logging.properties配置
   - 添加Logger到LogManager
2. 从单例LogManager获取Logger
3. 设置级别Level，并指定日志记录LogRecord
4. Filter提供了日志级别之外更细粒度的控制
5. Handler是用来处理日志输出位置
6. Formatter是用来格式化LogRecord的



## 日志门面 

https://www.slf4j.org/manual.html



log4j2 

https://logging.apache.org/log4j/2.x/log4j-slf4j-impl.html





https://www.baeldung.com/slf4j-with-log4j2-logback

https://jabref.readthedocs.io/en/latest/adr/0002-use-slf4j-for-logging/