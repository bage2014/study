# study-linux-nginx #
Nginx学习笔记

## 参考链接 ##

- Nginx中文文档 [http://www.nginx.cn/doc/](http://www.nginx.cn/doc/ "Nginx中文文档")
- Nginx 相关介绍(Nginx是什么?能干嘛?) [https://www.cnblogs.com/wcwnina/p/8728391.html](https://www.cnblogs.com/wcwnina/p/8728391.html "Nginx 相关介绍(Nginx是什么?能干嘛?)")

## 功能概述 ##
- Nginx中文文档 [http://www.nginx.cn/doc/](http://www.nginx.cn/doc/)

## 优势特性 ##
- Nginx的优点 [https://blog.csdn.net/nangeali/article/details/60143560](https://blog.csdn.net/nangeali/article/details/60143560)
- 作为 Web 服务器：相比 Apache，Nginx 使用**更少的资源**，支持**更多的并发**连接，体现**更高的效率**，这点使 Nginx 尤其受到虚拟主机提供商的欢迎。能够支持高达 50,000 个并发连接数的响应，感谢 Nginx 为我们选择了 epoll and kqueue 作为开发模型.
- 作为**负载均衡**服务器：Nginx 既可以在内部直接支持 Rails 和 PHP，也可以支持作为 HTTP代理服务器 对外进行服务。Nginx 用 C 编写, 不论是系统资源开销还是 CPU 使用效率都比 Perlbal 要好的多。
- 作为邮件代理服务器: Nginx 同时也是一个非常优秀的邮件代理服务器（最早开发这个产品的目的之一也是作为邮件代理服务器），Last.fm 描述了成功并且美妙的使用经验。
- Nginx 安装非常的简单，**配置文件 非常简洁**（还能够支持perl语法），Bugs非常少的服务器: Nginx 启动特别容易，并且几乎可以做到7*24不间断运行，即使运行数个月也不需要重新启动。你还能够在 不间断服务的情况下进行软件版本的升级。
- **成本低廉**：Nginx为开源软件，采用的是2-clause BSD-like协议，可以免费试用，并且可用于商业用途
- **支持Rewrite重写**：能够根据域名、URL的不同，将http请求分到不同的后端服务器群组。
- **支持GZIP压缩**: 可以添加浏览器本地缓存的Header头。
- **支持热部署**: Nginx支持热部署，它的自动特别容易，并且，几乎可以7天*24小时不间断的运行，即使，运行数个月也不需要重新启动，还能够在不间断服务的情况下，对软件版本进行升级。

## 调试 nginx ##
Nginx的一个 杀手级特性 就是你能使用 debug_connection 指令只调试 某些 连接。

这个设置只有是你使用 --with-debug 编译的nginx才有效。