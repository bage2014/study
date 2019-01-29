# study-linux #
Linux学习笔记

## 常用命令 ##

### grep命令基本用法 ###
- 参考链接 [https://mp.weixin.qq.com/s?__biz=MzIwNTc4NTEwOQ==&mid=2247483656&idx=1&sn=17ad20cce3d6d4d587a905a706f488ed&chksm=972ad072a05d5964f28bb4c136572dbb1f75ad63d65d43d2e4b4bbf631f94fe5abe41e1cb40b&mpshare=1&scene=21&srcid=10073g3SoldEq4Vth1QQd6Z3#wechat_redirect](https://mp.weixin.qq.com/s?__biz=MzIwNTc4NTEwOQ==&mid=2247483656&idx=1&sn=17ad20cce3d6d4d587a905a706f488ed&chksm=972ad072a05d5964f28bb4c136572dbb1f75ad63d65d43d2e4b4bbf631f94fe5abe41e1cb40b&mpshare=1&scene=21&srcid=10073g3SoldEq4Vth1QQd6Z3#wechat_redirect "微信链接")

## 软件安装 ##

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

### Nexus ###
- 参考链接 
  - 官网 [https://www.sonatype.com/download-oss-sonatype](https://www.sonatype.com/download-oss-sonatype "官网")
  - 文档 [https://help.sonatype.com/repomanager3](https://help.sonatype.com/repomanager3 "文档") 
  - 下载地址 [https://sonatype-download.global.ssl.fastly.net/repository/repositoryManager/3/nexus-3.13.0-01-unix.tar.gz](https://sonatype-download.global.ssl.fastly.net/repository/repositoryManager/3/nexus-3.13.0-01-unix.tar.gz "下载地址")


