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
* 防盗链
```shell script
location ~*/(js|img|files) {
        # 校验是否带referer头，如果没有就无法访问
		valid_referers none 47.105.103.82;
		if ($invalid_referer){
		return 403;
		}
		root html;
		}
		}	
```
验证防盗链可以使用curl来验证
```shell script
# 加上-I就可以实现查看请求头的信息
curl -I http://xx.xx.xx/img/docker.PNG
# -e referer消息
curl -e "http://www.baidu.com"  http://xx.xx.xx/img/docker.PNG
```

* keepalive
对客户端进行配置的keepalive参数

```shell script
http {
    include       mime.types;
    default_type  application/octet-stream;

    #keepalive_timeout  0;
	# 表示建立一个连接，超过65s就会超时，设置为０就让keepalive失效
    keepalive_timeout  65 65;
	#
	keepalive_time 1h; # 表示建立TCP连接，如果超过1个小时没有任何活动就关闭连接
	#默认60，系统中弱有耗时操作，如果超过了send_timeout,就强制断开
	#send_timeout 60;
	# 一个TCP复用中可以支持并发接受的请求个数，1000就比较够用了，太多太少都不好
	keepalive_requests 1000;
```

对上游服务器配置的keepalive的参数
向上游服务器保留链接数
keepalive 100;
连接保留时间
keepalive_timeout;
一个tcp复用可以并发接收的请求个数
keepalive_requests
server中的配置
proxy_http_version 1.1;#配置http的版本号，默认使用http1.0协议，需要在request中添加"Connection:keepalive" header才能够支持，而http1.1默认支持开启keepalive
proxy_set_header Connection "";# 清除close信息
```shell script
upstream javaServers{
        # 负载均衡策略要放在第一行
		hash $request_uri;
		#设置和上游服务器的连接
		keepalive 100;
		#保留服务器连接的时间
		keepalive_timeout 65;
		# 一个连接可以接收的请求的个数
		keepalive_requests 1000;

	   server 172.20.0.1:10001;
	   server 172.20.0.1:10002;
	   
	}	
 location ^~/nginx {
		# 配置http的版本号，默认使用http1.0协议，需要在request中添加"Connection:keepalive" header才能够支持，而http1.1默认支持开启keepalive
		proxy_http_version 1.1;
		## 清除close信息
		proxy_set_header Connection "";
		default_type application/json;
		# break 标识只匹配带第一条就返回，不会继续往下执行匹配
		rewrite  ^/nginx/([0-9]+).html$  /nginx/get  redirect;
	 	proxy_pass http://javaServers;
			#proxy_pass http://qq.com;
		
        }
```