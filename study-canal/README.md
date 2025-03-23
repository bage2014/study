# Study-canal



## 参考链接

官网文档  https://github.com/alibaba/canal
java https://github.com/alibaba/canal/wiki/ClientExample



## 快速开始
https://github.com/alibaba/canal/wiki/Docker-QuickStart




下载运行

下载

```
docker pull canal/canal-server:v1.1.1
```

运行

```
# 构建一个destination name为test的队列
sh run.sh -e canal.auto.scan=false \
		  -e canal.destinations=test \
		  -e canal.instance.master.address=127.0.0.1:3306  \
		  -e canal.instance.dbUsername=canal  \
		  -e canal.instance.dbPassword=canal  \
		  -e canal.instance.connectionCharset=UTF-8 \
		  -e canal.instance.tsdb.enable=true \
		  -e canal.instance.gtidon=false  \ 
```

## 应用实践

下载运行

```
Publish Remote

```

## 原理解析

基本原理



## 参考链接

