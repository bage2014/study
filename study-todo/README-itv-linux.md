# Linux 技术总结

## 1. 基础概念

### 1.1 什么是 Linux
Linux 是一种开源的、自由的类 Unix 操作系统内核，由 Linus Torvalds 于 1991 年创建。它是目前世界上最流行的服务器操作系统，也是许多嵌入式系统和移动设备（如 Android）的基础。

### 1.2 Linux 内核概念
Linux 内核是 Linux 操作系统的核心部分，负责管理系统的硬件资源和提供基本服务。主要功能包括：
- **内存管理**：管理系统的物理内存和虚拟内存
- **进程管理**：调度和管理系统中的进程
- **硬件管理**：通过设备驱动程序与硬件设备交互
- **文件管理**：提供文件系统接口和管理
- **网络管理**：实现网络协议栈和网络设备支持

### 1.3 Linux 发行版分类
Linux 发行版是基于 Linux 内核的完整操作系统，通常包含桌面环境、应用程序和工具。常见的发行版包括：

| 类型 | 代表发行版 | 特点 |
|------|-----------|------|
| 企业级 | Red Hat Enterprise Linux (RHEL)、CentOS、SUSE Linux Enterprise | 稳定性高，支持周期长，适合服务器环境 |
| 桌面版 | Ubuntu、Fedora、Debian | 用户友好，软件丰富，适合个人使用 |
| 轻量级 | Alpine Linux、Arch Linux | 体积小，性能高，适合容器和嵌入式系统 |
| 安全导向 | Kali Linux、Tails | 内置安全工具，适合渗透测试和安全研究 |

### 1.4 Linux 文件系统结构
Linux 采用树形文件系统结构，所有文件和目录都从根目录 `/` 开始：

```
/               # 根目录
├── bin/        # 基本命令可执行文件
├── boot/       # 引导加载程序和内核文件
├── dev/        # 设备文件
├── etc/        # 系统配置文件
├── home/       # 用户主目录
├── lib/        # 共享库文件
├── media/      # 可移动媒体挂载点
├── mnt/        # 临时挂载点
├── opt/        # 可选应用程序
├── proc/       # 进程信息和系统状态
├── root/       # root 用户主目录
├── sbin/       # 系统管理命令
├── srv/        # 服务数据
├── sys/        # 系统硬件信息
├── tmp/        # 临时文件
├── usr/        # 用户程序和数据
└── var/        # 可变数据（日志、缓存等）
```

## 2. 核心技术

### 2.1 内核功能模块

#### 2.1.1 内存管理
Linux 内存管理负责：
- 物理内存分配和回收
- 虚拟内存管理
- 内存映射
- 页面置换
- 内存保护

#### 2.1.2 进程管理
Linux 进程管理包括：
- 进程创建和终止
- 进程调度
- 进程间通信（IPC）
- 进程状态管理

#### 2.1.3 硬件管理
通过设备驱动程序与硬件设备交互，支持：
- 字符设备
- 块设备
- 网络设备

#### 2.1.4 文件管理
提供统一的文件系统接口，支持多种文件系统：
- ext4、XFS、Btrfs 等本地文件系统
- NFS、SMB 等网络文件系统
- tmpfs、procfs 等虚拟文件系统

### 2.2 启动过程
Linux 系统启动过程：
1. **BIOS/UEFI 初始化**：硬件自检，加载引导设备
2. **引导加载程序**：GRUB2 加载内核和初始化 RAM 磁盘
3. **内核初始化**：检测硬件，加载驱动，挂载根文件系统
4. **初始化系统**：systemd 或 SysV init 启动系统服务
5. **用户登录**：显示登录界面，等待用户登录

### 2.3 虚拟内存管理

#### 2.3.1 分段与分页
- **分段**：将虚拟地址空间划分为逻辑段（代码段、数据段、堆栈段等）
- **分页**：将物理内存和虚拟内存划分为固定大小的页（通常为 4KB）

#### 2.3.2 页面置换算法
当内存不足时，Linux 使用以下算法选择要置换的页面：
- **LRU (Least Recently Used)**：最近最少使用
- **LFU (Least Frequently Used)**：最不经常使用
- **CLOCK 算法**：改进的 LRU 算法，减少开销
- **WSClock 算法**：考虑工作集的 CLOCK 算法

### 2.4 I/O 多路复用
Linux 提供多种 I/O 多路复用机制：
- **select**：最早的实现，支持有限的文件描述符
- **poll**：改进的 select，无文件描述符限制
- **epoll**：Linux 特有的高效实现，适用于高并发场景

