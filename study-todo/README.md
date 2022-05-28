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

、消息服务 ，进行实现



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

启动 

```
docker run --network myapp --name elasticsearch -p 9092:9200 -p 8093:9300 -e "discovery.type=single-node" elasticsearch:7.16.2
```

 访问

```
http://127.0.0.1:9092/_cat/health
http://127.0.0.1:8093/_cat/health
```

### 安装kibana

Docker Pull Command

```
docker pull kibana:7.16.2

```

start a instance

```
docker run --network myapp -it -d -e ELASTICSEARCH_URL=http://elasticsearch:9092/ --name kibana -p 9056:5601 kibana:7.16.2
```

自定义配置文件

```
-v /usr/local/es/es.yml:/usr/share/elasticsearch/config/elasticsearch.yml
```

visit

```
http://192.168.146.133:5601/app/kibana
```

### 安装 logstash

Docker Pull Command

```
docker pull logstash:7.16.2
```

start a instance[not enough space]

```
docker run --name logstash --rm -it -v /home/bage/data/pipeline/:/usr/share/logstash/pipeline/ logstash:7.16.2
```

### 



