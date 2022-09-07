# Redis支持的数据类型
```text
String是Redis最基本的类型，你可以理解成与Memcached一模一样的类型，一个key对应一个value
String类型是二进制安全的。意味着Redis的string可以包含任何数据类型。比如jpg图片或者序列化对象
String类型是Redis最基本的数据类型，一个Redis种字符串value最多可以是512M

```

## 字符串
```shell script
String的数据结构为简单动态字符串(Simple Dynamic String,缩写SDS)。是可以修改的字符串，内部结构实现上类似与Java的ArrayList，
采用了冗余空间的方式来减少内存的饿频繁分配。当总覅创建长度小于1M的时候，扩容都是加倍现有的空间，如果超过1M，扩容每次都只是多扩容1M的
空间。需要注意的是字符串最大长度是512M.

```

## Redis列表(List)
单键多值
Redis列表是简单的字符串列表，按照插入顺序排序。可以添加一个元素到列表的头部(左边)或者尾部(右边)。
他的底层实际是一个双向链表，对两端的操作性能很高，通过索引下表的操作中间的结点性能会比较差。
命令
```shell script
# 在队列的左侧添加给键k1添加值v1
127.0.0.1:6379> lpush k1 v1
(integer) 1
# 在队列的左侧添加给键k1添加值v2
127.0.0.1:6379> lpush k1 v2
(integer) 2
# 在队列的左侧添加给键k1添加值v3
127.0.0.1:6379> lpush k1 v3
(integer) 3
# 在队列的左侧添加给键k1添加值v4
127.0.0.1:6379> lpush k1 v4
(integer) 4
# 获取所有的值
127.0.0.1:6379> lrange k1 0 -1
1) "v4"
2) "v3"
3) "v2"
4) "v1"
# 在列表的右侧给k2键添加值v1
127.0.0.1:6379> rpush k2 v1
(integer) 1
# 在列表的右侧给k2键添加值v2
127.0.0.1:6379> rpush k2 v2
(integer) 2
# 在列表的右侧给k2键添加值v3
127.0.0.1:6379> rpush k2 v3
(integer) 3
# 在列表的右侧给k2键添加值v4
127.0.0.1:6379> rpush k2 v4
(integer) 4
# 在列表的右侧给k2键添加值v5
127.0.0.1:6379> rpush k2 v5
(integer) 5
# 获取所有的元素
127.0.0.1:6379> lrange k2 0 -1
1) "v1"
2) "v2"
3) "v3"
4) "v4"
5) "v5"
# 在键k1的队列右侧弹出1个元素
127.0.0.1:6379> rpop k1 1
1) "v1"
# 在键k1的队列右侧弹出2个元素
127.0.0.1:6379> rpop k1 2
1) "v2"
2) "v3"
127.0.0.1:6379> lpush k1 v111
(integer) 2
127.0.0.1:6379> lrange k1 0 -1
1) "v111"
2) "v4"
# 从键k1的队列弹出一个元素压入键k2队列的左边
127.0.0.1:6379> rpoplpush k1 k2
"v4"
127.0.0.1:6379> lrange k1 0 -1
1) "v111"
127.0.0.1:6379> lrange k2 0 -1
1) "v4"
2) "v1"
3) "v2"
4) "v3"
5) "v4"
6) "v5"
# 获取列表的长度
127.0.0.1:6379> llen k1
(integer) 1
127.0.0.1:6379> llen k2
(integer) 6
# 在v5前面插入一个值vv5
127.0.0.1:6379> linsert k2 before "v5" "vv5"
(integer) 7
127.0.0.1:6379> lrange k2 0 -1
1) "v4"
2) "v1"
3) "v2"
4) "v3"
5) "v4"
6) "vv5"
7) "v5"
#在队列左侧删除一个 "vv5"的值
127.0.0.1:6379> lrem k2 1 "vv5"
(integer) 1
# 
127.0.0.1:6379> lset k2 0 v0
OK
127.0.0.1:6379> lrange k2 0 -1
1) "v0"
2) "v1"
3) "v2"
4) "v3"
5) "v4"
6) "v5"

```

