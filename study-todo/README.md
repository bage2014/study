# study-TODO #
代办事项：

Kayak

PacificA : kafka采用的一致性协议





压测



线上问题定位



Spring Cloud Alibaba 相关环境搭建

Docker 版本



API 方法 VS Logic 直接调用

Message Bus 抽取发送邮件方法



Auth Server 设置自定义User ID字段



yarn 配置淘宝 链接



vue 选型

目前采用： element plus

https://element-plus.gitee.io/zh-CN/guide/design.html



vue 使用route

https://cn.vuejs.org/v2/guide/routing.html

https://router.vuejs.org/zh/guide/#javascript

https://github.com/chrisvfritz/vue-2.0-simple-routing-example/



统一校验登陆状态；

后端，，，

文件上传-待自测

、消息服务 ，进行实现 -done 

spring boot 日子打到ELK - done



Html 访问问题 -done 引入themef依赖即可



搭建Spring Cloud 环境

注册中心、啥的 【docker版本】



注册中心：

http://c.biancheng.net/springcloud/nacos.html





MapStruct + Lombok 问题处理 -done 

mapping 日期自动mapping 







### docker 安装ELK

#### 设置国内镜像

参考链接： https://www.csdn.net/tags/NtzaMgzsNDA4Ni1ibG9n.html

**操作步骤**

Docker Desktop 应用图标 -> Perferences，在左侧导航菜单选择 Docker  Engine，在右侧像下边一样编辑 json 文件。修改完成之后，点击 Apply & Restart 按钮，Docker  就会重启并应用配置的镜像地址了。

```json
{
  "registry-mirrors": [
    "https://9cpn8tt6.mirror.aliyuncs.com",
    "https://hub-mirror.c.163.com",
    "https://registry.docker-cn.com"
  ]
}
```

验证

```
docker info
```

## 

### 安装ES

Docker Pull Command

```
docker pull elasticsearch:7.16.2

```

创建基础目录 

```
mkdir /Users/bage/bage/docker-data/es

bage % pwd
/Users/bage/bage/docker-data/es

bage % ls
config	data	nodes	plugins
```

编辑文件

```
vi /Users/bage/bage/docker-data/es/config/elasticearch.yml


```

启动 

```
docker run --network myapp --name bage-es -p 9092:9200 -p 8093:9300 \
-e "discovery.type=single-node" \
-v /Users/bage/bage/docker-data/es/config/elasticearch.yml:/usr/share/elasticsearch/config/elasticearch.yml \
-v /Users/bage/bage/docker-data/es/data:/usr/share/elasticsearch/data \
-v /Users/bage/bage/docker-data/es/plugins:/usr/share/elasticsearch/plugins \
-d elasticsearch:7.16.2

```

 访问

```
http://127.0.0.1:9092/_cat/health
http://127.0.0.1:9092

查看索引
http://localhost:9092/_cat/indices?v&pretty
```



### 安装kibana

Docker Pull Command

```
docker pull kibana:7.16.2

```

创建基础目录 

```
mkdir /Users/bage/bage/docker-data/kibana

bage % pwd
/Users/bage/bage/docker-data/kibana

bage % ls
config
```

编辑文件[暂时不使用]

```
/Users/bage/bage/docker-data/kibana/config/kibana.yml

server.port: 8056
server.host: "0.0.0.0"
elasticsearch.url: "http://bage-es:9092"

```

start a instance

```
docker run --network myapp -d --link bage-es:elasticsearch -p 9056:5601 --name bage-kibana \
-d kibana:7.16.2

```

visit

```
http://127.0.0.1:9056/app/kibana

http://127.0.0.1:9056/app/kibana#/dev_tools/console
```

**kibana里建立索引** 

通过kiban菜单去建立索引：Management>Index patterns>Create index pattern，这里会显示可用的索引名称。

可以直接搜索 index 找到 Create index pattern



选择对应的索引



Discover 里面创建 看板

### 安装 logstash

参考链接 https://www.elastic.co/guide/en/logstash/7.17/docker.html

https://github.com/elastic/logstash/blob/7.17/config/logstash.yml

Docker Pull Command

```
docker pull logstash:7.16.2
```

创建基础目录 

```
mkdir /Users/bage/bage/docker-data/logstash

bage % pwd
/Users/bage/bage/docker-data/logstash

bage % ls
pipeline	config

```

编辑文件

```
vi /Users/bage/bage/docker-data/logstash/config/logstash.yml

http.host: "0.0.0.0"
# xpack.monitoring.elasticsearch.hosts: ["http://bage-es:9092"]
monitoring.elasticsearch.hosts: ["http://bage-es:9092"]
# path.config: /usr/share/logstash/config/conf.d/*.conf
# path.logs: /usr/share/logstash/logs

```



```
vi /Users/bage/bage/docker-data/logstash/pipeline/logstash.conf

# Sample Logstash configuration for creating a simple
# Beats -> Logstash -> Elasticsearch pipeline.

input {
 tcp {
  mode => "server"
  port => 5044
  codec => json_lines
  stat_interval => "5"
 }
}

output {
  elasticsearch {
    hosts => ["http://bage-es:9200"]
    index => "{{indexName}}"
    #user => "elastic"
    #password => "changeme"
  }
}

```

start a instance

```
docker run --network myapp --name=bage-logstash \
-p 8056:5601 \
-p 8044:5044 \
-v /Users/bage/bage/docker-data/logstash/config/logstash.yml:/usr/share/logstash/config/logstash.yml \
-v /Users/bage/bage/docker-data/logstash/pipeline/:/usr/share/logstash/pipeline/ \
-d logstash:7.16.2


docker run --name bage-logstash --network myapp -p 8056:5601 --link bage-es:elasticsearch -d logstash:7.16.2
  
```

### 访问



## Flutter

Mac 搭建Flutter 环境

下载Flutter 

下载Android Studio



配置fluter 环境变量



安装 Dart 插件、Flutter 插件



ZipException 处理

Flutter run ZipException

https://www.jianshu.com/p/8e1007e65c66

```

  
```

### 