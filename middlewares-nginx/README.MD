# NGINX
## Nginx的架构体系
![nginx请求流程](./files/请求流程.PNG)
　
```text
    nginx使用一个master,协调多个worker进程的方式进行服务
```

## Nginx配置详解
```shell script
# 处理进程数
worker_processes  1;
events {
    #一个进程能处理的链接数
    worker_connections  1024;
}


http {
    # 将另外的配置文件引到当前的配置文件中
    include       mime.types;
    # 默认使用数据流传输数据
    default_type  application/octet-stream;
    #标识打开零拷贝
    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;
    # 虚拟主机
    server {
        # 监听的端口号
        listen       80;
        # 主机名 ，域名
        server_name  localhost;
        # uri,例如http://www.baidu.com/xx/xxx/aa.txt,是http://www.baidu.com后面的资源位置
        location / {
            root   html;
            index  index.html index.htm;
        }
        # 错误码
        error_page   500 502 503 504  /50x.html;
        # 如果访问/50x.html就会跳转到 html
        location = /50x.html {
            root   html;
        }

    }
}

```

### 负载均衡的策略
```text
1. ip_hash:根据客户端的ip地址转发到同一台服务器，可以保持会话，但是不适合移动互联网，因为在网络动态变化的状态下，
ip是时常变化的，无法正确的保持会话
2. least_conn:最少联机的的机器，如果使用权重的方式，那么权重低的自然连接少，就无法实现按比分配
3. url_hash:根据用户访问的url定向转发请求,可以实现流量的定向，但是无法实现用户定向
4. fair:根据服务器响应实践转发请求，如果是网络延迟导致的时间响应慢，就会判断错误

```