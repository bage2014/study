
# study-Linux-Command #
## 系统简介 ## 

### 优点 ###

- 开源
- 稳定
- 操作系统，并肩 windows、MacOS

### 缺点 ###

- 无特定厂商
- 部分软件、游戏不支持
- 用户友好特性欠缺



### 一切皆文件 ###

Linux 中一切都是文件；
目录、文件名称长度限制  <= 255 
大小写敏感，无拓展名概念

| 硬件设备      | 文件名称                                                     |
| ------------- | ------------------------------------------------------------ |
| IDE设备       | /dev/hd[a-d]，现在的 IDE设备已经很少见了，因此一般的硬盘设备会以 /dev/sd 开头。 |
| SCSI/SATA/U盘 | /dev/sd[a-p]，一台主机可以有多块硬盘，因此系统采用 a~p 代表 16 块不同的硬盘。 |
| 软驱          | /dev/fd[0-1]                                                 |
| 打印机        | /dev/lp[0-15]                                                |
| 光驱          | /dev/cdrom                                                   |
| 鼠标          | /dev/mouse                                                   |
| 磁带机        | /dev/st0 或 /dev/ht0                                         |



## 参数风格

-： 一横说明参数是 字符 形式
--：两横说明参数是 单词 形式

-：参数前有横的是 System V风格
空：参数前没有横的是 BSD风格



## 文件与目录

### 基本操作

切换目录 Change working directory

    cd

显示当前目录

```
pwd 
```

查看当前目录下的文件和文件夹

    ls 

创建文件夹 xxx

    mkdir xxx

删除空文件夹

    rmdir xxx

创建文件

    touch xxx.txt

删除文件

    rm xxx.txt

移动

    mv ./old-dir/abc.txt ./new-dir/ 

重命名

    mv ./abc.txt ./abcdefg.txt 

### 内容查看

翻页查看内容

```
more ./hello.txt
```

从文件尾部查看

```
tail ./hello.txt
```

从文件开头查看

```
head ./hello.txt 
```

查看文件所有内容 

```
cat ./hello.txt 
```

### 提高层次

查看文件最新更新等属性

```
stat ./hello.txt 
```

文件字数、行数统计

```
wc ./hello.txt
```

查看文件编码

```
file ./hello.txt
```

