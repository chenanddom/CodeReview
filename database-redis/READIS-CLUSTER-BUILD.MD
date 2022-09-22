# Redis集群搭建

* 创建配置文件redis6379.conf、redis6380.conf、redis6381.conf、redis6389.conf、redis6390.conf、redis6391.conf.
```shell script
# 引入公共的配置文件
include /usr/local/redis_cluster/redis.conf
# pid
pidfile "/var/run/redis_6379.pid"
port 6379
# rdb持久化的为念
dbfilename "dump6379.rdb"
# 密码认证
masterauth "root@123"
#开启集群配置
cluster-enabled yes
# 节点配置的文件(不需要手动改，集群节点自动维护)
cluster-config-file node-6379.conf
# 集群节点的超时时间
cluster-node-timeout 15000 
```

* 分别使用上面的配置文件启动6个服务实例
```shell script
./bin/redis-server redis6379.conf
./bin/redis-server redis6380.conf
./bin/redis-server redis6381.conf
./bin/redis-server redis6389.conf
./bin/redis-server redis6390.conf
./bin/redis-server redis6391.conf
```


* 将以上的6个服务实例组成一个集群
这一步需要在redis源码的编译的目录里面使用redis-cli客户端
使用以下的命令就可以搭建好集群
```shell script
[root@redis-node1 src]# redis-cli --cluster create --cluster-replicas 1 127.0.0.1:6379 127.0.0.1:6380 127.0.0.1:6381 127.0.0.1:6389 127.0.0.1:6390 127.0.0.1:6391 -a root@123
Warning: Using a password with '-a' or '-u' option on the command line interface may not be safe.
>>> Performing hash slots allocation on 6 nodes...
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
Adding replica 127.0.0.1:6390 to 127.0.0.1:6379
Adding replica 127.0.0.1:6391 to 127.0.0.1:6380
Adding replica 127.0.0.1:6389 to 127.0.0.1:6381
>>> Trying to optimize slaves allocation for anti-affinity
[WARNING] Some slaves are in the same host as their master
M: 7a4265ab3bb5d544acee33c60bc2af18df025e81 127.0.0.1:6379
   slots:[0-5460] (5461 slots) master
M: 2c999799dfc983d74cc40a986fee9a11c3d68311 127.0.0.1:6380
   slots:[5461-10922] (5462 slots) master
M: 6edc34584f11f64b138dca2832ef2c542d4eed5d 127.0.0.1:6381
   slots:[10923-16383] (5461 slots) master
S: 1c6f5d2e10641ac9631f84a2fabe1f02ee1a08e7 127.0.0.1:6389
   replicates 7a4265ab3bb5d544acee33c60bc2af18df025e81
S: 7191f840034f8511d6c4e500ccc34353cb1cae38 127.0.0.1:6390
   replicates 2c999799dfc983d74cc40a986fee9a11c3d68311
S: 4f634eb9a3ff8d7da41470f6775bcf76395b604b 127.0.0.1:6391
   replicates 6edc34584f11f64b138dca2832ef2c542d4eed5d
# 此处是确认是否使用上面提示的方案分配集群的slot插槽.
Can I set the above configuration? (type 'yes' to accept): yes
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join

>>> Performing Cluster Check (using node 127.0.0.1:6379)
M: 7a4265ab3bb5d544acee33c60bc2af18df025e81 127.0.0.1:6379
   slots:[0-5460] (5461 slots) master
   1 additional replica(s)
S: 4f634eb9a3ff8d7da41470f6775bcf76395b604b 127.0.0.1:6391
   slots: (0 slots) slave
   replicates 6edc34584f11f64b138dca2832ef2c542d4eed5d
S: 1c6f5d2e10641ac9631f84a2fabe1f02ee1a08e7 127.0.0.1:6389
   slots: (0 slots) slave
   replicates 7a4265ab3bb5d544acee33c60bc2af18df025e81
S: 7191f840034f8511d6c4e500ccc34353cb1cae38 127.0.0.1:6390
   slots: (0 slots) slave
   replicates 2c999799dfc983d74cc40a986fee9a11c3d68311
M: 6edc34584f11f64b138dca2832ef2c542d4eed5d 127.0.0.1:6381
   slots:[10923-16383] (5461 slots) master
   1 additional replica(s)
M: 2c999799dfc983d74cc40a986fee9a11c3d68311 127.0.0.1:6380
   slots:[5461-10922] (5462 slots) master
   1 additional replica(s)
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```
到此，redis的集群已经搭建完成。
![redis集群](./file/redis-cluster-1.PNG)



* 什么是 slots?

一个 Redis 集群包含 16384 个插槽（hash slot），数据库中的每个键都属于这 16384 个插槽的其中一个。集群使用公式 CRC16 (key) % 16384 来计算键 key 属于哪个槽， 其中 CRC16 (key) 语句用于计算键 key 的 CRC16 校验和 。
集群中的每个节点负责处理一部分插槽。 举个例子， 如果一个集群可以有主节点， 其中：
节点 A 负责处理 0 号至 5460 号插槽。
节点 B 负责处理 5461 号至 10922 号插槽。
节点 C 负责处理 10923 号至 16383 号插槽。


