# 05 新兴技术

> 云计算 + 大数据 + 微服务 + AI + 区块链
> 占比：约 10-15%，逐年增加

## 1. 云计算

### 1.1 三大服务模式
| 模式 | 说明 | 例子 |
|------|------|------|
| IaaS | 基础设施即服务 | AWS EC2、阿里云 ECS |
| PaaS | 平台即服务 | Heroku、App Engine |
| SaaS | 软件即服务 | Salesforce、Office 365 |

### 1.2 部署模型
- 公有云 / 私有云 / 混合云 / 社区云

### 1.3 关键技术
- 虚拟化：Hypervisor（KVM/Xen/VMware）
- 容器：Docker / LXC
- 容器编排：Kubernetes / Swarm / Mesos
- 服务网格：Istio / Linkerd
- Serverless：FaaS（AWS Lambda）

## 2. 大数据

### 2.1 4V 特征
- Volume（量大） / Velocity（高速） / Variety（多样） / Value（价值密度低）
- 第五 V：Veracity（真实性）

### 2.2 Hadoop 生态
| 组件 | 作用 |
|------|------|
| HDFS | 分布式文件存储 |
| MapReduce | 分布式计算 |
| YARN | 资源调度 |
| HBase | 列式 NoSQL |
| Hive | SQL on Hadoop |
| Pig | 脚本语言 |
| Sqoop | RDBMS ↔ Hadoop |
| Flume | 日志采集 |
| Kafka | 消息队列 |
| ZooKeeper | 协调服务 |
| Spark | 内存计算 |
| Flink | 流计算 |
| Storm | 实时计算 |

### 2.3 Lambda 架构 vs Kappa 架构
- Lambda：批层（HDFS/MR）+ 速度层（Spark Streaming）+ 服务层
- Kappa：用 Kafka 替代批层，全流式

### 2.4 NoSQL 四大类
| 类型 | 例子 | 场景 |
|------|------|------|
| KV | Redis / Memcached | 缓存、计数 |
| 文档 | MongoDB / CouchDB | 内容管理 |
| 列存 | HBase / Cassandra | 大数据宽表 |
| 图 | Neo4j | 社交、推荐 |

## 3. 微服务架构

### 3.1 与 SOA 区别
| 维度 | SOA | 微服务 |
|------|-----|--------|
| 通信 | ESB 总线 | 轻量（REST/MQ） |
| 数据 | 共享 | 每服务独立 |
| 部署 | 一起 | 独立 |
| 粒度 | 较粗 | 细 |
| 治理 | 中心化 | 去中心化 |

### 3.2 核心组件
- 服务注册发现：Eureka / Nacos / Consul / Zookeeper
- API 网关：Spring Cloud Gateway / Zuul / Kong
- 配置中心：Spring Cloud Config / Nacos / Apollo
- 熔断器：Hystrix / Sentinel / Resilience4j
- 链路追踪：SkyWalking / Zipkin / Jaeger
- 服务通信：Feign / gRPC / Dubbo

### 3.3 服务拆分原则
- 单一职责
- 限界上下文（DDD）
- 独立部署
- 独立数据存储
- 团队边界（康威定律）

## 4. 消息中间件对比

| 中间件 | 吞吐 | 延迟 | 可靠性 | 适用 |
|--------|------|------|--------|------|
| Kafka | 极高 | 低 | 高（持久化） | 日志、流式 |
| RabbitMQ | 中 | 低 | 高 | 业务消息 |
| RocketMQ | 高 | 低 | 极高（事务） | 电商事务 |
| ActiveMQ | 中 | 中 | 中 | 传统场景 |

## 5. 分布式核心理论

### 5.1 CAP
- 一致性 C / 可用性 A / 分区容错 P
- 三选二，分布式必然 P，故 CP 或 AP

### 5.2 BASE
- 基本可用 / 软状态 / 最终一致

### 5.3 一致性
- 强一致性 / 弱一致性 / 最终一致性
- 共识算法：Paxos / Raft / ZAB

### 5.4 分布式事务方案
- 2PC / 3PC
- TCC（Try-Confirm-Cancel）
- Saga
- 本地消息表
- 事务消息（RocketMQ）

### 5.5 分布式 ID
- UUID / 雪花 Snowflake / 数据库自增 / Redis / Leaf

## 6. 人工智能

### 6.1 机器学习分类
- 监督 / 无监督 / 半监督 / 强化学习

### 6.2 深度学习框架
- TensorFlow / PyTorch / Keras / Caffe

### 6.3 应用领域
- NLP / CV / 推荐系统 / 语音 / 大模型 LLM

## 7. 区块链

### 7.1 三大类型
- 公有链 / 联盟链 / 私有链

### 7.2 核心技术
- 哈希 / 数字签名 / 默克尔树 / 共识（PoW/PoS/PBFT）/ 智能合约

### 7.3 应用
- 数字货币 / 供应链溯源 / 版权 / 跨境支付

## 8. 物联网 / 边缘计算

### 8.1 物联网四层
- 感知层 / 网络层 / 平台层 / 应用层

### 8.2 边缘计算
- 数据就近处理，降低延迟
- 与云计算互补

## 错题归档

| 日期 | 题号 | 错因 | 状态 |
|------|------|------|------|
