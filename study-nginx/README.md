# Study-nginx #




## 参考链接 ##
- 官网文档 http://nginx.org/en/docs/
- docker hub 下载 https://hub.docker.com/_/nginx
- Loadbalance http://nginx.org/en/docs/http/load_balancing.html
- 负载均衡模块 http://nginx.org/en/docs/http/ngx_http_upstream_module.html
- URL 重写 http://nginx.org/en/docs/http/converting_rewrite_rules.html
- https://www.bilibili.com/video/BV1em4y1A7ey/?spm_id_from=333.1007.tianma.9-2-38.click&vd_source=72424c3da68577f00ea40a9e4f9001a1

文档书籍 https://tengine.taobao.org/book/index.html


## Nginx 

To start nginx, run the executable file. Once nginx is started, it can be controlled by invoking the executable with the `-s` parameter. Use the following syntax:

> ```
> nginx -s signal
> ```

Where *signal* may be one of the following:

- `stop` — fast shutdown
- `quit` — graceful shutdown
- `reload` — reloading the configuration file
- `reopen` — reopening the log files



Example

> ```
> nginx -s reload
> ```



For getting the list of all running nginx processes

> ```
> ps -ax | grep nginx
> ```



## Docker 



```
docker pull nginx
```



```console
docker run --name bage-nginx -d -p 8080:80 nginx
```



## Conf

server





http://nginx.org/en/docs/http/load_balancing.html

weight

```
http {
    upstream myapp1 {
        server srv1.example.com weight=3;
        server srv2.example.com;
        server srv3.example.com;
    }

    server {
        listen 80;

        location / {
            proxy_pass http://myapp1;
        }
    }
}
```



## Converting rewrite rules

```
RewriteCond  %{HTTP_HOST}  example.org
RewriteRule  (.*)          http://www.example.org$1
```

大纲

1、 反向代理

2、 配置文件

3、 为何？Nginx 架构