拷贝 -r 递归拷贝

    cp -r ./old-dir/* ./new-dir 

文件切分

```
split -l 6 hello.txt

[Linux]
split -l 3 hello.txt -d -a 3 url_

[Mac]
split -l 3 hello.txt hello__

```

### 文件编辑

#### vi编辑器

**模式概念**

编辑模式 vs 命令模式

**模式切换**

- i +

命令 -> 辑

+ ESC

**拓展**

编辑 -> 命令

```
a // 当前字符后添加
A // 行末添加
i // 当前字符前插入
I // 行首插入
o // 当前行下面插入一空行
O // 当前行前面插入一空行
```

查看模式

```
:wq // 保存并退出 
:w // 保存 
:w! // 强制保存
:q // 退出
:q! // 强制退出 
:set number // 显示行号 
:set nonumber // 隐藏行号
:n // 跳到第 n 行 行首
:$ // 跳转到最后一行 

dd // 删除光标坐在行
ndd // 删除当前行及其后 n-1 行，一共删除 n 行

nyy // 拷贝当前行及其后 n-1 行内容 
p // [小写p]粘贴到当前光标所在位置的下方
P // [大写P]粘贴到当前光标所在位置的上方  

/xyz // 光标开始，向下查找 xyz 字符串,按 n 查找下一个 
?xyz // 光标开始，向上查找 xyz 字符串 
:s/F/R // 替换文本操作，将F字符串换成T字符

x // 删除当前光标字符,光标之后
X // 删除光标之前一个字符
D // 删除当前光标到行尾的全部字符 
ZZ // 保存文件内容
x // 将光标处的字符删除  

```

### 查找类

#### find

在目录中查找匹配的文件

从当前目录以及子目录中查找文件名是hello.txt的文件

```
 find ./ -name hello.txt
```

从当前目录以及子目录中查找文件名是hello.txt的文件，忽略大小写

```
 find ./ -iname hello.txt
```

从当前目录以及子目录中查找文件是*.txt的文件，正则表达式

```
 find ./ -iregex ".*\(\.txt\)$"
```

#### grep

用户在文本内容中匹配字符，并输出

基本用法，从hello.txt 中查找bage字符串

```
 grep bage hello.txt
```

从hello.txt 中反向查找 bage 字符串，并显示前后各5行

```
 grep -v -A 5 -B 5 bage hello.txt
```

忽略大小写，从hello.txt 中查找bage字符串，并高亮颜色

```
 grep -i --color bage hello.txt
```

正则表达式，从 hello.txt 中查找匹配 bage 或 cage的行

```
 grep -e '[bc]age' hello.txt
```

正则表达式，从 hello.txt 中查找不匹配 bage 或 cage的行;并显示行号

```
 grep -n -v  -e '[bc]age' hello.txt
```



## 软件安装

### yum

Yellow dog Updater, Modified

基于 RPM包 管理

安装软件 package_name

```
yum install <package_name>
```

移除软件 package_name

```
yum remove <package_name>
```

查找软件

```
yum search <keyword>
```

### rpm

查询软件 <package_name> 是否安装

```
rpm -qa | grep <package_name>
```

安装软件 <package_name> 

```
rpm - ivh <package_name>
```

升级软件 <package_name> 

```
rpm -Uvh <package_name>
```

卸载软件 <package_name> 

```
rpm -e <package_name>
```





## 磁盘管理 ##

- **df**（英文全称：disk free）：列出文件系统的整体磁盘使用量
当前目录使用情况
```
df -h ./
Filesystem      Size  Used Avail Use% Mounted on
/dev/vda1        50G  6.7G   41G  15% /
```

- **du**（英文全称：disk used）：检查磁盘空间使用量

```
du -h ./hhh
8.0K    ./hhh/hello-in-hhh
12K     ./hhh
```


## 压缩解压

### tar 类

压缩，将 hello 文件夹压缩到 hello.tar.gz 

```
tar -cvzf hello.tar.gz hello/
```

解压，将 hello.tar.gz 压缩文件解压到 hello文件夹

```
tar -xvf hello.tar.gz hello/
```



### zip 类

压缩，将 hello 文件夹压缩到 hello.zip

```
zip hello.zip hello/
```

解压，将 hello.zip 压缩文件解压到 hello文件夹

```
unzip hello.zip hello/
```



## 防火墙

查看防火墙状态

```
# service iptable status
# systemctl status firewalld.service // centos7
```

关闭防火墙

```
# service iptable stop
# systemctl stop firewalld.service // centos7
```

打开防火墙

```
# service iptable start
# systemctl start firewalld.service // centos7
```

禁用防火墙

```
# service iptable disable 
# systemctl disable firewalld.service // centos7
```

启用防火墙

```
# service iptable enable 
# systemctl enable firewalld.service // centos7
```

永久关闭|开启防火墙

```
# chkconfig httpd on | off
# chkconfig iptable on | off	
```



## 网络

ping 
```
ping www.baidu.com
```
netstat 
netstat命令是一个用于检查本地计算机与其他计算机之间的网络连接情况的工具。通过使用不同的参数linux web服务器，可以获取各种有关网络连接信息的详细数据  
端口监听
```
netstat -an | grep 3306
tcp        0      0 0.0.0.0:3306            0.0.0.0:*               LISTEN     
tcp        0      0 127.0.0.1:3306          127.0.0.1:45736         ESTABLISHED          
unix  2      [ ]         DGRAM                    135330652 

```
统计信息  
```
netstat -s 

```

wget 
wget -c url - 断电下载
```
wget -c https://console.cloud.tencent.com/lighthouse/instance/detail?searchParams=rid%3D1&rid=4&id=lhins-nlurh7nu&action=DescribeInstanceLogin
```

curl 
```
curl http://localhost:8080
```

ssh
远程
```
ssh bage@127.0.0.1

```

scp  远程拷贝
```
scp ./abc.txt user@host:/abc.txt

```


## 用户和组

https://www.runoob.com/linux/linux-user-manage.html

    useradd - 添加新用户
    passwd - 设置密码 
    usrrdel - 删除用户
    groupadd -创建用户组
    groupdel - 删除
    su - 切换用户whoami

    

## 权限 



```
chmod 777 ./abc.txt

```



## 进程

### ps

基本用法

```
ps
```

多字段显示

```
ps -l
```

根据进程号过滤

```
ps -p 12345
```

显示 用户 bage 的进程

```
ps -u bage
```

内存中的程序

```
ps aux =?= ps -ef 
```



### lsof

查看那个程序占用了abc.txt

```
lsof abc.txt
```

查看进程 12345,123456 占用的文件

```
lsof -p 12345,123456
```

查看非用户 bage 占用了那些文件

```
lsof -u ^bage
```

查看谁占用了3306端口

```
lsof -i:3306
```



### kill

列出所有信号

```
kill -l
```

列出所有信号

```
kill -9 xxx
```

java 程序生成 dump 文件

```
kill -3 xxx
```



### top

实时显示进程信息

```
top

Processes: 474 total, 3 running, 471 sleeping, 2766 threads            08:51:10
Load Avg: 1.35, 1.67, 1.91  CPU usage: 15.23% user, 7.43% sys, 77.33% idle
SharedLibs: 440M resident, 89M data, 108M linkedit.
MemRegions: 251913 total, 5410M resident, 739M private, 3457M shared.
PhysMem: 15G used (1513M wired), 102M unused.
VM: 189T vsize, 3272M framework vsize, 0(0) swapins, 0(0) swapouts.
Networks: packets: 1603397/1093M in, 1837984/1074M out.
Disks: 557660/13G read, 113712/2110M written.

PID   COMMAND      %CPU  TIME     #TH    #WQ  #PORT MEM    PURG   CMPRS  PGRP
75    fseventsd    81.1  00:36.26 12/1   1    266   4898K  0B     704K   75
144   WindowServer 19.9  04:19.81 21     5    3985  609M-  15M+   64M    144
0     kernel_task  14.4  03:27.33 479/8  0    0     7840K+ 0B     0B     0
1806  Terminal     10.2  00:29.08 8      3    294-  113M+  6192K  11M    1806
2781  QQMusic      9.6   01:11.85 57     6    721+  149M+  96K+   33M-   2781
749   qemu-system- 6.5   06:41.35 9      0    24    8872M  0B     455M   570
2791  com.apple.We 6.2   00:35.60 4      2    103   105M   0B     19M    2791
111   opendirector 6.1   00:05.71 9      8    1205- 14M    0B     5216K  111
2859  top          4.7   00:31.99 1/1    0    47+   5137K  0B     880K   2859
```




### free

内存使用情况

无任何参数

```
free
```

人性化每 5s 显示一次

```
free -h -s 5
```



### watch

周期性执行指定命令；全屏显示

每三秒执行一次 date 命令

```
watch -n 3 date
```

高亮显示差异内容

```
watch -n 3 -d date
```

当呆执行的命令存在空格换行或参数时，可以使用 ' '

```
 watch -n 3 'ls | grep txt'
```



### netstat 



### iostat
执行1次
```
iostat 1

              disk0       cpu    load average
    KB/t  tps  MB/s  us sy id   1m   5m   15m
   22.62  277  6.12  12  9 79  1.50 4.13 3.53
```
### pstack
```
$ pstack 7790
#0  0x00007fe52186f60c in waitpid () from /lib64/libc.so.6
#1  0x0000000000440c34 in waitchld.isra.10 ()
#2  0x0000000000441eec in wait_for ()
#3  0x0000000000433b1e in execute_command_internal ()
#4  0x0000000000433d3e in execute_command ()
#5  0x000000000041e375 in reader_loop ()
#6  0x000000000041c9de in main ()
```


## 其他+
### 软连接
ln -s - 创建软连接
### ifconfig

### 历史啥的
who
history
### 重定向
\> >> 2> 重定向
### 管道
｜ 管道



### 主机名

查看主机名

```
hostname
```

暂时更改主机名

```
# hostname <host-name> 
```

永久修改主机名

```
$ sudo hostnamectl --static set-hostname <host-name>
$ sudo hostnamectl --transient set-hostname <host-name>
$ sudo hostnamectl --pretty set-hostname <host-name>
```



### 关机

正确的关机流程为：sync > shutdown > reboot > halt

关机指令为：shutdown ，你可以man shutdown 来看一下帮助文档。

最后总结一下，不管是重启系统还是关闭系统，首先要运行 **sync** 命令，把内存中的数据写到磁盘中

### 超级用户权限

设置普通用户的超级用户权限

```
# vi /etc/sudoers
# username ALL=(root)NOPASSWD:ALL	
```



### 查看在线

```
who	
```



### crontab 

查看

```
crontab -l	
```

查看

```
crontab -e
```

查看

```
crontab -root
```

说明

| *        | *        | *        | *        | *         |
| -------- | -------- | -------- | -------- | --------- |
| 分：1-59 | 时：0-23 | 日：1-31 | 月：1-12 | 星期：0-6 |



### nc 【netcat】

主要作用
（1）端口侦听，nc 可以作为 server 以 TCP 或 UDP 方式侦听指定端口；
（2）端口扫描，nc 可以作为 client 发起 TCP 或 UDP 请求；
（3）机器之间传输文件；
（4）机器之间网络测速。

监听 1234端口

```
nc -l 1234
```



请求 本地的 1234 端口

```
nc localhost 1234 
```



扫描端口：

```
nc -v ip -z 8000-9000
```

说明，扫描 8000-9000的端口

nc -v ip -z startPort-endPort




## 总结

重点 + 目标 

【查漏补缺】

文件基础 + 查找 + 权限 + 软件 + 网络 + 进程

### 该会的要会

- 知识概述
- 基本命令

### 学习ING

- 知识成体系

- 点到面

### 帮助文档

- man 命令
- info ？
- whatis ?



## 高阶 TODO

### Shell脚本

命令复用

提高效率

### Linux 系统

发展史

内核+架构

### 开发软件安装

jdk\maven\mysql\

### Docker

docker 

docker + 

## 参考链接

linux 在线： https://copy.sh/v86/?profile=linux26

https://linuxtools-rst.readthedocs.io/zh_CN/latest/index.html

http://cn.linux.vbird.org/

https://www.linuxcool.com/

