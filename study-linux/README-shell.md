# study-linux #
## 参考链接

linux 在线： https://copy.sh/v86/?profile=linux26

https://linuxtools-rst.readthedocs.io/zh_CN/latest/index.html

http://cn.linux.vbird.org/

## 参数风格

-： 一横说明参数是 字符 形式
--：两横说明参数是单词形式
-：参数前有横的是 System V风格
空：参数前没有横的是 BSD风格



## 优缺点 ## 

### 优点 ###

操作系统，并肩 windows、MacOS
开源
稳定
省资源
服务器

### 缺点 ###

无特定厂商
部分软件、游戏不支持
用户友好特性欠缺

## 启动过程

- 内核的引导。
- 运行 init。
- 系统初始化。
- 建立终端 。
- 用户登录系统。





## 软件安装 ##






### 域名映射 ###

修改/etc/hosts文件

    vi /etc/hosts


末尾追加如下内容，

    127.0.0.1	localhost.localdomain	localhost 


hosts文件格式是一行一条记录，分别是IP地址 hostname aliases，三者用空白字符分隔，aliases可选。

    IP addr		hostname				alias



### nc 【netcat】 ###

主要作用
（1）端口侦听，nc 可以作为 server 以 TCP 或 UDP 方式侦听指定端口；
（2）端口扫描，nc 可以作为 client 发起 TCP 或 UDP 请求；
（3）机器之间传输文件；
（4）机器之间网络测速。

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



## 主机名 ##

查看主机名

    hostname

暂时更改主机名

    # hostname <host-name> 

永久修改主机名

    $ sudo hostnamectl --static set-hostname <host-name>
    $ sudo hostnamectl --transient set-hostname <host-name>
    $ sudo hostnamectl --pretty set-hostname <host-name>

## 压缩解压 ##

### tar 类

压缩，将 hello 文件夹压缩到 hello.tar.gz 

    tar -cvzf hello.tar.gz hello/

解压，将 hello.tar.gz 压缩文件解压到 hello文件夹

    tar -xvf hello.tar.gz hello/

参数含义

```
The other synopsis forms show the preferred usage.  The first option to
     tar is a mode indicator from the following list:
     -c      Create a new archive containing the specified items.  The long
             option form is --create.
     -f file, --file file
             Read the archive from or write the archive to the specified file.
             The filename can be - for standard input or standard output.  The
             default varies by system; on FreeBSD, the default is /dev/sa0; on
             Linux, the default is /dev/st0.
     -r      Like -c, but new entries are appended to the archive.  Note that
             this only works on uncompressed archives stored in regular files.
             The -f option is required.  The long option form is --append.
     -t      List archive contents to stdout.  The long option form is --list.
     -u      Like -r, but new entries are added only if they have a modifica-
             tion date newer than the corresponding entry in the archive.
             Note that this only works on uncompressed archives stored in reg-
             ular files.  The -f option is required.  The long form is
             --update.
     -x      Extract to disk from the archive.  If a file with the same name
             appears more than once in the archive, each copy will be
             extracted, with later copies overwriting (replacing) earlier
             copies.  The long option form is --extract.
     -v, --verbose
             Produce verbose output.  In create and extract modes, tar will
             list each file name as it is read from or written to the archive.
             In list mode, tar will produce output similar to that of ls(1).
             An additional -v option will also provide ls-like details in cre-
             ate and extract mode.
     -z, --gunzip, --gzip
             (c mode only) Compress the resulting archive with gzip(1).  In
             extract or list modes, this option is ignored.  Note that, unlike
             other tar implementations, this implementation recognizes gzip
             compression automatically when reading archives.
```



### zip 类

压缩，将 hello 文件夹压缩到 hello.zip

    zip hello.zip hello/

解压，将 hello.zip 压缩文件解压到 hello文件夹

    unzip hello.zip hello/



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



## 网络



## 用户和组

https://www.runoob.com/linux/linux-user-manage.html





## 权限 



```
chmod 777 ./abc.txt

```



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

### 基本操作

切换目录

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



### 内容查看 ###

翻页查看内容

    more ./hello.txt

从文件尾部查看

    tail ./hello.txt

从文件开头查看

    head ./hello.txt 

查看文件所有内容 

    cat ./hello.txt 

### 属性查看

查看文件最新更新等属性

    stat ./hello.txt 

文件字数、行数统计

    wc ./hello.txt

查看文件编码

    file ./hello.txt



## 磁盘管理 ##

- **df**（英文全称：disk free）：列出文件系统的整体磁盘使用量

- **du**（英文全称：disk used）：检查磁盘空间使用量
- **fdisk**：用于磁盘分区



## rpm 软件安装 ##

查询软件 <package_name> 是否安装

    rpm -qa | grep <package_name>

安装软件 <package_name> 

    rpm - ivh <package_name>

升级软件 <package_name> 

    rpm -Uvh <package_name>

卸载软件 <package_name> 

    rpm -e <package_name>



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
```



### pmap

### sar

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

### pstack

### pstrace

## 查找类 ##
### grep
用户在文本内容中匹配字符，并输出

基本用法，从hello.txt 中查找bage字符串
```
 grep bage hello.txt
```

从hello.txt 中查找bage字符串，不显示整行

```
 grep -o bage hello.txt
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


### find
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