注意:
lpop/rpop如果值还没有取完，键还会存在，如果值取完了，键就消失了。
[Redis列表的数据结构](https://app.diagrams.net/#Hchenanddom%2FCharts%2Fmain%2Frepo%2Fredis%E5%88%97%E8%A1%A8%E7%9A%84%E7%BB%93%E6%9E%84.drawio)

Redis的列表List，如果在数据比少的时候，就会分配一块内存存储列表数据，这块内存就叫zipList(压缩列表)，如果数据比较大，占据的内存比较大，
那么就需要开辟多个内存，并且将这些内存使用列表连接起来。这个链表就叫QuickList


## set集合
```shell script
127.0.0.1:6379> sadd k1 v1 v2 v3 v4
(integer) 4
# 获取set集合内的所有值
127.0.0.1:6379> smembers k1
1) "v2"
2) "v4"
3) "v1"
4) "v3"
# 判断集合内是否存在某个值
127.0.0.1:6379> sismember k1 v1
(integer) 1
127.0.0.1:6379> sismember k1 v5
(integer) 0
#返回集合的元素的个数
127.0.0.1:6379> scard k1
(integer) 4
#删除集合种的对应的元素
127.0.0.1:6379> srem k1 v2 v3
(integer) 2
127.0.0.1:6379> smembers k1
1) "v4"
2) "v1"
# 随机的从集合种吐出一个元素
127.0.0.1:6379> spop k1
"v4"
#从集合随机的获取两个元素,不删除
127.0.0.1:6379> srandmember k1 2
1) "v5"
2) "v1"
127.0.0.1:6379> smembers k1
1) "v7"
2) "v6"
3) "v2"
4) "v5"
5) "v1"
6) "v4"
7) "v3"
---------------------------------------------
127.0.0.1:6379> flushdb
OK
# 添加集合
127.0.0.1:6379> sadd k1 v1 v3 v5
(integer) 3
127.0.0.1:6379> sadd k2 v2 v4
(integer) 2
# 将k1集合的v5元素挪到k2集合
127.0.0.1:6379> smove k1 k2 v5
(integer) 1
127.0.0.1:6379> smembers k1
1) "v1"
2) "v3"
127.0.0.1:6379> smembers k2
1) "v5"
2) "v2"
3) "v4"
-----------------------------------------------
127.0.0.1:6379> smembers k1
1) "v5"
2) "v1"
3) "v3"
127.0.0.1:6379> smembers k2
1) "v5"
2) "v2"
3) "v4"
127.0.0.1:6379> sadd k1 v5
(integer) 1
# 取交集
127.0.0.1:6379> sinter k1 k2
1) "v5"
-----------------------------------------------
#取并集
127.0.0.1:6379> smembers k1
1) "v5"
2) "v1"
3) "v3"
127.0.0.1:6379> smembers k2
1) "v5"
2) "v2"
3) "v4"
127.0.0.1:6379> sunion k1 k2
1) "v2"
2) "v5"
3) "v1"
4) "v3"
5) "v4"
-----------------------------------------------
# 取差值，取在k1不在k2的值元素
127.0.0.1:6379> smembers k1
1) "v5"
2) "v1"
3) "v3"
127.0.0.1:6379> smembers k2
1) "v5"
2) "v2"
3) "v4"
127.0.0.1:6379> sdiff k1 k2
1) "v1"
2) "v3"
```
Set数据结构是dict字典，字典是使用哈希白哦实现的。


## hash数据类型
```text
Hash类型对应的数据结构有两种，ziplist(压缩列表)，hashtable(哈希表)。当数据
field-value长度段且个数比较少的时候，使用ziplist，否则使用hashtable.

```
```shell script
127.0.0.1:6379> flushdb
OK
# 添加hash对应的key，以及对应的field对应的value
127.0.0.1:6379> hset user:1001 id 1
(integer) 1
# 添加hash对应的key，以及对应的field对应的value
127.0.0.1:6379> hset user:1001 name zhangsan
(integer) 1
# 获取hash对应的key的field对应的value
127.0.0.1:6379> hset user:1001 age 20
(integer) 1
# 获取hash对应的key的field对应的value
127.0.0.1:6379> hget user:1001 id
"1"
# 获取hash对应的key的field对应的value
127.0.0.1:6379> hget user:1001 name
"zhangsan"
# 获取所有key对应的所有field-value
127.0.0.1:6379> hgetall user:1001
1) "id"
2) "1"
3) "name"
4) "zhangsan"
5) "age"
6) "20"
#批量添加 key对应的field-value
127.0.0.1:6379> hmset user:1002 id 2 name lisi age 30
OK
# 获取key对应的所有value
127.0.0.1:6379> hgetall user:1002
1) "id"
2) "2"
3) "name"
4) "lisi"
5) "age"
6) "30"
# 判断是否存在key对应的field-value
127.0.0.1:6379> hexists user:1001 id
(integer) 1
# 判断是否存在key对应的field-value
127.0.0.1:6379> hexists user:1001 gender
(integer) 0
# 获取key所有的所有的field
127.0.0.1:6379> hkeys user:1001
1) "id"
2) "name"
3) "age"
# 获取key所有的value
127.0.0.1:6379> hvals user:1001
1) "1"
2) "zhangsan"
3) "20"
# 将key对应的field-value值自增1
127.0.0.1:6379> hincrby user:1001 age 1
(integer) 21
127.0.0.1:6379> hgetall user:1001
1) "id"
2) "1"
3) "name"
4) "zhangsan"
5) "age"
6) "21"
# 原子性的添加key对应的field-value，如果存在就操作失败，如果不存在就添加成功
127.0.0.1:6379> hsetnx user:1001 id 1
(integer) 0
```

## zset有序集合
```text
有序集合zset域普通集合set非常相似，试一个没有重复元素的字符串集合。不同的指出试有序集合的每一个成员都关联了评分(score)，这
个评分被用来按照从最低分到最高分的方式排序集合中的成员。集合的成岩试唯一的，但是评分可以是重复的。

```

```shell script
# 添加有序集合
127.0.0.1:6379> zadd topk 100 java 200 c++ 300 c 400 php 500 c# 600 lua
(integer) 6
# 获取有序集合里面所有的元素
127.0.0.1:6379> zrange topk 0 -1
1) "java"
2) "c++"
3) "c"
4) "php"
5) "c#"
6) "lua"
#获取有序集合里面所有的有序元素保活它的分数
127.0.0.1:6379> zrange topk 0 -1 withscores
 1) "java"
 2) "100"
 3) "c++"
 4) "200"
 5) "c"
 6) "300"
 7) "php"
 8) "400"
 9) "c#"
10) "500"
11) "lua"
12) "600"
# 获取有序集合里面对应分数取值范围的有序集合的元素
127.0.0.1:6379> zrangebyscore topk 200 400
1) "c++"
2) "c"
3) "php"
# 按照逆序获取指定范围有序集合的元素
127.0.0.1:6379> zrevrangebyscore topk 500 300
1) "c#"
2) "php"
3) "c"
# 对有序集合的元素进行增加
127.0.0.1:6379> zincrby topk 10 java
"110"
127.0.0.1:6379> zrange topk 0 -1
1) "java"
2) "c++"
3) "c"
4) "php"
5) "c#"
6) "lua"
127.0.0.1:6379> zrange topk 0 -1 withscores
 1) "java"
 2) "110"
 3) "c++"
 4) "200"
 5) "c"
 6) "300"
 7) "php"
 8) "400"
 9) "c#"
10) "500"
11) "lua"
12) "600"
#删除有序集合中的指定元素
127.0.0.1:6379> zrem topk c#
(integer) 1
127.0.0.1:6379> zrange topk 0 -1 withscores
 1) "java"
 2) "110"
 3) "c++"
 4) "200"
 5) "c"
 6) "300"
 7) "php"
 8) "400"
 9) "lua"
10) "600"
# 统计有序集合的分数的范围的内的个数
127.0.0.1:6379> zcount topk 400 600
(integer) 2
127.0.0.1:6379> zcount topk 200 600
(integer) 4
# 获取有序集合在集合内的排名
127.0.0.1:6379> zrank topk php
(integer) 3
```

* zset的底层数据结构
```text
    SortedSet是Redis提高的一个非常特别的数据结构，一方面他等价于Java的Map<String,Double>，可以给每一个元素value赋予一个权重score,
另一方面它又类似于TreesET,内部的元素会按照权重score进行排序，可以得到每一个元素的名词，还可以通过score的范围来获取元素的列表。
    zset底层使用了两个数据结构
    1. hash,hash的左右就是关联元素value和权重score,保障元素value唯一性，可以通过元素value找到value对应的score值
    2. 跳跃表，跳跃表的目的在于给元素value排序，根据score的范围获取元素列表。
```




##






























