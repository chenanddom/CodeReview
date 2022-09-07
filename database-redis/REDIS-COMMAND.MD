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
    * lrem k1 count "value" 删除k1键下面的count个value值。
    * lset k1 index value 将k1键下的列表的index角标的值修改为value。
    
    
    
* set集合
    * sadd k1 v1 v2 v3 向k1键添加v1 v2 v3值
    * smove k1 k2 "v"将k1键里面的v值转移到k2
    * smembers k1 将k1键全部获取出来
    * sinter k1 k2 为键k1和键k2取交集
    * sunion k1 k2 为键k1和键k2取并集
    * sdiff k1 k2 为键k1和键k2取差集，取存在k1但是，不存在k2的元素
            


* hash存储
    * hset k1 f1 v1: 添加键k1的字段f1 对应的值v1
    * hmset k1 f1 v1 f2 v2 f3 v3:添加键k1对应的字段f1 f2 f3对应的值v1 v2 v3
    * hget k1 f1:获取键k1的域f1的值 
    * hgetall k1:获取键k1所有的键值
    * hexists k1 f1 判断键k1是否存在域f1
    * hkeys k1 获取键所有的域
    * hvals k1 获取键k1的所有的值
    * hincrby k1 f1:为键k1发域f1自增
    * hsetnx k1 f1 v1:原子性的为k1键添加f1-v1        
            
            
            
            
* zset有序集合
    * zadd k1 score1 value1 score2 value2:在键k1添加value1,value2以及值对应的分数 
    * zrange k1 0 -1 withscores:查询有序集合内所有的元素，带分数返回，带分数返回可选
    * zrangebysocre k1 minscore maxscore:根据分数返回集合k1对应的元素
    * zrevrangbyscore k1 maxscore minscore：根据分数返回集合k1对应的元素逆序返回
    * zrem k1 v1:删除有序集合内的元素v1
    * zinrby k1 score v1:为有序集合里面的元素v1加分score
    * zcount k1 minscore maxscore :统计k1集合从最大分数到最小分数的元素个数    
    * zrank k1 v1:获取元素v1在有序集合的排名
    
    
* 发布订阅
    * publish channel "message content" 发布内容
    * subscribe channel :订阅主题
         
            
                        
            
            
            