* 多值插入需要指定组
```shell script
127.0.0.1:6381> mset name{user} zhangsan age{user} 20
-> Redirected to slot [5474] located at 127.0.0.1:6380
OK

```

* 计算key在对应的插槽
```shell script
#计算name{user}的插槽值
127.0.0.1:6380> cluster keyslot name{user}
(integer) 5474
127.0.0.1:6380> cluster keyslot k2
(integer) 449

```
* 根据插槽计算插槽内的数据数量
注意，只能计算本节点的插槽的数据个数
```shell script
# 注意，只能计算本节点的插槽的数据个数
127.0.0.1:6380> cluster countkeysinslot 449
(integer) 0
127.0.0.1:6380> cluster countkeysinslot 5474
(integer) 2
# 在对应的插槽就可以计算出来对应插槽的key的数量
127.0.0.1:6379> cluster countkeysinslot 449
(integer) 1
```
* 获取对应插槽的key
```shell script
127.0.0.1:6379> cluster getkeysinslot 449 1
1) "k2"
```

## 验证宕机后
例子：
假如6381宕机
```shell script
127.0.0.1:6381> cluster nodes
8fad167225a3b36da94b626f3a5ed36b4897c45a 127.0.0.1:6389@16389 slave 0ca20f4a1b58ecac245777a99fd54f50003a80c6 0 1663822514292 8 connected
0c40f8f4acdc83b493fa836e9bb24e5c1730c6a0 127.0.0.1:6380@16380 slave c578ee731ff793a71e5a971d803dd81d7a23aafe 0 1663822514000 10 connected
c578ee731ff793a71e5a971d803dd81d7a23aafe 127.0.0.1:6390@16390 master - 0 1663822515306 10 connected 5461-10922
0ca20f4a1b58ecac245777a99fd54f50003a80c6 127.0.0.1:6379@16379 master - 0 1663822514000 8 connected 0-5460
e39465474c93ac7701ef4cf9d3afabce484c8f0b 127.0.0.1:6391@16391 slave 58e4abe3114f474f90b28df2b9fbe58aae29c5ce 0 1663822516321 3 connected
58e4abe3114f474f90b28df2b9fbe58aae29c5ce 127.0.0.1:6381@16381 myself,master - 0 1663822515000 3 connected 10923-16383
127.0.0.1:6381> shutdown
```
6379服务查看集群节点的情况
```shell script
127.0.0.1:6379> cluster nodes
c578ee731ff793a71e5a971d803dd81d7a23aafe 127.0.0.1:6390@16390 master - 0 1663822659645 10 connected 5461-10922
e39465474c93ac7701ef4cf9d3afabce484c8f0b 127.0.0.1:6391@16391 master - 0 1663822659000 11 connected 10923-16383
0c40f8f4acdc83b493fa836e9bb24e5c1730c6a0 127.0.0.1:6380@16380 slave c578ee731ff793a71e5a971d803dd81d7a23aafe 0 1663822661679 10 connected
0ca20f4a1b58ecac245777a99fd54f50003a80c6 127.0.0.1:6379@16379 myself,master - 0 1663822660000 8 connected 0-5460
58e4abe3114f474f90b28df2b9fbe58aae29c5ce 127.0.0.1:6381@16381 master,fail - 1663822642380 1663822638000 3 disconnected
8fad167225a3b36da94b626f3a5ed36b4897c45a 127.0.0.1:6389@16389 slave 0ca20f4a1b58ecac245777a99fd54f50003a80c6 0 1663822660669 8 connected
```
重启6381的服务
```shell script
[root@redis-node1 redis_cluster]# /usr/local/bin/redis-server redis6381.conf 
```

再次查看6381的情况：
```shell script
127.0.0.1:6379> cluster nodes
c578ee731ff793a71e5a971d803dd81d7a23aafe 127.0.0.1:6390@16390 master - 0 1663822666000 10 connected 5461-10922
e39465474c93ac7701ef4cf9d3afabce484c8f0b 127.0.0.1:6391@16391 master - 0 1663822667781 11 connected 10923-16383
0c40f8f4acdc83b493fa836e9bb24e5c1730c6a0 127.0.0.1:6380@16380 slave c578ee731ff793a71e5a971d803dd81d7a23aafe 0 1663822668794 10 connected
0ca20f4a1b58ecac245777a99fd54f50003a80c6 127.0.0.1:6379@16379 myself,master - 0 1663822667000 8 connected 0-5460
58e4abe3114f474f90b28df2b9fbe58aae29c5ce 127.0.0.1:6381@16381 slave e39465474c93ac7701ef4cf9d3afabce484c8f0b 0 1663822666564 11 connected
8fad167225a3b36da94b626f3a5ed36b4897c45a 127.0.0.1:6389@16389 slave 0ca20f4a1b58ecac245777a99fd54f50003a80c6 0 1663822667000 8 connected

```
从上面的例子我们可以知道6381一开始是master节点，down掉之后6391上升为master,重启6381就变成了slave节点。

注意需要在配置的目录启动redis节点，否则无法正常获取到node.conf配置文件，无法正常加入集群



