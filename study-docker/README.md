# study-docker #
docker 学习笔记

## 环境搭建 ##

### 参考链接 ###
- Docker 官网 [https://docs.docker.com/](https://docs.docker.com/ "Docker 官网")
- CentOS安装 [https://docs.docker.com/install/linux/docker-ce/centos/#install-using-the-convenience-script](https://docs.docker.com/install/linux/docker-ce/centos/#install-using-the-convenience-script "CentOS安装")
- CentOS安装 [https://docs.docker.com/install/linux/docker-ce/centos/#install-using-the-convenience-script](https://docs.docker.com/install/linux/docker-ce/centos/#install-using-the-convenience-script "CentOS安装")

### 安装 ###
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

    $ docker run -v /home/bage/mnt-tomcat:/mnt -it --rm -p 8888:8080 tomcat
    
    
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

Start a mysql server instance

    docker run --name bage-mysql -v /home/bage/data/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=bage -p 3306:3306 -d bage-mysql
其中

	--name 起名 bage-mysql 
	-v 挂载目录 /home/bage/data/mysql:/var/lib/mysql 
	-e 设置root密码 MYSQL_ROOT_PASSWORD=bage 
	-p 映射端口 3306:3306 
	-d bage-mysql

进入mysql容器

    docker exec -it bage-mysql /bin/bash
在容器内部进行登录

    mysql -u root –p
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


### 安装配置Postgres ###
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
下载安装：

    docker pull nginx
启动：

    docker run -p 80:80 --name tmp-nginx-container -d nginx
进入容器：

    docker exec -it tmp-nginx-container /bin/bash
默认配置文件位置：

    /etc/nginx/nginx.conf 
和

    /etc/nginx/conf.d/default.conf

新建一个临时编辑目录：

    mkdir -p /home/bage/workspace/docker/docker/
拷贝配置文件出来进行编辑:

    docker cp tmp-nginx-container:/etc/nginx/conf.d/default.conf /home/bage/workspace/docker/docker/
编辑修改后进行返回：

    docker cp /home/bage/workspace/docker/docker/ tmp-nginx-container:/etc/nginx/nginx.conf

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
    
 自定义配置文件启动
 
	docker run -p 8379:6379 -v /home/bage/conf/redis.conf:/usr/local/etc/redis/redis.conf --name redis -d redis redis-server /usr/local/etc/redis/redis.conf  --requirepass "bage.redis"

### 安装配置RabbitMQ ###
参考链接：[https://hub.docker.com/_/rabbitmq](https://hub.docker.com/_/rabbitmq "安装配置RabbitMQ")

Docker Pull Command

    docker pull rabbitmq

启动

	docker run -d --hostname my-rabbit --name bage-rabbit -p 15672:15672 -p 5672:5672 -e RABBITMQ_DEFAULT_USER=bage -e RABBITMQ_DEFAULT_PASS=bage rabbitmq:3-management
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

[http://{ip}:15672/](http://{ip}:15672/)


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


### 安装部署portainer  ###
参考链接: [https://hub.docker.com/r/portainer/portainer]()、[https://portainer.readthedocs.io/en/latest/deployment.html](https://portainer.readthedocs.io/en/latest/deployment.html)

Docker Pull Command

    docker pull portainer/portainer

启动(/home/bage/data/portainer 持久化数据目录)

    docker run -d -p 9000:9000 --name portainer --restart always -v /var/run/docker.sock:/var/run/docker.sock -v /home/bage/data/portainer:/data portainer/portainer


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
Docker Pull Command

    docker pull docker.elastic.co/elasticsearch/elasticsearch:6.7.2
    
    docker pull elasticsearch

启动 

    docker run -p 8892:9200 -p 8893:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:6.7.2
    
    docker run -d --name elasticsearch -p 8892:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch

 
访问

    http://{ip}:8892/_cat/health  


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


### 网络连接 ###
参考链接 [https://docs.docker.com/network/bridge/](https://docs.docker.com/network/bridge/)、[https://stackoverflow.com/questions/54901581/connect-to-mysql-server-running-in-docker-container-from-another-container](https://stackoverflow.com/questions/54901581/connect-to-mysql-server-running-in-docker-container-from-another-container)

自定义 bright 网络，名字为 my-net
 
	docker network create my-net

在运行时候，添加参数，可以通过 myapp-xxx 别名访问

    --network my-net
    docker create --name my-nginx \
      --network my-net \
      --publish 8080:80 \
      nginx:latest
      
To connect a running container to an existing user-defined bridge,
      
    docker network connect my-net my-nginx


验证，可以在其他容器中，ping myapp-xxx

### 常见错误 ###
启动centos镜像报错

    $ docker run centos
    WARNING: IPv4 forwarding is disabled. Networking will not work.
Redis启动后无法进行访问

[https://blog.csdn.net/weixin_39800144/article/details/79235460](https://blog.csdn.net/weixin_39800144/article/details/79235460 "参考链接1")
[https://blog.csdn.net/flymoringbird/article/details/80717700](https://blog.csdn.net/flymoringbird/article/details/80717700 "参考链接2")
