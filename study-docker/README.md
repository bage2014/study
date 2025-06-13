# study-docker #
docker 学习笔记

## 环境搭建 ##

### 参考链接 ###
- Docker 官网 [https://docs.docker.com/](https://docs.docker.com/ "Docker 官网")
- CentOS安装 [https://docs.docker.com/install/linux/docker-ce/centos/#install-using-the-convenience-scripthttps://docs.docker.com/install/linux/docker-ce/centos/#install-using-the-convenience-script "CentOS安装")

### 安装 ###

https://docs.docker.com/engine/install/centos/

Most users set up Docker’s repositories and install from them, for ease of installation and upgrade tasks. This is the recommended approach.

安装依赖包

      yum install -y yum-utils \
      device-mapper-persistent-data \
      lvm2

配置仓库

      yum-config-manager \
      --add-repo \
      https://download.docker.com/linux/centos/docker-ce.repo    

安装Docker 
> yum install docker-ce

启动
> systemctl start docker

运行hello world
> docker run hello-world

非root管理docker
> \# usermod -aG docker $USER

非root运行hello-world
> $ docker run hello-world

配置开机启动
> systemctl enable docker 



docker hub 设置国内镜像地址

https://www.csdn.net/tags/NtzaMgzsNDA4Ni1ibG9n.html

在任务栏点击 Docker Desktop 应用图标 -> Perferences，在左侧导航菜单选择 Docker  Engine，在右侧像下边一样编辑 json 文件。修改完成之后，点击 Apply & Restart 按钮，Docker  就会重启并应用配置的镜像地址了。

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



添加当前用户

https://docs.docker.com/engine/install/linux-postinstall/

To create the `docker` group and add your user:

1. Create the `docker` group.

   ```console
   $ sudo groupadd docker
   ```

2. Add your user to the `docker` group.

   ```console
   $ sudo usermod -aG docker $USER
   
   usermod -aG docker bage
   ```

3. Log out and log back in so that your group membership is re-evaluated.

   > If you're running Linux in a virtual machine, it may be necessary to restart the virtual machine for changes to take effect.

   You can also run the following command to activate the changes to groups:

   ```console
   $ newgrp docker
   ```

Verify that you can run `docker` commands without `sudo`.

```console
$ docker run hello-world
```

This command downloads a test image and runs it in a container. When the container runs, it prints a message and exits.



## 常用命令 ##

### Docker ###
    docker –version
    docker info
    docker run hello-world
    docker image ls
    docker container ls --all
### List Docker CLI commands ###
    docker
    docker container --help

### Display Docker version and info ###
    docker --version
    docker version
    docker info

### Execute Docker image ###
    docker run hello-world

### List Docker images ###
    docker image ls

### List Docker containers (running, all, all in quiet mode) ###
    docker container ls
    docker container ls --all
    docker container ls –aq

### 查看docker 信息 ###
    docker inspect ${containerId}


### 进入容器 ###
    docker exec -it ${containerID} /bin/bash

### 删除image ###
    docker rmi ${iamge:v}

### 宿主机拷贝到容器 ###
    docker cp sourcePath ${containerId}:destinationPath

### 容器拷贝到宿主机 ###
    docker cp ${containerId}:destinationPath sourcePath 

### 保存一个congtainer到 image ###
    docker commit ${containerId} image:v 

docker  23c18d958279 bigdata:v0.2

### 保存一个image ###
    docker save -o ./workspace/docker/tomcat0.1.tar tomcat:0.1 
    docker save -o destinationPath imageName

### 加载一个文件到image ###
    docker load -i sourcePath

## docker hub & 常用环境 ##
官方镜像 [https://hub.docker.com/explore/](https://hub.docker.com/explore/ "官方镜像")

### 安装配置Tomcat ###	
参考链接：[https://hub.docker.com/_/tomcat/](https://hub.docker.com/_/tomcat/ "安装配置Tomcat")

Docker Pull Command

    $ docker pull tomcat
启动

    $ docker run -it --rm -p 8888:8080 tomcat
启动带挂载目录

    $ docker run -v ${HOME}/mnt-tomcat:/mnt -it --rm -p 8888:8080 tomcat


​    
### 安装配置CentOS [未验证] ###
参考链接：[https://hub.docker.com/_/centos/](https://hub.docker.com/_/centos/ "安装配置CentOS")

Docker Pull Command

    $ docker pull centos
运行

    docker run centos
查看ID

    docker container ls

### 安装配置MySQL ###
[https://hub.docker.com/_/mysql](https://hub.docker.com/_/mysql)
	
下载

    docker pull mysql

Mac M1 报错

```
docker pull mysql

Using default tag: latest

latest: Pulling from library/mysql

no matching manifest for linux/arm64/v8 in the manifest list entries

```

解决方案

```
docker pull mysql/mysql-server
```



Start a mysql server instance

    docker run --name bage-mysql -v ${HOME}/data/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=bage -p 3306:3306 -d bage-mysql
    
    Mac-pro:	
    docker run --name bage-mysql -v ${HOME}/bage/docker-data/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=bage -p 3306:3306 -d mysql/mysql-server
    
    docker run --name bage-mysql --add-host=host.docker.internal:host-gateway -v ${HOME}/bage/docker-data/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=bage -p 3306:3306 -d mysql/mysql-server



其中

	--name 起名 bage-mysql 
	-v 挂载目录 ${HOME}/data/mysql:/var/lib/mysql 
	-e 设置root密码 MYSQL_ROOT_PASSWORD=bage 
	-p 映射端口 3306:3306 
	-d bage-mysql

进入mysql容器

    docker exec -it bage-mysql /bin/bash
在容器内部进行登录

    mysql -u root -p
创建数据库

    Create database mydb;
创建用户

    CREATE USER 'bage'@'%' IDENTIFIED BY 'bage';
授权

    grant all privileges on mydb.* to 'bage'@'%';

远程无法登陆问题
[https://blog.csdn.net/gf0515/article/details/80466213](https://blog.csdn.net/gf0515/article/details/80466213)

修改密码规则

    ALTER user 'bage'@'%' IDENTIFIED BY 'bage' PASSWORD EXPIRE NEVER;

更新密码

    ALTER user 'bage'@'%' IDENTIFIED WITH mysql_native_password BY 'bage';
刷新

    FLUSH PRIVILEGES;

### 安装配置signoz

https://signoz.io/docs/install/docker/



### 安装配置Postgres

参考链接：[https://hub.docker.com/_/postgres/](https://hub.docker.com/_/postgres/ "安装配置Postgres")

Docker Pull Command

    docker pull postgres
start a postgres instance 方式1

    docker run -it -p 5432:5432 --name bage-postgres  -e POSTGRES_PASSWORD=postgres -d postgres
start a postgres instance 方式2

    docker run --name bage-postgres -e POSTGRES_PASSWORD=postgres -d postgres
connect to it from an application

    docker run -it --rm --link bage-postgres:postgres postgres psql -h postgres -U postgres
创建数据库：

    CREATE TABLE weather (
       cityvarchar(80),
       temp_lo int,   -- low temperature
       temp_hi int,   -- high temperature
       prcpreal,  -- precipitation
       datedate
    );

### 安装配置Nginx ###

https://hub.docker.com/_/nginx

下载安装：

    docker pull nginx
启动：

    docker run -p 80:80 --name tmp-nginx-container -d nginx
    
    Mac
    docker run -p 80:80 -v ${HOME}/bage/docker-conf/nginx/nginx.conf:/etc/nginx/nginx.conf --name bage-nginx -d nginx

docker访问宿主机：

```
host.docker.internal
```

进入容器：

    docker exec -it tmp-nginx-container /bin/bash
默认配置文件位置：

    /etc/nginx/nginx.conf 
和

    /etc/nginx/conf.d/default.conf

新建一个临时编辑目录：

    mkdir -p ${HOME}/workspace/docker/docker/
拷贝配置文件出来进行编辑:

    docker cp tmp-nginx-container:/etc/nginx/conf.d/default.conf ${HOME}/workspace/docker/docker/
编辑修改后进行返回：

    docker cp ${HOME}/workspace/docker/docker/ tmp-nginx-container:/etc/nginx/nginx.conf

基本应用

- 参考链接
  - Nginx的一些基本功能 [https://www.cnblogs.com/jimmy-muyuan/p/5424329.html](https://www.cnblogs.com/jimmy-muyuan/p/5424329.html "Nginx的一些基本功能")
  - Nginx详解-服务器集群 [https://www.cnblogs.com/jiekzou/p/4486447.html](https://www.cnblogs.com/jiekzou/p/4486447.html "Nginx详解-服务器集群")
#### 实例 ####

##### 静态HTTP服务器(配置和代码)  #####
    study-linux/src/main/resources/nginx-http-static

##### 代理服务器(配置和代码)  #####
    study-linux/src/main/resources/nginx-proxy

##### 负载均衡(简单轮询)  #####
    study-linux/src/main/resources/nginx-upstream


### 安装配置Nexus[非官方] ###
下载

    docker pull sonatype/nexus3
To run, binding the exposed port 8081 to the host.

    $ docker run -d -p 8081:8081 --name nexus sonatype/nexus3
To test:

    $ curl -u admin:admin123 http://localhost:8081/service/metrics/ping

如不能访问，先在web进行访问http://localhost:8081进行登录，后再次验证
### 安装配置Redis ###
参考链接：[https://hub.docker.com/_/redis/](https://hub.docker.com/_/redis/ "安装配置Redis")
Docker Pull Command

    docker pull redis
start a redis instance

    $ docker run -p 6379:6379 --name bage-redis -d redis --requirepass "bage"
connect to it from an application

    $ docker run --name some-app --link bage-redis:redis -d application-that-uses-redis
... or via redis-cli

    $ docker run -it --link bage-redis:redis --rm redis redis-cli -h redis -p 6379

设置密码(启动时候)

    docker run -p 6379:6379 --name bage-redis -d redis --requirepass "bage"
    
    Mac 
    docker run -p 6379:6379 --name bage-redis  --add-host=host.docker.internal:host-gateway  -d redis --requirepass "bage"


​    

 自定义配置文件启动

	docker run -p 8379:6379 -v ${HOME}/conf/redis.conf:/usr/local/etc/redis/redis.conf --name redis -d redis redis-server /usr/local/etc/redis/redis.conf  --requirepass "bage.redis"



登陆

... or via redis-cli

```
$ redis-cli


$ auth XXXX
```

https://redis.io/docs/ui/cli/

```
redis-cli -a mypassword PING
```



### 安装配置RabbitMQ ###

参考链接：[https://hub.docker.com/_/rabbitmq](https://hub.docker.com/_/rabbitmq "安装配置RabbitMQ")

Docker Pull Command

    docker pull rabbitmq

启动

	docker run -d --hostname bage-rabbit --name bage-rabbit -p 15672:15672 -p 5672:5672 -e RABBITMQ_DEFAULT_USER=bage -e RABBITMQ_DEFAULT_PASS=bage rabbitmq:3-management
启动过程中会进行下载 rabbitmq:3-management

    Unable to find image 'rabbitmq:3-management' locally 
    3-management: Pulling from library/rabbitmq
    f476d66f5408: Already exists 
    8882c27f669e: Already exists 
    d9af21273955: Already exists 
    f5029279ec12: Already exists 
    ecb5cfa3e5cd: Already exists 
    b23d77357b59: Already exists 
    9cc380c0670a: Already exists 
    00edb647b0d0: Already exists 
    7fea86aa9152: Already exists 
    12f866ac27b1: Already exists 
    6d288824e924: Pull complete 
    0a1f754a8a89: Pull complete 

访问验证(bage/bage)

Visit

```
[http://{ip}:15672/]

http://127.0.0.1:15672/

```


### 安装配置shipyard ###
参考链接：[https://hub.docker.com/r/shipyard/shipyard](https://hub.docker.com/r/shipyard/shipyard "安装配置shipyard")

Docker Pull Command

    docker pull shipyard/shipyard


### 安装部署zookeeper  ###

参考链接：[https://hub.docker.com/_/zookeeper](https://hub.docker.com/_/zookeeper)

Docker Pull Command

    docker pull zookeeper

启动

    docker run --name bage-zookeeper --restart always -p 2181:2181 -d zookeeper
    
    Mac
    docker run --network bage-net --name bage-zookeeper -v ${HOME}/bage/docker-data/zookeeper:/data -p 2181:2181 -d zookeeper
    
    -e JVMFLAGS="-Xmx1024m"
    docker run --network bage-net --name bage-zookeeper -v ${HOME}/bage/docker-data/zookeeper:/data -p 2181:2181 -d zookeeper



Connect to Zookeeper from the Zookeeper command line client

```console
docker run -it --rm --link bage-zookeeper:zookeeper zookeeper zkCli.sh -server zookeeper
```

### Connecting to ZooKeeper

```
$ bin/zkCli.sh -server 127.0.0.1:2181
```



### 安装部署portainer  ###

参考链接: [https://hub.docker.com/r/portainer/portainer]()、[https://portainer.readthedocs.io/en/latest/deployment.html](https://portainer.readthedocs.io/en/latest/deployment.html)

Docker Pull Command

    docker pull portainer/portainer

启动(${HOME}/data/portainer 持久化数据目录)

    docker run -d -p 9000:9000 --name portainer --restart always -v /var/run/docker.sock:/var/run/docker.sock -v ${HOME}/data/portainer:/data portainer/portainer


### 安装部署 jenkins  ###
参考链接 [https://hub.docker.com/_/jenkins](https://hub.docker.com/_/jenkins)

Docker Pull Command

    docker pull jenkins

启动 

    docker run -p 8080:8080 -p 50000:50000 -v /your/home:/var/jenkins_home jenkins

访问

    http://{ip}:8080  

### 安装部署 elasticsearch  ###
参考链接 [https://www.elastic.co/guide/en/elasticsearch/reference/6.7/docker.html](https://www.elastic.co/guide/en/elasticsearch/reference/6.7/docker.html)、[https://hub.docker.com/_/elasticsearch](https://hub.docker.com/_/elasticsearch)
版本匹配 [https://www.elastic.co/cn/support/matrix#matrix_compatibility](https://www.elastic.co/cn/support/matrix#matrix_compatibility) 


Docker Pull Command

    docker pull elasticsearch:7.5.1
    
    Mac M1
    docker pull elasticsearch:7.16.2


​    

启动 

    docker run --network myapp --name elasticsearch -p 9092:9200 -p 8093:9300 -e "discovery.type=single-node" elasticsearch:7.5.1
    
    Mac
    docker run --network myapp --name elasticsearch -p 9092:9200 -p 8093:9300 -e "discovery.type=single-node" elasticsearch:7.11.1

 
















基本访问

```
http://localhost:9092/


curl -X GET "localhost:9092/persons/_doc/1711947442539?pretty"
```
























访问

    http://{ip}:9092/_cat/health  
    
    http://127.0.0.1:9092/_cat/health
    http://127.0.0.1:8093/_cat/health


### 安装部署 zipkin  ###
参考链接 [https://hub.docker.com/r/openzipkin/zipkin](https://hub.docker.com/r/openzipkin/zipkin)

Docker Pull Command

    docker pull openzipkin/zipkin

启动 

    docker run --name bage-zipkin --network myapp -d -p 9411:9411 openzipkin/zipkin



### 安装部署 FastDFS  ###
参考链接 [https://hub.docker.com/r/season/fastdfs](https://hub.docker.com/r/season/fastdfs)

Docker Pull Command

    docker pull season/fastdfs

启动 

    docker run -ti --name storage -v ~/storage_data:/fastdfs/storage/data -v ~/store_path:/fastdfs/store_path --net=host -e TRACKER_SERVER:192.168.1.2:22122 season/fastdfs storage


### 安装部署 Vue  ###
打包 vue 项目

    npm run build

创建 config 配置文件
在项目根目录下创建nginx文件夹，该文件夹下新建文件default.conf

	server {
	    listen       80;
	    server_name  localhost;
	
	    access_log  /var/log/nginx/host.access.log  main;
	    error_log  /var/log/nginx/error.log  error;
	
	    location / {
	        root   /usr/share/nginx/html;
	        index  index.html index.htm;
	        try_files $uri $uri/ /index.html;
	    }
	
	    #error_page  404              /404.html;
	
	    # redirect server error pages to the static page /50x.html
	    #
	    error_page   500 502 503 504  /50x.html;
	    location = /50x.html {
	        root   /usr/share/nginx/html;
	    }
	}

Dockerfile 文件


	FROM nginx
	
	MAINTAINER lrh
	
	COPY dist/  /usr/share/nginx/html/
	
	COPY nginx/default.conf /etc/nginx/conf.d/default.conf

构建vue镜像

	docker build -t my-app-ui .

启动

	docker run --name my-app-ui -p 8080:80 my-app-ui


启动

	docker run -p 8080:8080 my-app-ui .



### 安装部署 Ceph[待验证]  ###
参考链接 [https://hub.docker.com/r/ceph/daemon](https://hub.docker.com/r/ceph/daemon)、[http://docs.ceph.org.cn/](http://docs.ceph.org.cn/)、[http://docs.ceph.org.cn/radosgw/s3/java/](http://docs.ceph.org.cn/radosgw/s3/java/)、

[https://blog.csdn.net/don_chiang709/article/details/91511828](https://blog.csdn.net/don_chiang709/article/details/91511828)


查看CIDR 和 IP 和 设备

	ifconfig ==> got ip == 192.168.96.132
	ip addr ==> got CEPH_PUBLIC_NETWORK == 192.168.96.132/24
	lsblk ==> got OSD_DEVICE == /dev/sda

Docker Pull Command

	docker pull ceph/daemon

创建目录
	
	mkdir -p ${HOME}/data/ceph/etc
	mkdir -p ${HOME}/data/ceph/lib

If SELinux is enabled, run the following commands:

	sudo chcon -Rt svirt_sandbox_file_t ${HOME}/data/ceph/etc
	sudo chcon -Rt svirt_sandbox_file_t ${HOME}/data/ceph/lib

Deploy a monitor

	docker run -d --net=host --name=ceph-mon \
	-v ${HOME}/data/ceph/etc:/etc/ceph \
	-v ${HOME}/data/ceph/lib:/var/lib/ceph/ \
	-e MON_IP=192.168.96.132 \
	-e CEPH_PUBLIC_NETWORK=192.168.96.132/24 \
	ceph/daemon mon

查看状态

	docker exec ceph-mon ceph -s

Deploy a Manager daemon

	docker run -d --net=host --name=ceph-mgr \
	-v ${HOME}/data/ceph/etc:/etc/ceph \
	-v ${HOME}/data/ceph/lib:/var/lib/ceph/ \
	ceph/daemon mgr

查看状态

	docker exec ceph-mon ceph -s


exported keyring for client.bootstrap-osd

	docker exec ceph-mon \
	ceph auth get client.bootstrap-osd \
	-o /var/lib/ceph/bootstrap-osd/ceph.keyring


chmod ceph.client.admin.keyring

	chmod +r ${HOME}/data/ceph/etc/ceph.client.admin.keyring



Deploy an OSD

	docker run -d --net=host --name=ceph-osd \
	--pid=host \
	--privileged=true \
	-v ${HOME}/data/ceph/etc:/etc/ceph \
	-v ${HOME}/data/ceph/lib:/var/lib/ceph/ \
	-v /dev:/dev/ \
	-e OSD_DEVICE=/dev/sda \
	ceph/daemon osd_directory

查看状态

	docker exec ceph-mon ceph -s

osd_ceph_disk 启动 报错[待处理]


	docker run -d --net=host --name=ceph-osd \
	--pid=host \
	--privileged=true \
	-v ${HOME}/data/ceph/etc:/etc/ceph \
	-v ${HOME}/data/ceph/lib:/var/lib/ceph/ \
	-v /dev:/dev/ \
	-e OSD_DEVICE=/dev/sda \
	ceph/daemon osd_ceph_disk
	
	docker: Error response from daemon: OCI runtime create failed: container_linux.go:345: starting container process caused "setup user: no such file or directory": unknown.


Deploy a MDS

	docker run -d --net=host --name=ceph-mds \
	-v ${HOME}/data/ceph/etc:/etc/ceph \
	-v ${HOME}/data/ceph/lib:/var/lib/ceph/ \
	-e CEPHFS_CREATE=1 \
	ceph/daemon mds

查看状态

	docker exec ceph-mon ceph -s


​	
exported keyring for client.bootstrap-rgw

	docker exec ceph-mon \
	ceph auth get client.bootstrap-rgw \
	-o /var/lib/ceph/bootstrap-rgw/ceph.keyring

修改存储池副本size
	ceph osd pool set data size 1

Deploy a Rados Gateway 

	docker run -d --net=host --name=ceph-rgw \
	-v ${HOME}/data/ceph/etc:/etc/ceph \
	-v ${HOME}/data/ceph/lib:/var/lib/ceph/ \
	ceph/daemon rgw


查看状态

	docker exec ceph-mon ceph -s


Administration via radosgw-admin ??

	docker run -d --name ceph-rgw \
	-v ${HOME}/data/ceph/etc:/etc/ceph \
	-v /ho${HOME}ta/ceph/lib:/var/lib/ceph \
	-e CEPH_DAEMON=RGW -e RGW_NAME=myrgw -p 9000:9000 \
	-e RGW_REMOTE_CGI=1 -e RGW_REMOTE_CGI_HOST=192.168.96.132 \
	-e RGW_REMOTE_CGI_PORT=9000 ceph/daemon


​	
Deploy a REST API [起不来]

	docker run -d --net=host --name ceph-restapi \
	-e KV_TYPE=etcd \
	-e KV_IP=192.168.96.132 \
	ceph/daemon restapi

Deploy a RBD mirror

	docker run -d --net=host --name ceph-rbd_mirror \
	-e KV_IP=192.168.96.132 \
	ceph/daemon rbd_mirror


api java 

[https://docs.ceph.com/docs/master/radosgw/s3/java/](https://docs.ceph.com/docs/master/radosgw/s3/java/)


https://docs.ceph.com/docs/infernalis/man/8/radosgw-admin/

radosgw-admin user create --display-name="johnny rotten" --uid=johnny \
{ "user_id": "johnny", \
  "rados_uid": 0, \
  "display_name": "johnny rotten", \
  "email": "", \
  "suspended": 0, \
  "subusers": [], \
  "keys": [ \
        { "user": "johnny", \
          "access_key": "TCICW53D9BQ2VGC46I44", \
          "secret_key": "tfm9aHMI8X76L3UdgE+ZQaJag1vJQmE6HDb5Lbrz"}], \
  "swift_keys": []} \


### 安装配置xxl-job ###
参考链接：https://hub.docker.com/r/xuxueli/xxl-job-admin/

Docker Pull Command

	docker pull xuxueli/xxl-job-admin:2.4.1

start a instance

	docker run -e PARAMS="--spring.datasource.url=jdbc:mysql://localhost:3306/xxl_job?Unicode=true&characterEncoding=UTF-8 --spring.datasource.username=xxljob --spring.datasource.password=xxljob" -p 8808:8080 --name xxl-job-admin  -d xuxueli/xxl-job-admin:2.4.1


访问
	
	http://localhost:8808/xxl-job-admin

 















































### 安装配置 logstash ###
版本匹配 https://www.elastic.co/cn/support/matrix#matrix_compatibility 
参考链接：[https://www.elastic.co/guide/en/logstash/current/docker.html](https://www.elastic.co/guide/en/logstash/current/docker.html)、[https://hub.docker.com/_/logstash?tab=description](https://hub.docker.com/_/logstash?tab=description)、[https://www.elastic.co/guide/en/logstash/current/docker-config.html](https://www.elastic.co/guide/en/logstash/current/docker-config.html)
            https://www.elastic.co/guide/en/logstash/current/docker.html
Docker Pull Command

	docker pull logstash:7.5.1
	
	Mac M1
	docker pull logstash:7.16.2

start a instance[not enough space]

	docker run --name logstash --rm -it -v ${HOME}/data/pipeline/:/usr/share/logstash/pipeline/ logstash:7.5.1

### 安装配置 elk ###

参考链接[https://hub.docker.com/r/sebp/elk](https://hub.docker.com/r/sebp/elk)、[https://elk-docker.readthedocs.io/](https://elk-docker.readthedocs.io/)、[https://www.elastic.co/guide/en/elasticsearch/reference/5.0/vm-max-map-count.html#vm-max-map-count](https://www.elastic.co/guide/en/elasticsearch/reference/5.0/vm-max-map-count.html#vm-max-map-count)
ELK 环境搭建 [https://blog.51cto.com/daisywei/2126523](https://blog.51cto.com/daisywei/2126523)
[https://segmentfault.com/a/1190000020653301](https://segmentfault.com/a/1190000020653301)
Docker Pull Command

	docker pull sebp/elk:700


setting max_map_count

	sudo  sysctl -w vm.max_map_count=262144

vi ${HOME}/data/logstash/file-beats.conf

[Mac:   ${HOME}/bage/docker-data/elk/logstash/beats-input.conf]

```
# 数据输入配置：port -> 端口号；codec -> 输入格式。这里以logback为例。
input {
  tcp {
    port => 5044
    codec=>json_lines
  }
}

# 数据输出配置：hosts -> 主机集合；index -> 你将要创建的索引名称。这里es为例。
output {
  elasticsearch {
    hosts => ["127.0.0.1:9200"]
    index => "%{[appName]}-%{+YYYY.MM.dd}"
  }
}
```




start a instance

	docker run -v ${HOME}/data/logstash/file-beats.conf:/etc/logstash/conf.d/02-beats-input.conf -p 8056:5601 -p 8092:9200 -p 8044:5044 -it --name elk sebp/elk:700
	
	Mac: 
	docker run -v /Users/bage/bage/docker-data/elk/logstash/beats-input.conf:/etc/logstash/conf.d/02-beats-input.conf -p 8056:5601 -p 8092:9200 -p 8044:5044 -it --name elk sebp/elk:700
	
	Mac[多目录挂载问题]: 
	docker run -e MAX_MAP_COUNT="262144" -v /Users/bage/bage/docker-data/elk/es:/var/lib/elasticsearch -v /Users/bage/bage/docker-data/elk/logstash/beats-input.conf:/etc/logstash/conf.d/02-beats-input.conf -p 8056:5601 -p 8092:9200 -p 8044:5044 -it --name elk sebp/elk:700

访问
	
ES 

	http://192.168.146.133:8092/
	http://192.168.146.133:8092/_search?pretty

kibana 

	http://192.168.146.133:8056

项目实践链接 [https://github.com/bage2014/study-micro-services/tree/master/study-micro-services-sleuth](https://github.com/bage2014/study-micro-services/tree/master/study-micro-services-sleuth)

### 安装配置 kibana ###
参考链接：[https://hub.docker.com/_/kibana](https://hub.docker.com/_/kibana)
版本匹配 https://www.elastic.co/cn/support/matrix#matrix_compatibility 

Docker Pull Command

	docker pull kibana:7.5.1
	
	Mac
	docker pull kibana:7.16.2


start a instance

	docker run --network myapp -it -d -e ELASTICSEARCH_URL=http://elasticsearch:9092/ --name kibana -p 9056:5601 kibana:7.5.1

自定义配置文件

    -v /usr/local/es/es.yml:/usr/share/elasticsearch/config/elasticsearch.yml

visit

	http://192.168.146.133:5601/app/kibana

Kibana server is not ready yet 处理
部署ES；
docker run -p 8092:9200 -p 8093:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:6.7.2


### 安装配置 mongo ###
参考链接: https://hub.docker.com/_/mongo

【成功案例】https://www.linode.com/docs/guides/set-up-mongodb-on-docker/



Docker Pull Command

	docker pull mongo


mkdir

    mkdir -p /home/bage/data/mongodb
    mkdir -p /home/bage/conf/mongodb
    
    Mac
    mkdir -p /Users/bage/bage/docker-data/mongodb

Conf file

```
vi /home/bage/conf/mongodb/mongodb.conf

Mac
vi /Users/bage/bage/docker-conf/mongodb/mongodb.conf
```

content 	

	# mongod.conf
	
	# for documentation of all options, see:
	#   http://docs.mongodb.org/manual/reference/configuration-options/
	
	# Where and how to store data.
	storage:
	  dbPath: /var/lib/mongodb
	  journal:
	    enabled: true
	#  engine:
	#  mmapv1:
	#  wiredTiger:
	
	# where to write logging data.
	systemLog:
	  destination: file
	  logAppend: true
	  path: /var/log/mongodb/mongod.log
	
	# network interfaces
	net:
	  port: 27017
	  bindIp: 0.0.0.0
	
	# how the process runs
	processManagement:
	  timeZoneInfo: /usr/share/zoneinfo
	
	#security:
	
	#operationProfiling:
	
	#replication:
	
	#sharding:
	
	## Enterprise-Only Options:
	
	#auditLog:
	
	#snmp:


start a instance
		
	docker run --name mongo -p 27017:27017 -v /home/bage/data/mongodb:/data/db -d mongo --auth
		
	docker run --network bage-net --name mongo -p 27017:27017 -v /home/bage/data/mongodb:/etc/mongo -d mongo --config /home/bage/conf/mongodb/mongodb.conf --auth
	
	Mac
	docker run --network bage-net --name bage-mongo -p 27017:27017 -v /Users/bage/bage/docker-data/mongodb:/data/db -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=bage -d mongo
	
	指定配置文件？
	docker run --network bage-net --name bage-mongo -p 27017:27017 -v /home/bage/data/mongodb:/data/db -d mongo --config "/Users/bage/bage/docker-conf/mongodb/mongodb.conf" --auth


​	


​	


visit	
	
	docker exec -it mongo mongo admin
	
	docker exec -it bage-mongo mongo admin


​	



Auth

```
use admin

db.auth('admin', 'bage')

容器内部：

mongo admin -u admin -p bage

mongo 127.0.0.1:27017 -u "admin" -p "bage" --authenticationDatabase "admin"

```



create user 

```
db.createUser({ user:'bage',pwd:'bage',roles:[ { role:'userAdminAnyDatabase', db: 'admin'}]});
```

   	

	db.auth('admin', 'bage.mongodb')
	
	db.createUser({ user:'bage',pwd:'lulu1234',roles:[ { role:'userAdminAnyDatabase', db: 'admin'}]});
	
	db.createUser({ user: 'test', pwd: 'bagetest', roles: [{ role: "readWrite", db: "bagedb" }] });
	
	Mac: 
	db.createUser({ user:'bage',pwd:'bage',roles:[ { role:'userAdminAnyDatabase', db: 'admin'}]});
	db.auth('bage', 'bage')


​	
​	db.createUser({ user:'bage',pwd:'bagedb',roles:[ { role:'userAdminAnyDatabase', db: 'bagedb'}]});
​	db.auth('bage', 'bagedb')


​	



**CRUD**

https://www.mongodb.com/docs/manual/crud/

create user 

```

db.inventory.insertOne(
   { item: "canvas", qty: 100, tags: ["cotton"], size: { h: 28, w: 35.5, uom: "cm" } }
);

db.inventory.find( { item: "canvas" } );

```

   



#### 社区版本

参考链接：

https://www.mongodb.com/docs/manual/tutorial/install-mongodb-community-with-docker/#std-label-docker-mongodb-community-install

download 

```
docker pull mongodb/mongodb-community-server

```






### 安装配置 prometheus ###

参考链接：https://hub.docker.com/r/prom/prometheus

https://prometheus.io/docs/introduction/overview/

https://prometheus.io/download/

Docker Pull Command

	docker pull prom/prometheus

start a instance

	docker run --name prometheus -p 9090:9090 prom/prometheus
	
	docker run -d --name bage-prometheus -p 9090:9090 prom/prometheus

start a instance

	docker run --name prometheus -p 9090:9090 -v /home/bage/data/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus
	
	Mac 
	docker run -d --name bage-prometheus  --add-host=host.docker.internal:host-gateway  -p 9090:9090 -v /Users/bage/bage/docker-data/prometheus:/prometheus/data -v /Users/bage/bage/docker-conf/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus


​	


visit

	http://192.168.146.139:9090/metrics



### 安装配置 grafana ###

参考链接：https://hub.docker.com/r/grafana/grafana

Docker Pull Command

	docker pull grafana/grafana
	
	docker pull grafana/grafana

start a instance

	docker run -d --name=grafana -p 3000:3000 grafana/grafana
	
	Mac
	docker run -d --name=bage-grafana  --add-host=host.docker.internal:host-gateway -p 3000:3000 -v /Users/bage/bage/docker-data/grafana:/var/lib/grafana grafana/grafana


visit

	http://localhost:3000/

Try it out, default admin user is admin/admin.



#### 增加插件redis 看板

```
http://localhost:3000/admin/plugins/redis-datasource?page=overview
```



### 安装配置 gluster[待验证] ###

参考链接：https://github.com/gluster/gluster-containers

Docker Pull Command

	docker pull gluster/gluster-centos


prepare 

	mkdir -p /home/bage/gluster/etc/glusterfs 
	mkdir -p /home/bage/gluster/lib/glusterd 
	mkdir -p /home/bage/gluster/log/glusterfs
	mkdir -p /home/bage/gluster/fs/cgroup
	mkdir -p /home/bage/gluster/lib/glusterd
	mkdir -p /home/bage/gluster/dev

start a instance

	docker run -v /home/bage/gluster/etc/glusterfs:/etc/glusterfs:z -v /home/bage/gluster/lib/glusterd:/var/lib/glusterd:z -v /home/bage/gluster/log/glusterfs:/var/log/glusterfs:z -v /home/bage/gluster/fs/cgroup:/sys/fs/cgroup:ro -d --privileged=true --net=host -v /home/bage/gluster/dev/:/dev gluster/gluster-centos


Docker Pull Command

```
docker pull gluster/gluster-s3
```

prepare 

	mkdir -p /home/bage/gluster/mnt/gluster-object

start a instance

	$ docker run -d --privileged  -v /home/bage/gluster/fs/cgroup:/sys/fs/cgroup/:ro -p 8080:8080 -v /home/bage/gluster/mnt/gluster-object:/mnt/gluster-object -e S3_ACCOUNT="tv1" -e S3_USER="admin" -e S3_PASSWORD="redhat" gluster/gluster-s3



### 安装配置 Nacos

参考链接： https://hub.docker.com/r/nacos/nacos-server

Docker Pull Command

```
docker pull nacos/nacos-server
```

start a instance

```
docker run --name nacos-quick -e MODE=standalone -p 8849:8848 -d nacos/nacos-server:2.0.2
```

Docker Pull Command

```
http:localhost:8849 
```





### docker 安装ELK 【Mac】

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



### 安装ES【Mac】

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

### 安装skywalking

https://hub.docker.com/r/apache/skywalking-oap-server

https://skywalking.apache.org/docs/main/next/readme/

https://skywalking.apache.org/docs/main/next/en/setup/backend/backend-docker/

https://hub.docker.com/r/apache/skywalking-swck



Docker Pull Command

```
docker pull apache/skywalking-oap-server

docker pull apache/skywalking-ui

docker pull apache/skywalking-swck
```



Network？



Start an instance 

```
// Start a standlone container with H2 storage
$ docker run --name bage-skywalking-oap-server --restart always -d apache/skywalking-oap-server


$ docker run --name oap-ui --restart always -d -e SW_OAP_ADDRESS=http://oap:12800 apache/skywalking-ui

```



### 安装kibana【Mac】

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

### 安装 logstash【Mac】

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



### 安装 OceanBase【Mac】

参考链接 

https://open.oceanbase.com/docs/observer-cn/V3.1.3/10000000000096600

https://hub.docker.com/r/oceanbase/oceanbase-ce

Docker Pull Command

```
docker pull oceanbase/oceanbase-ce
```

To start an OceanBase instance, run this command:

```bash
docker run -p 2881:2881 --name obstandalone -d oceanbase/oceanbase-ce

docker run -p 2881:2881 --name bage-oceanbase-ce -d oceanbase/oceanbase-ce

docker logs bage-oceanbase-ce | tail -1
```

Connect 【todo】

```
docker exec -it bage-oceanbase-ce ob-mysql sys # Connect to sys tenant

```







### 安装 Postgres

参考链接 

https://hub.docker.com/_/postgres

Docker Pull Command

```
docker pull postgres
```

Start an instance

```bash
docker run --name bage-postgres -e POSTGRES_PASSWORD=mysecretpassword -d postgres


$ docker run -d \
	--name bage-postgres \
	-e POSTGRES_PASSWORD=mysecretpassword \
	-e PGDATA=/var/lib/postgresql/data/pgdata \
	-v /custom/mount:/var/lib/postgresql/data \
	postgres
```



### 安装 Jenkins【Mac,toCheck】

参考链接 

https://www.jenkins.io/zh/doc/book/installing/#downloading-and-running-jenkins-in-docker

https://hub.docker.com/r/jenkinsci/blueocean/

Docker Pull Command

```
docker pull jenkinsci/blueocean
```

Run 

```
docker run --name=bage-jenkins -p 8080:8080 jenkinsci/blueocean

docker run --name=bage-jenkins -u root -p 8080:8080 -p 50000:50000 -v /var/run/docker.sock:/var/run/docker.sock jenkinsci/blueocean 

```

To start an Jenkins instance, run this command:

```bash
docker run \
  -u root \
  --rm \  
  -d \ 
  -p 8080:8080 \ 
  -p 50000:50000 \ 
  -v jenkins-data:/var/jenkins_home \ 
  -v /var/run/docker.sock:/var/run/docker.sock \ 
  jenkinsci/blueocean 



```



### 安装 Redis-Stat 【Mac用不了】

参考链接 

https://hub.docker.com/r/insready/redis-stat

Docker Pull Command

```
docker pull insready/redis-stat
```

Run 

```console
docker run --name bage-redis-stat --link bage-redis:redis -p 8080:63790 -d insready/redis-stat --server redis
```



### 安装配置 Sentinel Dashboard

how to download

```
docker pull bladex/sentinel-dashboard
```

how to start

```
docker run --name bage-sentinel  -d -p 8858:8858 -d  bladex/sentinel-dashboard
```

how to visit

```
http://localhost:8858/

```

account and password: [sentinel/sentinel]



### 安装 mysqld_exporter

参考链接 

https://registry.hub.docker.com/r/prom/mysqld-exporter/

https://github.com/prometheus/mysqld_exporter

Docker Pull Command

```
docker pull prom/mysqld-exporter

```

Run 

```console

docker run --name bage-mysqld-exporter --add-host=host.docker.internal:host-gateway  -d -p 9104:9104 -e DATA_SOURCE_NAME="bage:bage@(host.docker.internal:3306)/mydbpro" prom/mysqld-exporter
                   
```

访问

```
http://localhost:9104/metrics

```



- **推荐图标ID：https://grafana.com/dashboards/7362**



### 安装 CK\clickhouse

参考链接 

https://hub.docker.com/r/clickhouse/clickhouse-server

Docker Pull Command

```
docker pull clickhouse/clickhouse-server
```

Run 

```
docker run -d --name bage-clickhouse-server --ulimit nofile=262144:262144 clickhouse/clickhouse-server
```

Visit

```

```



### 安装 Vault

参考链接 

https://github.com/hashicorp/vault

https://developer.hashicorp.com/vault/docs/configuration

官网 https://developer.hashicorp.com/vault/docs/get-started/developer-qs 

https://developer.hashicorp.com/vault/tutorials/getting-started/getting-started-first-secret



Docker Pull Command

```

https://hub.docker.com/r/hashicorp/vault
```

Run 

```
docker run -p 8200:8200 -e 'VAULT_DEV_ROOT_TOKEN_ID=dev-only-token' --name=bage-vault hashicorp/vault

docker run --cap-add=IPC_LOCK -e 'VAULT_DEV_ROOT_TOKEN_ID=mybageroot' -e 'VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8080' --name=bage-vault hashicorp/vault

docker run --cap-add=IPC_LOCK -e 'VAULT_DEV_ROOT_TOKEN_ID=mybageroot' -e 'VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8080' --name=bage-vault -p 8200:8200 hashicorp/vault

```

Config

```
export VAULT_TOKEN="dev-only-token"
export VAULT_ADDR="http://127.0.0.1:8200"
```

Set/Get Value 

```
vault kv put secret/github github.oauth2.key=foobar

vault kv put -mount=secret hello foo=world excited=yes
vault kv get -mount=secret hello
```



### 安装 Neo4j

参考链接 

https://hub.docker.com/_/neo4j

https://neo4j.com/docs/operations-manual/current/docker/

Docker Pull Command

```
docker pull neo4j
```

Run 

```console
docker run \
    --name=bage-neo4j --publish=7474:7474 --publish=7687:7687 \
    --volume=${HOME}/bage/docker-data/neo4j/:/data \
    neo4j
```

Visit

```
http://localhost:7474/browser/
```

By default, this requires you to login with `neo4j/neo4j` and change the password.



### 安装 TiDB

参考链接 

https://hub.docker.com/r/pingcap/tidb

https://docs-archive.pingcap.com/tidb/v2.1/test-deployment-using-docker

https://developer.aliyun.com/article/138312

Docker Pull Command

```
docker pull pingcap/tidb
```

Run 

```

docker run --name tidb-server -d -v ${HOME}/bage/docker-data/tidb:/tmp/tidb -p 4000:4000 -p 10080:10080 pingcap/tidb

docker run --name bage-tidb-server -d -v ${HOME}/bage/docker-data/tidb:/tmp/tidb -p 4000:4000 -p 10080:10080 pingcap/tidb:latest

```

Visit

```
# curl localhost:10080/status

# mysql -h 127.0.0.1 -P 4000 -u root -D test


```

### 安装 Minio

参考链接 

https://hub.docker.com/r/minio/minio

https://github.com/minio/minio/tree/master/docs/docker

https://github.com/minio/minio

Docker Pull Command

```
docker pull minio/minio
```

Run 

```
mkdir -p ~/bage/docker-data/minio

docker run \
  -p 9000:9000 \
  -p 9001:9001 \
  --name bage-minio \
  -v ${HOME}/bage/docker-data/minio:/data \
  -e "MINIO_ROOT_USER=bage" \
  -e "MINIO_ROOT_PASSWORD=bage-666666" \
  minio/minio server /data --console-address ":9001"
  
```

Visit

The MinIO deployment starts using default root credentials `minioadmin:minioadmin`. 

指定后则使用： bage / bage-666666

```
// API: 
http://127.0.0.1:9000 


// WebUI: 
http://127.0.0.1:9001 
```





### 安装 Zookeeper

参考链接 

https://hub.docker.com/_/zookeeper

https://farid-baharuddin.medium.com/setting-up-an-apache-zookeeper-cluster-in-docker-8960d5c23f5c

Docker Pull Command

```
docker pull zookeeper

```

Run 

```
mkdir -p ~/bage/docker-data/zookeeper


```

Visit

```
// API: 

```



### 安装 Pulsar

参考链接 

https://pulsar.apache.org/docs/next/getting-started-docker/

https://hub.docker.com/r/apachepulsar/pulsar-manager

https://pulsar.apache.org/docs/3.3.x/client-libraries-java/

Docker Pull Command

```
docker pull apachepulsar/pulsar
```

Run 

```
mkdir -p ${HOME}/bage/docker-data/pulsar
mkdir -p ${HOME}/bage/docker-conf/pulsar

docker run -it --name bage-pulsar \
-p 6650:6650 \
-p 8080:8080 \
--mount source=pulsardata,target=/pulsar/data \
--mount source=pulsarconf,target=/pulsar/conf \
apachepulsar/pulsar \
bin/pulsar standalone
```

Manager

```
docker pull apachepulsar/pulsar-manager


docker run -it \
  -p 9527:9527 -p 7750:7750 \
  -e SPRING_CONFIGURATION_FILE=/pulsar-manager/pulsar-manager/application.properties \
  apachepulsar/pulsar-manager
```



Visit

```
// Manager
http://localhost:7750/


```



### 安装 Excalidraw

参考链接 

https://excalidraw.com/

https://hub.docker.com/r/excalidraw/excalidraw

https://docs.excalidraw.com/docs/@excalidraw/excalidraw/installation

Docker Pull Command

```
docker pull apachepulsar/pulsar
```

Run 

```
docker run --rm -dit --name bage-excalidraw -p 5000:80 excalidraw/excalidraw

```

Visit

```
http://localhost:5000/

```

### 安装 Loki

参考链接 

https://hub.docker.com/r/grafana/loki

https://grafana.com/docs/loki/latest/setup/install/docker/

Docker Pull Command

```
docker pull grafana/loki
```

Run 

```
docker run --network bage-net -d --name=bage-loki -p 3100:3100 grafana/loki

docker run --network bage-net --name=bage-loki -p 3100:3100 grafana/loki

```

Visit

```
http://localhost:3100/ready

http://localhost:3100/metrics
```



### 安装 KKFileViewer

参考链接 

https://kkfileview.keking.cn/zh-cn/docs/production.html

Docker Pull Command

```
# 网络环境方便访问docker中央仓库
docker pull keking/kkfileview:4.1.0

# 网络环境不方便访问docker中央仓库
wget https://kkfileview.keking.cn/kkFileView-4.1.0-docker.tar
docker load -i kkFileView-4.1.0-docker.tar
```

Run 

```
docker run -it -p 8012:8012 keking/kkfileview:4.1.0

```

Visit

```
http://127.0.0.1:8012
```



### 安装 n8n

参考链接 

https://github.com/n8n-io/n8n

https://docs.n8n.io/

Docker Pull Command

```
docker run -it --rm --name n8n -p 5678:5678 docker.n8n.io/n8n-io/n8n

```

Run 

```
docker run -it --rm --name n8n -p 5678:5678 docker.n8n.io/n8n-io/n8n

```

Visit

```
 http://localhost:5678
```



### 安装 OpenHands

参考链接 

https://github.com/All-Hands-AI/OpenHands

https://docs.all-hands.dev/zh-Hans/modules/usage/getting-started

Docker Pull Command

```
docker pull docker.all-hands.dev/all-hands-ai/runtime:0.28-nikolaik 
```

Run 

```
mkdir ${HOME}/bage/docker-data/open-hands

docker run -it --rm --pull=always \
    -e SANDBOX_RUNTIME_CONTAINER_IMAGE=docker.all-hands.dev/all-hands-ai/runtime:0.28-nikolaik \
    -e LOG_ALL_EVENTS=true \
    -v ${HOME}/bage/docker-data/open-hands/docker.sock:/var/run/docker.sock \
    -v ${HOME}/bage/docker-data/open-hands/.openhands-state:/.openhands-state \
    -p 3000:3000 \
    --add-host host.docker.internal:host-gateway \
    --name bage-openhands-app \
    docker.all-hands.dev/all-hands-ai/openhands:0.28
 
```

Visit

```
 http://localhost:3000
```

config

```
配置AI 的Key
// 可以用来优化代码、生成一些代码实现
// 待验证
```



### 安装 Canal【TODO】

参考链接 

https://github.com/alibaba/canal

https://github.com/alibaba/canal/wiki/Docker-QuickStart

联MySQL  https://developer.aliyun.com/article/1048093



Docker Pull Command

```

docker pull canal/canal-server
```

Run 

```
docker network create bage-net

mkdir ${HOME}/bage/docker-conf/canal

# 启动容器（目的是拷贝配置文件）
docker run --name bage-canal \
-p 11111:11111  \
-id canal/canal-server

# 提前创建 /mydata/canal/conf目录，接着来执行拷贝配置文件命令
docker cp bage-canal:/home/admin/canal-server/conf/example/instance.properties  ${HOME}/bage/docker-conf/canal/
 
 
# 删除原先的canal服务
docker rm -f bage-canal
# 启动canal服务
# -i：让容器的标准输入保持打开（特别特别重要，注意不要是-d，一定要加上i）
docker run --network bage-net --name bage-canal \
-p 11111:11111  \
-v ${HOME}/bage/docker-conf/canal/instance.properties:/home/admin/canal-server/conf/example/instance.properties \
--link bage-mysq:bage-mysq \
-id canal/canal-server

```

MySQL

```

docker run --network bage-net --name bage-mysql-for-canal -v ${HOME}/bage/docker-data/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=bage -p 3306:3306 -d mysql/mysql-server mysqld \
  --datadir=/var/lib/mysql \
  --user=mysql \
  --server-id=1 \
  --log-bin=${HOME}/bage/docker-data/mysql/mysql-bin.log \
  --binlog_do_db=test
  
```

config

```

```



### 安装 MetaGPT

参考链接 

https://github.com/geekan/MetaGPT/blob/main/docs/README_CN.md

https://docs.deepwisdom.ai/main/zh/guide/get_started/installation.html#%E4%BD%BF%E7%94%A8docker%E5%AE%89%E8%A3%85

Docker Pull Command

```
docker pull metagpt/metagpt
```

Config

```
mkdir -p ${HOME}/bage/docker-conf/metagpt/{config,workspace}

mkdir ${HOME}/bage/docker-conf/metagpt/config
mkdir ${HOME}/bage/docker-conf/metagpt/workspace

```

Run 

```
docker run --rm metagpt/metagpt cat /app/metagpt/config/config2.yaml > ${HOME}/bage/docker-conf/metagpt/config/config2.yaml

vi ${HOME}/bage/docker-conf/metagpt/config/config2.yaml # 修改配置文件

# 你也可以启动一个容器并在其中执行命令
docker run --name bage-metagpt -d \
    --privileged \
    -v ${HOME}/bage/docker-conf/metagpt/config/config2.yaml:/app/metagpt/config/config2.yaml \
    -v ${HOME}/bage/docker-conf/metagpt/workspace:/app/metagpt/workspace \
    metagpt/metagpt
```

Visit

```
$ docker exec -it bage-metagpt /bin/bash

$ metagpt "Write a cli snake game"

```



### 安装 Dify

参考链接  https://docs.dify.ai/zh-hans/getting-started/install-self-hosted/docker-compose

Download

```
// 创建目录
cd ~/bage/github

// 下载 
git clone https://github.com/langgenius/dify.git --branch 0.15.3

```



Build

```
cd ~/bage/github

cd dify/docker

cd ~/bage/github/dify/docker

cp .env.example .env

// 启动 ////  如果是 v1 【docker-compose up -d】
docker compose up -d

关闭
docker compose down


```



Start

```
// 如果是docker v2 [docker compose version]
docker compose up -d

// v1
docker-compose up -d

// 状态检察 
docker compose ps


# 配置
http://localhost/install

// 访问
http://localhost
```



Config with Deepseek

```
https://docs.dify.ai/en/learn-more/use-cases/integrate-deepseek-to-build-an-ai-app
```



### 安装 Mermaid

参考链接  https://mermaid.js.org/intro/getting-started.html

https://juejin.cn/post/7493792787399770152

Download

```

```



Start

```
// 如果是docker v2 [docker compose version]
docker compose up -d

// 访问
http://localhost
```





Visit

```
// 如果是docker v2 [docker compose version]
docker compose up -d

// 访问
http://localhost
```



### 安装 AppWrite

参考链接  https://appwrite.io/docs

https://github.com/appwrite/appwrite

https://ipv6.rs/tutorial/macOS/Appwrite/

Download & start

```
docker run  --name bage-app-write -it  --rm \
    --volume /var/run/docker.sock:/var/run/docker.sock \
    --volume ${HOME}/bage/docker-conf/appwrite:/usr/src/code/appwrite:rw \
    --entrypoint="install" \
    appwrite/appwrite:1.7.3

docker run -it --rm --volume /var/run/docker.sock:/var/run/docker.sock --volume ${HOME}/bage/docker-data/appwrite:/usr/src/code/appwrite/data --env "APPWRITE_ENDPOINT=http://localhost:80/v1" --env "APPWRITE_PROJECT_ID=local" --env "APPWRITE_API_KEY=secret" appwrite/appwrite:1.7.3


cd ${HOME}/bage/docker-conf/appwrite


docker compose up -d --remove-orphans --renew-anon-volumes


```



Visit

```
// go to http://localhost to access the Appwrite console from your browser.
http://localhost
```



### 网络连接 network ###

参考链接 [https://docs.docker.com/network/bridge/](https://docs.docker.com/network/bridge/)、[https://stackoverflow.com/questions/54901581/connect-to-mysql-server-running-in-docker-container-from-another-container](https://stackoverflow.com/questions/54901581/connect-to-mysql-server-running-in-docker-container-from-another-container)

自定义 bright 网络，名字为 my-net

	docker network create my-net
	
	docker network create bage-net
	
	docker network connect bage-net container-xxx

在运行时候，添加参数，可以通过 myapp-xxx 别名访问

    --network my-net
    docker create --name my-nginx \
      --network my-net \
      --publish 8080:80 \
      nginx:latest

To connect a running container to an existing user-defined bridge,
      
    docker network connect bage-net my-nginx
    
    docker network connect bage-net bage-mysql
    docker network connect bage-net bage-mysqld-exporter
    docker network connect bage-net bage-prometheus

验证，可以在其他容器中，ping myapp-xxx

```
http://bage-redis:6379/metrics

http://host.docker.internal:6379/metrics
```

docker 配置访问宿主机 

https://docs.docker.com/desktop/networking/

```
host.docker.internal


```



### 批量保存

https://www.orchome.com/17100

```
docker save $(docker images -q) -o 

IDS=$(docker images | awk '{if ($1 ~ /^(debian|centos)/) print $3}')
docker save $IDS -o ~/bage/images/all.tar

docker load -i /path/to/save/mydockersimages.tar

```



### 常见错误 ###

1、启动centos镜像报错

    $ docker run centos
    WARNING: IPv4 forwarding is disabled. Networking will not work.
Redis启动后无法进行访问

[https://blog.csdn.net/weixin_39800144/article/details/79235460](https://blog.csdn.net/weixin_39800144/article/details/79235460 "参考链接1")
[https://blog.csdn.net/flymoringbird/article/details/80717700](https://blog.csdn.net/flymoringbird/article/details/80717700 "参考链接2")


访问 io超时问题处理！！


git remote set-url origin git@github.com:bage2014/simple-java-maven-app.git

2、拉取报错

```
Unable to find image 'docker.n8n.io/n8n-io/n8n:latest' locally

docker: Error response from daemon: Get "https://registry-1.docker.io/v2/": net/http: request canceled while waiting for connection (Client.Timeout exceeded while awaiting headers).

See 'docker run --help'.
```

