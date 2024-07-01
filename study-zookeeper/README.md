# Study-Zookeeper #

Zookeeper


- 配置管理 
- 分布式锁



## 快速入门

### 基本安装

todo

### Docker 安装

https://github.com/bage2014/study/tree/master/study-docker



### 基本命令

Connecting to ZooKeeper

```
bin/zkCli.sh -server 127.0.0.1:2181
```

查看

```
ls /


```

创建

```
create /zk_test my_data

```

查看 ， Next, verify that the data was associated with the znode by running the `get` command, as in:

```
[zkshell: 12] get /zk_test
my_data
cZxid = 5
ctime = Fri Jun 05 13:57:06 PDT 2009
mZxid = 5
mtime = Fri Jun 05 13:57:06 PDT 2009
pZxid = 5
cversion = 0
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0
dataLength = 7
numChildren = 0
```

We can change the data associated with zk_test by issuing the `set` command, as in:

```
[zkshell: 14] set /zk_test junk
cZxid = 5
ctime = Fri Jun 05 13:57:06 PDT 2009
mZxid = 6
mtime = Fri Jun 05 14:01:52 PDT 2009
pZxid = 5
cversion = 0
dataVersion = 1
aclVersion = 0
ephemeralOwner = 0
dataLength = 4
numChildren = 0
[zkshell: 15] get /zk_test
junk
cZxid = 5
ctime = Fri Jun 05 13:57:06 PDT 2009
mZxid = 6
mtime = Fri Jun 05 14:01:52 PDT 2009
pZxid = 5
cversion = 0
dataVersion = 1
aclVersion = 0
ephemeralOwner = 0
dataLength = 4
numChildren = 0
```

(Notice we did a `get` after setting the data and it did, indeed, change.

Finally, let's `delete` the node by issuing:

```
[zkshell: 16] delete /zk_test
[zkshell: 17] ls /
[zookeeper]
[zkshell: 18]
```







## 关键点

- 数据模型
- 节点Node



## 数据模型

- 树状结构



## 节点Node

持久化 vs 临时 + 顺序 vs 非顺序

- 持久化节点
- 临时节点
- 持久化-顺序节点
- 临时-顺序节点



## 参考链接

官方文档 https://zookeeper.apache.org/doc/r3.9.2/index.html
java 访问 https://www.baeldung.com/java-zookeeper


## Bilibili

