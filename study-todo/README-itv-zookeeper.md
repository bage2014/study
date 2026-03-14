# Study-Zookeeper #

## 背景

Zookeeper是Apache基金会下的一个分布式协调服务，最初由Yahoo开发，用于解决分布式系统中的一致性问题。在分布式环境中，各个节点需要协调工作，例如配置管理、命名服务、分布式锁、集群管理等，Zookeeper提供了一个可靠的、高性能的、可扩展的解决方案。

Zookeeper的设计目标是：
- 简单：提供简单易用的API
- 可靠：保证数据一致性和可靠性
- 高性能：支持高并发操作
- 可扩展：支持集群部署

## 使用场景

1. **配置管理**：集中管理分布式应用的配置信息，实现配置的动态更新
2. **分布式锁**：实现分布式环境下的锁机制，保证资源的互斥访问
3. **命名服务**：为分布式系统中的节点提供唯一的命名
4. **集群管理**：监控集群节点的状态，实现集群的自动发现和管理
5. **领导选举**：在分布式系统中选举出领导者节点
6. **分布式队列**：实现分布式环境下的队列功能

## 精品分析

### Zookeeper在大数据生态中的应用
- **Hadoop**：使用Zookeeper管理NameNode的高可用性
- **Kafka**：使用Zookeeper存储broker信息和消费者组状态
- **HBase**：使用Zookeeper管理RegionServer的状态和集群配置
- **Storm**：使用Zookeeper协调Nimbus和Supervisor的工作

### Zookeeper的优势
- **简单易用**：提供简单的API接口，易于集成
- **可靠性高**：基于ZAB协议保证数据一致性
- **性能优异**：支持高并发读写操作
- **容错能力**：支持集群部署，单个节点故障不影响整体服务

## 基本原理

### 核心概念

#### 数据模型
Zookeeper采用树形结构（类似文件系统）来存储数据，每个节点称为znode。znode可以存储少量数据（默认最大1MB），并且可以有子节点。

#### 节点类型
- **持久化节点（PERSISTENT）**：创建后一直存在，直到被显式删除
- **临时节点（EPHEMERAL）**：创建者会话结束后自动删除
- **持久化顺序节点（PERSISTENT_SEQUENTIAL）**：持久化节点，且会自动添加递增的顺序号
- **临时顺序节点（EPHEMERAL_SEQUENTIAL）**：临时节点，且会自动添加递增的顺序号

#### 会话（Session）
客户端与Zookeeper服务器建立的连接称为会话。会话有超时机制，当客户端长时间无响应时，会话会被关闭，临时节点也会被删除。

#### 监听（Watch）
客户端可以对znode设置监听，当znode发生变化时，Zookeeper会通知客户端。监听是一次性的，触发后需要重新设置。

### 核心思想

#### ZAB协议
Zookeeper采用ZAB（ZooKeeper Atomic Broadcast）协议来保证数据一致性。ZAB协议分为两个阶段：
1. **崩溃恢复**：当 leader 节点崩溃时，重新选举新的 leader，并确保所有节点的数据一致
2. **原子广播**：leader 节点将变更广播给所有 follower 节点，确保所有节点的状态一致

#### 一致性保证
Zookeeper保证以下特性：
- **顺序一致性**：客户端的更新请求按顺序执行
- **原子性**：更新要么成功，要么失败，不会部分执行
- **单一视图**：客户端看到的是相同的服务视图
- **可靠性**：一旦更新成功，结果会被持久化
- **及时性**：客户端能在一定时间内看到最新的数据

## 注意事项

### 性能考虑
- **数据量限制**：每个znode的数据大小限制为1MB，不适合存储大量数据
- **会话管理**：合理设置会话超时时间，避免频繁重连
- **监听机制**：避免设置过多的监听，防止网络拥塞

### 可靠性考虑
- **集群部署**：生产环境建议部署3个或5个节点的集群
- **数据备份**：定期备份Zookeeper的数据
- **监控**：监控Zookeeper的运行状态，及时发现问题

### 最佳实践
- **合理设计节点结构**：根据业务需求设计合理的节点层次结构
- **使用顺序节点**：利用顺序节点实现分布式锁、队列等功能
- **注意会话管理**：确保客户端正确处理会话过期的情况
- **避免过度使用**：Zookeeper适合协调服务，不适合作为数据存储

## 快速入门

### 基本安装

1. 下载Zookeeper安装包
2. 解压到指定目录
3. 复制配置文件：`cp conf/zoo_sample.cfg conf/zoo.cfg`
4. 启动服务：`bin/zkServer.sh start`
5. 停止服务：`bin/zkServer.sh stop`

### Docker 安装

```bash
docker run -d --name zookeeper -p 2181:2181 zookeeper:3.7
```

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

## 面试题目及解题思路

### 1. 什么是Zookeeper？它的主要功能是什么？

**解题思路**：
- 首先解释Zookeeper的定义和定位
- 然后列举其主要功能
- 最后简要说明其在分布式系统中的作用

**参考答案**：
Zookeeper是一个分布式协调服务，用于解决分布式系统中的一致性问题。它的主要功能包括：
- 配置管理：集中管理分布式应用的配置信息
- 分布式锁：实现分布式环境下的锁机制
- 命名服务：为分布式系统中的节点提供唯一命名
- 集群管理：监控集群节点的状态
- 领导选举：在分布式系统中选举领导者节点
- 分布式队列：实现分布式环境下的队列功能

### 2. Zookeeper的数据模型是什么样的？

**解题思路**：
- 描述Zookeeper的数据模型结构
- 解释节点类型及其特点
- 说明数据存储的限制

