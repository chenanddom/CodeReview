# Redis命令

* exists key 查看key是否存在

* type key 查看key的类型

* incr/decr key 秒杀使用的增一和减一

* dbsize 当前库的key的大小

* flushdb 清除当前数据库

* flushall 清除redis所有库

* append k1 v11 将v11追加到键为k1的值后面，如果k1的原值为v1，那么，执行完该命令后就是v1v11.

* setnx k1 v1 添加k1=v1，如果k1已经存在就无法添加

* mset k1 v1 k2 v2 k3 v3 批次的添加键值

* mget 对应上面的mget k1  k2 k3 批次的获取键对应的值

* msetnx k1 v1 k2 v2 k3 v3 原子性的批次设置键值

* getrange 获取key对应范围的值
```shell script
127.0.0.1:6379> set name chendom
OK
127.0.0.1:6379> getrange name 0 3
"chen"
127.0.0.1:6379> 
```
* setrange name 3 and
```shell script
127.0.0.1:6379> setrange name 3 and
(integer) 7
127.0.0.1:6379> get name
"cheandm"
127.0.0.1:6379>
```
* getset k1 以新值换取旧值
```shell script
127.0.0.1:6379> setrange name 3 and
(integer) 7
127.0.0.1:6379> get name
"cheandm"
127.0.0.1:6379>
```

* 列表的相关的操作
    * lpush k1 v1在键k1的队列左侧添加值v1
    * lpop k1 count 在键k1的队列上弹出count个元素
    * rpush k1 v1 在键k1的列表右边添加一个元素
    * rpop k1 count在k1的列表右边弹出count个元素
    * rpoplpush k1 k2 在键k1的队列弹出一个元素添加到键为k2的队列的左侧。    