# 环境准备指南

## 1. 环境准备

### 1.1 Docker 环境

#### 1.1.1 安装步骤

官方安装文档：[Docker Engine Install CentOS](https://docs.docker.com/engine/install/centos/)

推荐使用 Docker 仓库进行安装，这样便于后续的升级和维护。

**步骤 1：安装依赖包**

```bash
yum install -y yum-utils \
device-mapper-persistent-data \
lvm2
```

**步骤 2：配置 Docker 仓库**

```bash
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
```

### 1.2 MySQL 安装

官方镜像地址：[Docker Hub MySQL](https://hub.docker.com/_/mysql)

#### 1.2.1 下载镜像

```bash
# 标准版本
docker pull mysql

# Mac M1 芯片电脑
docker pull mysql/mysql-server
```

#### 1.2.2 启动 MySQL 实例

```bash
docker run --name bage-mysql \
--add-host=host.docker.internal:host-gateway \
-v ${HOME}/bage/docker-data/mysql:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=bage \
-p 3306:3306 \
-d mysql/mysql-server
```

**参数说明**：
- `--name bage-mysql`：容器名称
- `-v ${HOME}/data/mysql:/var/lib/mysql`：挂载数据目录
- `-e MYSQL_ROOT_PASSWORD=bage`：设置 root 用户密码
- `-p 3306:3306`：映射端口
- `-d`：后台运行

#### 1.2.3 配置 MySQL

**进入容器**：

```bash
docker exec -it bage-mysql /bin/bash
```

**登录 MySQL**：

```bash
mysql -u root -p
```

**创建数据库和用户**：

```sql
-- 创建数据库
CREATE DATABASE mydb;

-- 创建用户
CREATE USER 'bage'@'%' IDENTIFIED BY 'bage';

-- 授权
GRANT ALL PRIVILEGES ON mydb.* TO 'bage'@'%';
```

**解决远程登录问题**：

```sql
-- 修改密码规则
ALTER USER 'bage'@'%' IDENTIFIED BY 'bage' PASSWORD EXPIRE NEVER;

-- 更新密码加密方式
ALTER USER 'bage'@'%' IDENTIFIED WITH mysql_native_password BY 'bage';

-- 刷新权限
FLUSH PRIVILEGES;
```

### 1.3 JMeter 安装

官方网站：[Apache JMeter](https://jmeter.apache.org/)

#### 1.3.1 启动 JMeter

```bash
cd ${HOME}/software/apache-jmeter-5.5/bin
./jmeter.sh
```

#### 1.3.2 常用脚本

测试脚本存放目录：

```bash
${HOME}/bage/github/study/study-best-practice/jmx
```

### 1.4 Redis 安装

官方镜像地址：[Docker Hub Redis](https://hub.docker.com/_/redis/)

#### 1.4.1 下载镜像

```bash
docker pull redis
```

#### 1.4.2 启动 Redis 实例

```bash
docker run -p 6379:6379 \
--name bage-redis \
--add-host=host.docker.internal:host-gateway \
-d redis \
--requirepass "bage"
```

#### 1.4.3 登录 Redis

使用 redis-cli 登录：

```bash
redis-cli

auth bage
```

### 1.5 RabbitMQ 安装

官方镜像地址：[Docker Hub RabbitMQ](https://hub.docker.com/_/rabbitmq)

#### 1.5.1 下载镜像

```bash
docker pull rabbitmq
```

#### 1.5.2 启动 RabbitMQ 实例

```bash
docker run -d \
--hostname bage-rabbit \
--name bage-rabbit \
-p 15672:15672 \
-p 5672:5672 \
-e RABBITMQ_DEFAULT_USER=bage \
-e RABBITMQ_DEFAULT_PASS=bage \
rabbitmq:3-management
```

**说明**：启动过程中会自动下载 `rabbitmq:3-management` 镜像（包含管理界面）。

#### 1.5.3 访问验证

访问管理界面（默认用户名/密码：admin/admin）：

```
http://127.0.0.1:15672/
```

### 1.6 Prometheus 安装

官方镜像地址：[Docker Hub Prometheus](https://hub.docker.com/r/prom/prometheus)

官方文档：[Prometheus 概述](https://prometheus.io/docs/introduction/overview/)

#### 1.6.1 下载镜像

```bash
docker pull prom/prometheus
```

#### 1.6.2 启动 Prometheus 实例

**基本启动**：

```bash
docker run -d \
--name bage-prometheus \
-p 9090:9090 \
prom/prometheus
```

**高级启动（带数据持久化和配置文件）**：

```bash
docker run -d \
--name bage-prometheus \
--add-host=host.docker.internal:host-gateway \
-p 9090:9090 \
-v /Users/bage/bage/docker-data/prometheus:/prometheus/data \
-v /Users/bage/bage/docker-conf/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml \
prom/prometheus
```

#### 1.6.3 访问验证

```
http://localhost:9090/metrics
```

### 1.7 Grafana 安装

官方镜像地址：[Docker Hub Grafana](https://hub.docker.com/r/grafana/grafana)

#### 1.7.1 下载镜像

```bash
docker pull grafana/grafana
```

#### 1.7.2 启动 Grafana 实例

**基本启动**：

```bash
docker run -d \
--name=bage-grafana \
-p 3000:3000 \
grafana/grafana
```

**Mac 系统启动（带数据持久化）**：

```bash
docker run -d \
--name=bage-grafana \
--add-host=host.docker.internal:host-gateway \
-p 3000:3000 \
-v /Users/bage/bage/docker-data/grafana:/var/lib/grafana \
grafana/grafana
```

#### 1.7.3 访问验证

访问 Grafana 界面（默认用户名/密码：admin/admin）：

```
http://localhost:3000/
```