**参考答案**：
Zookeeper采用树形结构（类似文件系统）来存储数据，每个节点称为znode。数据模型的特点：
- 树状结构：根节点为"/"，每个节点可以有子节点
- 节点类型：包括持久化节点、临时节点、持久化顺序节点、临时顺序节点
- 数据存储：每个节点可以存储少量数据（默认最大1MB）
- 节点属性：每个节点有版本号、时间戳等属性

### 3. Zookeeper如何保证数据一致性？

**解题思路**：
- 解释ZAB协议的工作原理
- 说明崩溃恢复和原子广播两个阶段
- 解释Zookeeper的一致性保证

**参考答案**：
Zookeeper采用ZAB（ZooKeeper Atomic Broadcast）协议来保证数据一致性。ZAB协议包括两个阶段：
1. **崩溃恢复**：当leader节点崩溃时，重新选举新的leader，并确保所有节点的数据一致
2. **原子广播**：leader节点将变更广播给所有follower节点，确保所有节点的状态一致

Zookeeper保证以下一致性特性：
- 顺序一致性：客户端的更新请求按顺序执行
- 原子性：更新要么成功，要么失败
- 单一视图：客户端看到的是相同的服务视图
- 可靠性：一旦更新成功，结果会被持久化
- 及时性：客户端能在一定时间内看到最新的数据

### 4. 如何使用Zookeeper实现分布式锁？

**解题思路**：
- 说明分布式锁的实现原理
- 解释使用临时顺序节点的优势
- 描述具体的实现步骤

**参考答案**：
使用Zookeeper实现分布式锁的步骤：
1. 在指定路径下创建临时顺序节点
2. 获取该路径下的所有子节点，并按顺序排序
3. 如果创建的节点是第一个节点，则获得锁
4. 如果不是第一个节点，则监听前一个节点的删除事件
5. 当前一个节点被删除时，重新检查自己是否是第一个节点
6. 释放锁时，删除创建的临时节点

使用临时顺序节点的优势：
- 临时节点：当客户端会话结束时自动释放锁
- 顺序节点：避免惊群效应，只有前一个节点释放锁时才通知下一个节点

### 5. Zookeeper的集群架构是什么样的？

**解题思路**：
- 描述Zookeeper的集群结构
- 解释leader和follower的角色
- 说明集群的容错能力

**参考答案**：
Zookeeper集群采用主从架构：
- **Leader**：负责处理客户端的写请求，维护数据一致性
- **Follower**：负责处理客户端的读请求，参与投票选举
- **Observer**：只处理读请求，不参与投票选举，提高读性能

集群的容错能力：
- 集群规模一般为奇数（3、5、7等）
- 只要超过半数节点存活，集群就能正常工作
- 例如，3节点集群可以容忍1个节点故障，5节点集群可以容忍2个节点故障

### 6. Zookeeper的会话机制是怎样的？

**解题思路**：
- 解释会话的概念和生命周期
- 说明会话超时的处理机制
- 描述会话与临时节点的关系

**参考答案**：
Zookeeper的会话机制：
- **会话创建**：客户端与服务器建立连接时创建会话，获得会话ID
- **会话状态**：包括CONNECTING、CONNECTED、CLOSED、EXPIRED
- **会话超时**：客户端需要定期发送心跳包，否则会话会超时
- **临时节点**：会话结束时，该会话创建的临时节点会被自动删除

会话超时时间的设置：
- 默认会话超时时间为10秒
- 可以根据业务需求调整，但不宜过长或过短
- 过长会导致临时节点释放延迟，过短会导致会话频繁断开

### 7. Zookeeper的监听机制是怎样的？

**解题思路**：
- 解释监听的概念和类型
- 说明监听的触发条件
- 描述监听的特点和注意事项

**参考答案**：
Zookeeper的监听机制：
- **监听类型**：包括节点数据变更、子节点变更、节点删除
- **触发条件**：当监听的事件发生时，Zookeeper会通知客户端
- **特点**：监听是一次性的，触发后需要重新设置
- **注意事项**：
  - 避免设置过多监听，防止网络拥塞
  - 监听通知是异步的，客户端需要处理通知延迟的情况
  - 重新设置监听时可能会错过期间的变更

### 8. Zookeeper与Redis、Consul等其他协调服务的区别是什么？

**解题思路**：
- 比较Zookeeper与其他协调服务的特点
- 分析各自的优势和适用场景
- 说明选择的依据

**参考答案**：
Zookeeper与其他协调服务的区别：

| 特性 | Zookeeper | Redis | Consul |
|------|-----------|-------|--------|
| 数据模型 | 树形结构 | 键值对 | 服务注册与发现 |
| 一致性保证 | 强一致性（ZAB协议） | 最终一致性 | 强一致性（Raft协议） |
| 适用场景 | 配置管理、分布式锁 | 缓存、消息队列 | 服务发现、健康检查 |
| 性能 | 高并发读，写性能一般 | 高并发读写 | 中等性能 |
| 生态系统 | 成熟，广泛应用于大数据生态 | 广泛应用于缓存 | 新兴，专注于服务网格 |

选择依据：
- 如果需要强一致性和成熟的生态，选择Zookeeper
- 如果需要高性能缓存和灵活的数据结构，选择Redis
- 如果需要服务发现和健康检查，选择Consul

## 参考链接

- 官方文档：https://zookeeper.apache.org/doc/r3.9.2/index.html
- Zookeeper原理与实践：https://www.ibm.com/developerworks/cn/java/j-lo-zookeeper/
- Zookeeper权威指南：https://book.douban.com/subject/26841616/

## Bilibili

- Zookeeper教程：https://www.bilibili.com/video/BV1Vf4y127N5/
- 分布式协调服务Zookeeper详解：https://www.bilibili.com/video/BV1J4411d7D3/

