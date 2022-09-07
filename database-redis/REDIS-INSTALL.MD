# Redis安装与配置


### 配置好编译环境

需要安装C/C++编译环境
```shell script
yum -y install gcc-c++
```

### 下载源码 
[下载redis-6.2.1](https://github.com/redis/redis/archive/refs/tags/6.2.1.tar.gz)
```shell script
#编译，
make 
# 安装
make install 
# 如果安装的过程中出现了如下的错误 ,只需要执行make distclean命令再次编译即可
make[1]: [persist-settings] Error 2 (ignored)
    CC adlist.o
In file included from adlist.c:34:0:
zmalloc.h:50:31: fatal error: jemalloc/jemalloc.h: No such file or directory
 #include <jemalloc/jemalloc.h>
```

### 修改redis.conf
```shell script
# 后端启动
daemonize yes 
```
* 启动
/usr/local/redis-server /etc/redis.conf

![启动](./files/redis-install.PNG)


## Redis配置


### redis的内存单位

redis的内存单位只支持bytes，不支持bit，大小写敏感
```text
# Note on units: when memory size is needed, it is possible to specify
# it in the usual form of 1k 5GB 4M and so forth:
#
# 1k => 1000 bytes
# 1kb => 1024 bytes
# 1m => 1000000 bytes
# 1mb => 1024*1024 bytes
# 1g => 1000000000 bytes
# 1gb => 1024*1024*1024 bytes
#

```
### 设置redis允许远程访问
如果要设置远程访问就需要注释掉下面这行，然后把保护模式也去掉
bind 127.0.0.1 -::1
去掉保护模式就是将
protected-mode yes
改成protected-mode no


### tcp-backlog 511访问连接
设置tcp的backlog，backlog其实是一个连接队列，backlog队列总和=未完成三次握手队列+已经完成三次握手队列。
在高并发的环境下，需要设置一个高backlog值来避免满客户端连接问题。但是此时Linux内核的参数/proc/sys/net/core/somaxconn
和/proc/sys/net/ipv4/tcp_max_syn_backlog都是128，为了达到效果还需要将他们的参数设置大

### timeout 0
标识对客户端的连接标识超时，如果值为0就是永不超时，如果设置为指定的非0非负数字，就会在连接空闲达到指定的时间超时
### tcp-keepalive 300
每个300秒检测以下连接是否还存活，如果还或者，那么就继续保持服务，否则就释放连接






