# study-linux #
Linux学习笔记



启动过程

- 内核的引导。
- 运行 init。
- 系统初始化。
- 建立终端 。
- 用户登录系统。



## 常用命令 ##

linux 在线： https://copy.sh/v86/?profile=linux26



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





### vi 编辑器【待验证】 ###



编辑模式 -> 命令模式

    Esc 



命令模式 -> 编辑模式

    a // 当前字符后添加
    
    A // 行末添加
    
    i // 当前字符前插入
    
    I // 行首插入
    
    o // 当前行下面插入一空行
    
    O // 当前行前面插入一空行




查看模式

    dd // 删除光标坐在行
    ZZ // 保存文件内容
    x // 将光标处的字符删除  
    :wq // 保存并退出 
    :w // 保存 
    :w! // 强制保存
    :q // 退出
    :q! // 强制退出 
    :set number // 显示行号 
    :set nonumber // 隐藏行号
    :n // 跳到第 n 行 行首
    :$ // 跳转到最后一行 
    x // 删除当前光标字符,光标之后
    X // 删除光标之前一个字符
    D // 删除当前光标到行尾的全部字符 
    dd // 删除光标所在行整行 
    ndd // 删除当前行及其后 n-1 行，一共删除 n 行 
    nyy // 拷贝当前行及其后 n-1 行内容 
    p // [小写p]粘贴到当前光标所在位置的下方
    P // [大写P]粘贴到当前光标所在位置的上方  
    /xyz // 光标开始，向下查找 xyz 字符串,按 n 查找下一个 
    ?xyz // 光标开始，向上查找 xyz 字符串 
    :s/F/R // 替换文本操作，将F字符串换成T字符








监听 1234端口

    nc -l 1234




请求 本地的 1234 端口

    nc localhost 1234 



## 主机名 ##

查看主机名

    hostname

暂时更改主机名

    # hostname <host-name> 

永久修改主机名

    $ sudo hostnamectl --static set-hostname <host-name>
    $ sudo hostnamectl --transient set-hostname <host-name>
    $ sudo hostnamectl --pretty set-hostname <host-name>

## tar压缩解压 ##

压缩，将 hello 文件夹压缩到 hello.tar.gz 

    tar -cvzf hello.tar.gz hello/

解压，将 hello.tar.gz 压缩文件解压到 hello文件夹

    tar -xvf hello.tar.gz hello/

## 防火墙 ##

查看防火墙状态

    # service iptable status
    # systemctl status firewalld.service // centos7

关闭防火墙

    # service iptable stop
    # systemctl stop firewalld.service // centos7

打开防火墙

    # service iptable start
    # systemctl start firewalld.service // centos7

禁用防火墙

    # service iptable disable 
    # systemctl disable firewalld.service // centos7

启用防火墙

    # service iptable enable 
    # systemctl enable firewalld.service // centos7

永久关闭|开启防火墙

    # chkconfig httpd on | off
    # chkconfig iptable on | off	





## 其他 ##



设置普通用户的超级用户权限

    # vi /etc/sudoers
    # username ALL=(root)NOPASSWD:ALL	



查看在线

    who	



crontab 

查看

    crontab -l	

查看

    crontab -e

查看

    crontab -root

说明

| *        | *        | *        | *        | *         |
| -------- | -------- | -------- | -------- | --------- |
| 分：1-59 | 时：0-23 | 日：1-31 | 月：1-12 | 星期：0-6 |


## 文件与目录 ##

| 硬件设备      | 文件名称                                                     |
| ------------- | ------------------------------------------------------------ |
| IDE设备       | /dev/hd[a-d]，现在的 IDE设备已经很少见了，因此一般的硬盘设备会以 /dev/sd 开头。 |
| SCSI/SATA/U盘 | /dev/sd[a-p]，一台主机可以有多块硬盘，因此系统采用 a~p 代表 16 块不同的硬盘。 |
| 软驱          | /dev/fd[0-1]                                                 |
| 打印机        | /dev/lp[0-15]                                                |
| 光驱          | /dev/cdrom                                                   |
| 鼠标          | /dev/mouse                                                   |
| 磁带机        | /dev/st0 或 /dev/ht0                                         |

ls: 列出目录及文件名
cd：切换目录
pwd：显示目前的目录
mkdir：创建一个新的目录
rmdir：删除一个空的目录
cp: 复制文件或目录
rm: 移除文件或目录
mv: 移动文件与目录，或修改文件与目录的名称

-a	显示所有文件及目录 (包括以“.”开头的隐藏文件)
-l	使用长格式列出文件及目录信息
-r	将文件以相反次序显示(默认依英文字母次序)
-t	根据最后的修改时间排序
-A	同 -a ，但不列出 “.” (当前目录) 及 “..” (父目录)
-S	根据文件大小排序
-R	递归列出所有子目录

## 文件属性 ##





## 磁盘管理 ##

- df：列出文件系统的整体磁盘使用量
- du：检查磁盘空间使用量
- fdisk：用于磁盘分区







## yum 命令 ##

Yellow dog Updater, Modified

基于 RPM包 管理



安装软件 <package_name>

    yum install <package_name>



移除软件 <package_name>

    yum remove <package_name>



查找软件 <keyword>

    yum search <keyword>



## 关机 ##

正确的关机流程为：sync > shutdown > reboot > halt

关机指令为：shutdown ，你可以man shutdown 来看一下帮助文档。

最后总结一下，不管是重启系统还是关闭系统，首先要运行 **sync** 命令，把内存中的数据写到磁盘中



