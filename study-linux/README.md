# study-linux #
Linux学习笔记

## 常用命令 ##

### grep命令基本用法 ###
- 参考链接 [https://mp.weixin.qq.com/s?__biz=MzIwNTc4NTEwOQ==&mid=2247483656&idx=1&sn=17ad20cce3d6d4d587a905a706f488ed&chksm=972ad072a05d5964f28bb4c136572dbb1f75ad63d65d43d2e4b4bbf631f94fe5abe41e1cb40b&mpshare=1&scene=21&srcid=10073g3SoldEq4Vth1QQd6Z3#wechat_redirect](https://mp.weixin.qq.com/s?__biz=MzIwNTc4NTEwOQ==&mid=2247483656&idx=1&sn=17ad20cce3d6d4d587a905a706f488ed&chksm=972ad072a05d5964f28bb4c136572dbb1f75ad63d65d43d2e4b4bbf631f94fe5abe41e1cb40b&mpshare=1&scene=21&srcid=10073g3SoldEq4Vth1QQd6Z3#wechat_redirect "微信链接")

## 软件安装 ##


### JDK+Maven ###

添加环境变量

    vi .bash_profile

末尾追加如下内容，
JAVA_HOME、MAVEN_HOME你懂的，需要修改成自己的目录

    export JAVA_HOME=/home/bage/professional/jdk1.8.0_131
    export MAVEN_HOME=/home/bage/professional/apache-maven-3.6.1
    export PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH
    export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

source 一下，使配置生效

    source .bash_profile


### Maven ###
- 参考链接 
  - 官网 [http://maven.apache.org/](http://maven.apache.org/ "官网")
  - 下载链接[http://mirrors.shu.edu.cn/apache/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz](http://mirrors.shu.edu.cn/apache/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz "下载链接")
  
### Nginx 安装 ###
- 参考链接 
  - 官网 [http://www.nginx.cn/doc/](http://www.nginx.cn/doc/ "官网")
  - 下载地址 [https://nginx.org/download/nginx-1.10.3.tar.gz](https://nginx.org/download/nginx-1.10.3.tar.gz "官网地址")
- 前期准备

`./configure` 缺少依赖pcre

    ./configure: error: the HTTP rewrite module requires the PCRE library.
    You can either disable the module by using --without-http_rewrite_module
    option, or install the PCRE library into the system, or build the PCRE library
    statically from the source with nginx by using --with-pcre=<path> option.
处理方式

    yum install pcre-devel

`./configure` 缺少依赖zlib

    ./configure: error: the HTTP gzip module requires the zlib library.
    You can either disable the module by using --without-http_gzip_module
    option, or install the zlib library into the system, or build the zlib library
    statically from the source with nginx by using --with-zlib=<path> option.
处理方式
    yum install zlib-devel

安装


    ./configure
或者

    ./configure --prefix=/home/bage/professional/nginx-1.15.0

#### 配置说明 ####
location配置参考 [https://www.cnblogs.com/coder-yoyo/p/6346595.html](https://www.cnblogs.com/coder-yoyo/p/6346595.html "nginx配置location总结")

    　　location = /uri 　　　  =    开头表示精确匹配，只有完全匹配上才能生效。
    　　location ^~ /uri 　　  ^~   开头对URL路径进行前缀匹配，并且在正则之前。
    　　location ~ pattern 　  ~    开头表示区分大小写的正则匹配。
    　　location ~* pattern 　 ~*   开头表示不区分大小写的正则匹配。
    　　location /uri 　　　　  /uri 不带任何修饰符，也表示前缀匹配，但是在正则匹配之后。
    　　location / 　　　　　   /    通用匹配，任何未匹配到其它location的请求都会匹配到，相当于switch中的default。

### ActiveMQ ###

- 环境搭建
  - 参考链接 [http://activemq.apache.org/getting-started.html](http://activemq.apache.org/getting-started.html "官网")
  - 下载地址 [http://www.apache.org/dyn/closer.cgi?filename=/activemq/5.15.4/apache-activemq-5.15.4-bin.tar.gz&action=download](http://www.apache.org/dyn/closer.cgi?filename=/activemq/5.15.4/apache-activemq-5.15.4-bin.tar.gz&action=download "官网")、
[http://mirrors.hust.edu.cn/apache//activemq/5.15.4/apache-activemq-5.15.4-bin.tar.gz](http://mirrors.hust.edu.cn/apache//activemq/5.15.4/apache-activemq-5.15.4-bin.tar.gz "国内镜像")

- 下载安装

Linux下载

    wget http://activemq.apache.org/path/tofile/apache-activemq-5.15.4-bin.tar.gz
加入安装路径

    cd ${activemq_install_dir}
    tar zxvf activemq-5.15.4-bin.tar.gz
启动

    cd ${activemq_install_dir}/bin
    ./activemq start


### Nodejs ###

- 环境搭建
  - 参考链接 [https://github.com/nodejs/help/wiki/Installation](https://github.com/nodejs/help/wiki/Installation)
  - 下载地址 [https://nodejs.org/en/download/](https://nodejs.org/en/download/)

- 下载安装

Linux下载


加入安装路径


启动


​    
​    
### Nexus ###
- 参考链接 
  - 官网 [https://www.sonatype.com/download-oss-sonatype](https://www.sonatype.com/download-oss-sonatype "官网")
  - 文档 [https://help.sonatype.com/repomanager3](https://help.sonatype.com/repomanager3 "文档") 
  - 下载地址 [https://sonatype-download.global.ssl.fastly.net/repository/repositoryManager/3/nexus-3.13.0-01-unix.tar.gz](https://sonatype-download.global.ssl.fastly.net/repository/repositoryManager/3/nexus-3.13.0-01-unix.tar.gz "下载地址")

### lrzsz ###

安装
    
    yum install lrzsz 

上传文件  

    rz
    
    rz -be 

### SSH KEY ###
参考链接 [https://help.github.com/articles/generating-an-ssh-key/](https://help.github.com/articles/generating-an-ssh-key/)、[https://help.github.com/en/github/authenticating-to-github/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent](https://help.github.com/en/github/authenticating-to-github/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent)


Steps

Open TerminalTerminalGit Bash.	
	略

Generating a new SSH key  

	ssh-keygen -t rsa -b 4096 -C "893542907@qq.com"

拷贝公钥

	cat /home/bage/.ssh/id_rsa.pub

配置多个(写入到另一个文件)

	/home/bage/.ssh/id_rsa_another_one.pub

添加对应私钥

	ssh-agent bash
	
	ssh-add ~/.ssh/id_rsa_another_one

新建文本文件

        vi C:\Users\bage\.ssh\config	

内容
	# 配置 github.com
	Host github.com               
	    HostName github.com
	    IdentityFile C:\\Users\\bage\\.ssh\\id_rsa_github
	    PreferredAuthentications publickey
	    User 893542907@qq.com	



### SFTP【待验证】 ###

参考链接 https://linuxeye.com/437.html


Steps

Open TerminalTerminalGit Bash.	
	略

切换到 root 用户

	su 

查看openssh的版本，版本必须大于4.8

	ssh -V 

创建sftp 用户组（名字建议就叫做sftp）

	groupadd sftp

创建sftp文件目录

	mkdir -p /data/sftp

设定Chroot目录权限

	chown -R root:sftp /data/sftp
	chmod 0755 /data/sftp

配置sshd_config（可以先备份文件）

	// vi 编辑
	vi /etc/ssh/sshd_config
	// 注释掉 Subsystem 这一行 
	# Subsystem     sftp    /usr/libexec/openssh/sftp-server
	// 文末尾添加
	Port 22
	Subsystem sftp internal-sftp -l INFO -f AUTH
	Match Group sftp
	ChrootDirectory /data/sftp/%u
	X11Forwarding no
	AllowTcpForwarding no
	ForceCommand internal-sftp -l INFO -f AUTH

设置SFTP用户可写入的目录

	mkdir /data/sftp/bagesftp
	chmod 0755 /data/sftp/bagesftp
	chown root:sftp /data/sftp/bagesftp

添加用户（-s 禁止登录   -M 不要自动建立用户的登入目录）

	useradd -g sftp -s /sbin/nologin -M bagesftp

创建密钥对

	mkdir -p /home/bagesftp/.ssh
	ssh-keygen -t rsa
	cp /root/.ssh/id_rsa.pub /home/bagesftp/.ssh/authorized_keys
	chown -R bagesftp.sftp /home/bagesftp

创建一个可写目录 upload

	mkdir /data/sftp/bagesftp/upload
	chown -R bagesftp:sftp /data/sftp/bagesftp/upload

重启sshd 服务

	systemctl restart sshd

连接验证

	sftp -oidentityFile=/root/.ssh/id_rsa bagesftp@127.0.0.1 -oport=22

