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
配置：
```shell script

#user  nobody;
worker_processes  1;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;
	upstream javaServers{
		# down代表服务已经下线，不在服务的列表内，weight代表的是结点的服务的权重，backup代表的是服务的副本备用
	   #server 172.20.0.1:10001 weight=8 down;
	   server 172.20.0.1:10001 weight=8 down;
	   server 172.20.0.1:10002 weight=2 backup;
	}	
       
    server {
        listen       80;
        server_name  localhost;

	 location / {
	 	proxy_pass http://javaServers;
			#proxy_pass http://qq.com;
		
            #root   html;
            #index  index.html index.htm;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

    }

}

```

* url 重写
rewrite是实现URL重写的关键指令，根据regex(正则表达式)部分内容，重定型到replacement，结尾是flag标记
格式: rewrite <regex> <replacement> [flag]
       关键字   正则     替代的内容     flag标记
       
flag标记:
    1. last 本条规则完成后，继续向下匹配新的location uri规则
    2. break 本条规则匹配完成即终止，不再匹配后面的任何规则
    3. redirect 返回302临时重定向，浏览器会显示跳转后的URL地址
    4. permanent 返回301永久重定向，浏览器会显示跳转后的URL地址       
    
rewrite url的语法
```shell script
# 重启防火墙
systemctl restart firewalld
# 重载规则
firewall-cm --reload
# 查看规则
firewalld-cmd --list-all
# 指定端口和ip访问
firewalld-cmd --permanent --add-rich-rule="rule family=ipv4" source address="192.168.0.1" port protocol="tcp"  port="8088"  accept
# 移除规则
firewalld-cmd --permanent --remove-rich-rule="rule family=ipv4" source address="192.168.0.1" port protocol="tcp" port="tcp" accept
```    
示例:要实现访问http://xx.xx.xx.xx/nginx/1.html就转到http://xx.xx.xx.xx/nginx/get的方法上
```shell script
 location ^~/nginx {
		default_type application/json;
		# break 标识只匹配带第一条就返回，不会继续往下执行匹配
		rewrite  ^/nginx/([0-9]+).html$  /nginx/get  redirect;
		#last 标识执行完当前指令后会继续往下执行下面的匹配
		#rewrite  ^/nginx/([0-9]+).html$  /nginx/get  last;
		# 302 临时重定向
		#rewrite  ^/nginx/([0-9]+).html$  /nginx/get  redirect;
		# 301永久重定向
		#rewrite  ^/nginx/([0-9]+).html$  /nginx/get  permanent;
	 	proxy_pass http://javaServers;
			#proxy_pass http://qq.com;
		
            #root   html;
            #index  index.html index.htm;
        }
	location ~*/(js|img|files) {
			root html;
		}	
```