### 2.5 sendfile 零拷贝技术
`sendfile` 系统调用实现零拷贝，减少数据在内核空间和用户空间之间的复制：
- 适用于文件服务器等需要高效传输大文件的场景
- 减少 CPU 开销和内存带宽使用

## 3. 常用命令

### 3.1 文件目录操作
```bash
# 切换目录
cd /path/to/directory

# 创建目录
mkdir -p directory/path

# 删除文件/目录
rm file.txt
rm -rf directory

# 复制文件/目录
cp source destination
cp -r source_directory destination_directory

# 移动文件/目录
mv source destination

# 查看文件内容
cat file.txt
less file.txt
more file.txt

# 查看文件头部/尾部
head -n 10 file.txt
tail -n 10 file.txt
tail -f file.txt  # 实时查看文件更新
```

### 3.2 权限管理
```bash
# 查看文件权限
ls -l file.txt

# 修改文件权限
chmod 755 file.txt  # rwxr-xr-x
chmod +x file.txt   # 添加执行权限

# 修改文件所有者
chown user:group file.txt

# 修改目录权限（递归）
chmod -R 755 directory
```

### 3.3 用户和组管理
```bash
# 创建用户
useradd username

# 删除用户
userdel username

# 修改用户密码
passwd username

# 创建组
groupadd groupname

# 将用户添加到组
usermod -aG groupname username

# 查看用户所属组
groups username
```

### 3.4 进程管理
```bash
# 查看进程
ps aux
ps -ef

# 查看进程树
pstree

# 实时查看进程
top
htop

# 终止进程
kill PID
kill -9 PID  # 强制终止

# 查找进程
pgrep process_name
```

### 3.5 网络命令
```bash
# 查看网络接口
ifconfig
ip addr

# 查看路由表
route -n
ip route

# 测试网络连接
ping hostname

# 查看网络连接
netstat -tuln
ss -tuln

# 网络诊断
traceroute hostname
nslookup hostname

# 监听端口
nc -l 8080
```

### 3.6 磁盘管理
```bash
# 查看磁盘使用情况
df -h

# 查看目录使用情况
du -sh directory

# 挂载文件系统
mount /dev/sdb1 /mnt

# 卸载文件系统
umount /mnt

# 查看磁盘分区
fdisk -l
```

### 3.7 压缩解压
```bash
# tar 压缩
tar -czvf archive.tar.gz directory

# tar 解压
tar -xzvf archive.tar.gz

# zip 压缩
zip -r archive.zip directory

# unzip 解压
unzip archive.zip
```

### 3.8 软件安装
```bash
# RPM 包管理（RHEL/CentOS）
rpm -ivh package.rpm    # 安装
rpm -e package          # 卸载
rpm -qa                 # 查看已安装包

# YUM 包管理（RHEL/CentOS）
yum install package     # 安装
yum remove package      # 卸载
yum update              # 更新
yum list                # 查看可用包

# APT 包管理（Ubuntu/Debian）
apt-get install package     # 安装
apt-get remove package      # 卸载
apt-get update              # 更新源
apt-get upgrade             # 更新包
```

## 4. 高阶用法

### 4.1 Shell 脚本编程
Shell 脚本是自动化 Linux 操作的强大工具：
```bash
#!/bin/bash

# 变量定义
NAME="Linux"

# 条件判断
if [ "$NAME" = "Linux" ]; then
    echo "Hello, $NAME!"
fi

# 循环
for i in {1..5}; do
    echo "Number: $i"
done

# 函数
function greet() {
    echo "Hello, $1!"
}

greet "World"
```

### 4.2 系统性能监控
```bash
# CPU 使用率
top
mpstat

# 内存使用情况
free -h
vmstat

# 磁盘 I/O
iostat
iotop

# 网络流量
iftop
tcpdump

# 系统负载
uptime
```

### 4.3 网络配置与管理
```bash
# 临时配置 IP 地址
ifconfig eth0 192.168.1.100 netmask 255.255.255.0

# 永久配置网络（RHEL/CentOS）
vi /etc/sysconfig/network-scripts/ifcfg-eth0

# 永久配置网络（Ubuntu）
vi /etc/netplan/00-installer-config.yaml

# 重启网络服务
systemctl restart network  # RHEL/CentOS
systemctl restart networking  # Ubuntu
```

