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

https://nacos.io/zh-cn/index.html

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





## Idea



https://www.baidu.com/s?wd=%E6%9C%89%E4%BB%80%E4%B9%88%E6%96%B0%E9%A2%96app%E6%83%B3%E6%B3%95&pn=20&oq=%E6%9C%89%E4%BB%80%E4%B9%88%E6%96%B0%E9%A2%96app%E6%83%B3%E6%B3%95&ie=utf-8&fenlei=256&rsv_idx=1&rsv_pq=c6ff598c000f331e&rsv_t=fea2kaljO0xoo8gvRpHo%2F67HDIO4yMP4OXIncyW%2BgDAgqJy0rgXbySsUv2M





1、、、、、、、、

最近网课又出现在我的生活里了。

  有个想法，我尽量用简单的语言描述一下。

  背景：我选择用ipad+iPhone pencil听网课，有做笔记的习惯

**产生的问题：**

  ①当我使用ipad听网课时我就无法做笔记，当我用ipad做笔记时就无法看网课。（同一设备）

  ②分屏功能也不错，但是问题在于分屏的话我的网课画面就会变小，和用手机看有什么区别呢？甚至还不如手机屏幕大。

  ③当然我也可以用电脑看网课，用pad或本子做笔记，但我更想拥有一个这样的软件，后期我再整理纸质笔记。

**关于应用的设想：**

  这个应用可以**漂浮**在各应用之上，有点悬浮窗的感觉，而这个笔记的纸它本身是一个**透明的**（或者是可以设置为透明的），可以让我在屏幕的各个地方、各个软件上随意做笔记，同时设置截图、录音、加减纸张甚至录制背景视频的**快捷键**。当我打开软件查看我的笔记本时，也可以查看我当时的笔记。

作者：风骨可无羡

链接：https://www.zhihu.com/question/299878221/answer/1731615384

来源：知乎

著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。



2、、、、、、、、

付费WiFi，到其他地方没流量了，付费给别人的WiFi让别人告诉你密码就可以了，怎么样。



3、、、、、、、、、

应用监控，，正在打开那个应用？？？？