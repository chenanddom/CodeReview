# Redis事务-锁-秒杀
## 事务
Redis事务师一个单独的隔离操作：事务中所有的命令都会序列化，按照是顺序执行。事务在执行的过程中，不回被其他的客户端
发送过来的命令请求打断。
Redis事务的主要作用就是串联多个明客防止别的命令插队

### Multi、Exec、discard
从输入Multi命令开始，输入的命令都会依次的进入命令队列中，但是不会执行，直到输入Exec后，Redis命令会将之前的命令队列中的命令依次执行，
组队的过程中可以通过discard来放弃组队。

注意：
```text
如果在组队中有任何一个命令执行出错，那么就本次事务就无法执行exec就出错回滚了
如果在组队过程中是成功的，但是命令的执行Exec的时候就集训执行正确的，错误的提示错误。
```
例子：
```shell script
192.168.0.199:6379> multi
OK
192.168.0.199:6379(TX)> set k1 v1
QUEUED
192.168.0.199:6379(TX)> set k2 v2
QUEUED
# 组队失败
192.168.0.199:6379(TX)> set k2
(error) ERR wrong number of arguments for 'set' command
# 组队失败执行整个队列里面的命令都会失败
192.168.0.199:6379(TX)> exec
(error) EXECABORT Transaction discarded because of previous errors.
# ----------------------------------------------------------------------
192.168.0.199:6379> multi
OK
192.168.0.199:6379(TX)> set k1 v1 
QUEUED
192.168.0.199:6379(TX)> set k2  v2
QUEUED
# 执行会出错的指令，提交没问题
192.168.0.199:6379(TX)> incr k1
QUEUED
# 组队成功，那么在exec的执行后阶段就执行正确的指令，提示错误的指令。
192.168.0.199:6379(TX)> exec
1) OK
2) OK
3) (error) ERR value is not an integer or out of range
192.168.0.199:6379> keys *
1) "k2"
2) "k1"

```

### 事务的冲突

* 悲观锁
    加锁

* 乐观锁
    使用版本控制
示例：
```shell script
---------------client1-----------------------------------------------------------------
# 监控键k1
192.168.0.199:6379> watch k1
OK
# 组队-开启事务
192.168.0.199:6379> multi
OK
192.168.0.199:6379(TX)> set k1 v1
QUEUED
# 提交事务
192.168.0.199:6379(TX)> exec
1) OK
192.168.0.199:6379> 
--------------client2----------------------------------------------------
# 监控键k1
192.168.0.199:6379> watch k1
OK
# 组队-开启事务
192.168.0.199:6379> multi
OK
192.168.0.199:6379> set k1 v1
QUEUED
# 提交事务
192.168.0.199:6379> exec
# 返回nil表示失败
(nil)
```

*　Redis事务的三大特性
    * 单独的隔离操作
        * 事务中的所有命令都会序列化，按顺序地执行。事务在执行的过程中，不会被其他客户端发送过来的命令请求打断
    * 没有隔离级别的概念
        * 队列中的命令没有提交之前都不会被实际执行，因为没有事务提交钱任何指令都不会被实际执行
    * 不保证原子性
        * 事务中如果有一条命令执行失败，其后的命令仍然会被执行，不会回滚    
        
        
* 秒杀的设计
秒杀的场景通常是高并发的情况，在高并发的情况下，要保证服务的不出现超卖的情况，还要保证服务的不出现遗留的问题。
方案设计图:

[方案设计图](https://app.diagrams.net/#Hchenanddom%2FCharts%2Fmain%2Frepo%2F%E7%A7%92%E6%9D%80%E6%96%B9%E6%A1%88%E7%9A%84%E8%AE%BE%E8%AE%A1.drawio)

核心代码设计

[JedisPoolConfiguration关于Jedis连接池的配置](./database-redis/src/main/java/com/itdom/config/JedisPoolConfiguration.java)

[核心业务逻辑的代码](./database-redis/src/main/java/com/itdom/service/SeckillService.java)

```java
 public Boolean secSkillProduct(SecKillProduct product) {
        product.setProductName("飞科剃须刀");
        product.setProductNumber("1001");
        product.setUserId(UUID.randomUUID().toString());
            Jedis jedis = pool.getResource();
            String kcKey = "sku:" + product.getProductNumber() + ":product";
            String userKey = "userId:" + product.getProductNumber() + ":user";
        try {
            //1. 监控商品库存的key
            jedis.watch(kcKey);
            //2. 获取库存的数据
            String num = jedis.get(kcKey);
            Integer number = Integer.valueOf(num);
            //如果没有库存就提示秒杀还没开始或者秒杀结束
            if (number == null || number <= 0) {
                logger.debug("秒杀结束了");
                //关闭jedis，在jedis3.0之后就弃用了之前使用的pool.returnResource()回收jedis连接，转而使用关闭实现连接的发放回
                jedis.close();
                return false;
            }
            //判断当前的用户是否已经参与过秒杀
            boolean isExist = jedis.sismember(userKey, product.getUserId());
            if (isExist) {
                logger.debug("你已经秒杀过了，不可以再次参与秒杀!!!");
                jedis.close();
                return false;
            }
            //开启组队(事务)
            Transaction multi = jedis.multi();
            multi.decr(kcKey);
            multi.sadd(userKey, product.getUserId());
            //提交事务
            List<Object> result = multi.exec();
            if (CollectionUtils.isEmpty(result)) {
                logger.debug("秒杀失败");
                jedis.close();
                return false;
            }
            jedis.close();
            return true;
        } catch (Exception e) {
            jedis.close();
            return false;
        }
    }
```
* 注意:
以上的代码的实现已经通过watch监控对应的key，和开启组队事务解决了对秒杀超卖的问题，但是还是会存在一个遗留的问题，因为组队使用了乐观锁，会
导致执行失败但是商品实际上还存在的情况，此时需要使用lua来实现最稳妥。
        
#### lua解决redis出现的遗漏问题。
lua可以将复杂的或者多部的redis操作，写为一个脚本，一次性的提交给redis执行，减少发福连接redis的次数。提升性能。

```lua
# 参数1
local userid=KEYS[1];
#参数2
local prodid=KEYS[2];

local qtkey="sku:"..prodid..":product";
local userskey="userId:"..prodid..":user";
local userExists=redis.call("sismember",userskey,userid);if tonumber(userExists)==1 then
  return 2;
end
local num=redis.call("get",qtkey);
if tonumber(num)<=0 then
  return 0;
else
  redis.call("decr",qtkey);
  redis.call("sadd",userskey,userid);
  end
  return 1
``` 
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
             