### 4.4 安全加固
```bash
# 防火墙配置
firewall-cmd --zone=public --add-port=80/tcp --permanent  # RHEL/CentOS
ufw allow 80/tcp  # Ubuntu

# SSH 安全配置
vi /etc/ssh/sshd_config
# 禁用 root 登录
# PermitRootLogin no
# 更改默认端口
# Port 2222

# 安装入侵检测系统
yum install fail2ban  # RHEL/CentOS
apt-get install fail2ban  # Ubuntu
```

## 5. 面试题解析

### 5.1 常见 Linux 面试问题

#### 5.1.1 什么是 Linux 内核？它的主要功能是什么？
**答案**：Linux 内核是 Linux 操作系统的核心部分，负责管理系统的硬件资源和提供基本服务。主要功能包括内存管理、进程管理、硬件管理、文件管理和网络管理。

#### 5.1.2 硬链接和软链接的区别是什么？
**答案**：
- **硬链接**：
  - 是文件的另一个名称，指向同一个 inode
  - 不能跨文件系统创建
  - 删除原始文件后，硬链接仍然有效
  - 不能链接到目录

- **软链接**：
  - 是一个特殊的文件，包含指向目标文件的路径
  - 可以跨文件系统创建
  - 删除原始文件后，软链接失效
  - 可以链接到目录

#### 5.1.3 Linux 文件权限中的 rwx 分别代表什么？
**答案**：
r（读）：允许查看文件内容或列出目录中的文件
w（写）：允许修改文件内容或在目录中创建、删除文件
x（执行）：允许执行文件或进入目录

#### 5.1.4 什么是虚拟内存？它的作用是什么？
**答案**：虚拟内存是一种内存管理技术，它允许应用程序使用比实际物理内存更多的内存空间。当物理内存不足时，系统会将部分不常用的内存数据交换到磁盘上，从而为当前活动的进程释放物理内存。虚拟内存的作用是：
- 扩大可用内存空间
- 实现内存保护
- 简化内存管理

#### 5.1.5 解释 Linux 中的进程状态
**答案**：Linux 进程有以下几种状态：
- R（运行）：进程正在运行或在就绪队列中等待运行
- S（睡眠）：进程在等待事件完成
- D（不可中断睡眠）：进程在等待 I/O 操作完成，不能被信号中断
- Z（僵尸）：进程已终止，但父进程尚未回收其资源
- T（停止）：进程被信号停止或被跟踪

#### 5.1.6 什么是 I/O 多路复用？Linux 中有哪些实现？
**答案**：I/O 多路复用是一种技术，允许一个进程同时监控多个文件描述符，当其中任何一个文件描述符就绪时，进程可以进行相应的操作。Linux 中的实现包括：
- select：最早的实现，支持有限的文件描述符
- poll：改进的 select，无文件描述符限制
- epoll：Linux 特有的高效实现，适用于高并发场景

#### 5.1.7 如何查看 Linux 系统的负载？
**答案**：可以使用以下命令查看系统负载：
- `uptime`：显示系统运行时间和平均负载
- `top`：实时显示系统负载和进程状态
- `vmstat`：显示虚拟内存统计信息和系统负载

#### 5.1.8 什么是零拷贝技术？Linux 中如何实现？
**答案**：零拷贝技术是一种减少数据在内核空间和用户空间之间复制的技术，提高数据传输效率。Linux 中通过 `sendfile` 系统调用实现零拷贝，适用于文件服务器等需要高效传输大文件的场景。

## 6. 参考链接

### 6.1 官方文档
- [Linux Kernel Documentation](https://www.kernel.org/doc/html/latest/)
- [Red Hat Enterprise Linux Documentation](https://access.redhat.com/documentation/en-us/red_hat_enterprise_linux/)
- [Ubuntu Documentation](https://help.ubuntu.com/)

### 6.2 教程资源
- [Linux Command](https://linuxcommand.org/)
- [The Linux Documentation Project](https://www.tldp.org/)
- [Linux Journey](https://linuxjourney.com/)

### 6.3 社区论坛
- [Stack Overflow](https://stackoverflow.com/questions/tagged/linux)
- [Linux Questions](https://www.linuxquestions.org/)
- [Ubuntu Forums](https://ubuntuforums.org/)

### 6.4 进阶学习
- [Linux Inside](https://0xax.gitbooks.io/linux-insides/)
- [Advanced Programming in the UNIX Environment](https://man7.org/tlpi/)
- [The Linux Programming Interface](https://man7.org/tlpi/)