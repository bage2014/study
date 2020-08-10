# study-linux #
Linux学习笔记

## 常用命令 ##





### grep命令基本用法 ###
- 参考链接 [https://mp.weixin.qq.com/s?__biz=MzIwNTc4NTEwOQ==&mid=2247483656&idx=1&sn=17ad20cce3d6d4d587a905a706f488ed&chksm=972ad072a05d5964f28bb4c136572dbb1f75ad63d65d43d2e4b4bbf631f94fe5abe41e1cb40b&mpshare=1&scene=21&srcid=10073g3SoldEq4Vth1QQd6Z3#wechat_redirect](https://mp.weixin.qq.com/s?__biz=MzIwNTc4NTEwOQ==&mid=2247483656&idx=1&sn=17ad20cce3d6d4d587a905a706f488ed&chksm=972ad072a05d5964f28bb4c136572dbb1f75ad63d65d43d2e4b4bbf631f94fe5abe41e1cb40b&mpshare=1&scene=21&srcid=10073g3SoldEq4Vth1QQd6Z3#wechat_redirect "微信链接")

## 软件安装 ##






### 域名映射 ###

修改/etc/hosts文件

    vi /etc/hosts
    

末尾追加如下内容，

    127.0.0.1	localhost.localdomain	localhost 
    

hosts文件格式是一行一条记录，分别是IP地址 hostname aliases，三者用空白字符分隔，aliases可选。

    IP addr		hostname				alias



### nc 【netcat】 ###

模拟TCP网络通讯
端口扫描



监听 1234端口

    nc -l 1234
    



请求 本地的 1234 端口

    nc localhost 1234 
    



扫描端口：

    nc -v ip -z 8000-9000

说明，扫描 8000-9000的端口

nc -v ip -z startPort-endPort