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
    x或X // 删除一个字符，x删除光标后的，而X删除光标前的； 
    D：删除从当前光标到光标所在行尾的全部字符； 
    dd：删除光标行正行内容； 
    ndd：删除当前行及其后n-1行； 
    nyy：将当前行及其下n行的内容保存到寄存器？中，其中？为一个字母，n为一个数字； 
    p：粘贴文本操作，用于将缓存区的内容粘贴到当前光标所在位置的下方； 
    P：粘贴文本操作，用于将缓存区的内容粘贴到当前光标所在位置的上方； 
    /字符串：文本查找操作，用于从当前光标所在位置开始向文件尾部查找指定字符串的内容，查找的字符串会被加亮显示； 
    ？name：文本查找操作，用于从当前光标所在位置开始向文件头部查找指定字符串的内容，查找的字符串会被加亮显示； 
    a，bs/F/T：替换文本操作，用于在第a行到第b行之间，将F字符串换成T字符串。其中，“s/”表示进行替换操作；
    







监听 1234端口

    nc -l 1234




请求 本地的 1234 端口

    nc localhost 1234 

