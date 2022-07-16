# MyBatis




## MyBatis缓存
```text
为了提高服务的并发性能，所有引入了缓存，也许说到缓存象到的是redis，但是，
实际的项目开发中需要有多级缓存。因为多级缓存的存在，可以减少了数据库的网络
IO和磁盘IO，减少了在IO上的消耗，增加了系统的并发性能.MyBatis也作为一个出
色的ORM框架也是拥有了出色的缓存机制，主要包括了一级缓存和二级缓存。MyBatis的
一级缓存默认是打开的，而且mybatis的一级缓存解决了DBMS的脏读和不可重复读的问题。
mybatis的以及缓存主要与sqlsession相关联。如果sqlSession关闭了缓存会清空，如果
做了update操作(不是xml的文件的标签)也会清除缓存，在提交事务commit和回滚事务 
rollback都会轻触缓存
```


### MyBatis命中缓存的规则
条件一:要求查询的StatementId必须相同，否则无法命中缓存，及时两个拆线呢语句，参数都完全一样。
![statementId](./files/mybatis-statementId-1.png)
```java
ManagementMapper mapper = sqlSession.getMapper(ManagementMapper.class);
List<Management> managementList = mapper.getById(1);
List<Management> managementList2 = mapper.getById2(1);
```

条件二:要求传递给SQL的查询参数必须相同，否则无法命中缓存
```xml
    <select id="getById4" parameterType="java.util.Map" resultType="com.itdom.entity.Management">
        select * from <include refid="tbName"></include> where id=#{id}
    </select>
```

```java
        ManagementMapper mapper = sqlSession.getMapper(ManagementMapper.class);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id",1);
        map.put("test",2);
        List<Management> managementList = mapper.getById4(map);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("id",1);
        map2.put("test",2);
        List<Management> managementList2 = mapper.getById4(map2);
```
执行结果如下:
```text
21:59:26.146 [main] DEBUG org.apache.ibatis.transaction.jdbc.JdbcTransaction - Setting autocommit to false on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@41a0aa7d]
21:59:26.161 [main] DEBUG com.itdom.mapper.ManagementMapper.getById4 - ==>  Preparing: select * from management where id=?
21:59:26.210 [main] DEBUG com.itdom.mapper.ManagementMapper.getById4 - ==> Parameters: 1(Integer)
21:59:26.245 [main] DEBUG com.itdom.mapper.ManagementMapper.getById4 - <==      Total: 1
```


条件三:要求分页参数必须相同，否则无法命中缓存。缓存粒度是整个分页擦汗寻结果，二不是结果中的每个对象。
```java
        RowBounds rowBounds = new RowBounds(0,1);
        sqlSession.selectList("com.itdom.mapper.ManagementMapper.list",null,rowBounds);
        RowBounds rowBounds2 = new RowBounds(0,2);
        sqlSession.selectList("com.itdom.mapper.ManagementMapper.list",null,rowBounds2);
```
```xml
    <select id="list" resultType="com.itdom.entity.Management">
        select * from <include refid="tbName"></include> where 1=1
    </select>
```
执行结果:
```text
22:13:24.876 [main] DEBUG org.apache.ibatis.datasource.pooled.PooledDataSource - Created connection 1894369629.
22:13:24.876 [main] DEBUG org.apache.ibatis.transaction.jdbc.JdbcTransaction - Setting autocommit to false on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@70e9c95d]
22:13:24.880 [main] DEBUG com.itdom.mapper.ManagementMapper.list - ==>  Preparing: select * from management where 1=1
22:13:24.930 [main] DEBUG com.itdom.mapper.ManagementMapper.list - ==> Parameters: 
22:13:24.962 [main] DEBUG com.itdom.mapper.ManagementMapper.list - ==>  Preparing: select * from management where 1=1
22:13:24.962 [main] DEBUG com.itdom.mapper.ManagementMapper.list - ==> Parameters: 
```
条件四:要求传递的JDBC的SQL 必须完全相同
```text
<select id="getById" parameterType="..." resultType="....">
SELECT * FROM management
<where>
<if test="type==1">id=#{id}</if>
<if test="type==2">1=1 AND id=#{id}</if>
</where

</select>

```

### 一级缓存的生命周期
mybatis缓存的产生都是sqlSession的select的相关方法，如果调用的是sqlSession的update的方法，那么缓存就会失效

![sqlSession的update方法导致缓存失效](./files/mybatis-cache-invalid-1.png)

![sqlSession关闭导致缓存失效](./files/mybatis-cache-invalid-2.png)

![事务提交会清空缓存](./files/mybatis-cache-invalid-3.png)

![事务回滚缓存失效](./files/mybatis-cache-invalid-4.png)

更新数据会导致缓存失效


[一级缓存源码剖析](./ANALYSIS_1CACHE_SOURCE_CODE.MD)
 






### MyBatis与Spring集成

* 未开启事务
    每次请求，Spring都会关闭旧的session再创建新得session，所以此时一级缓存是无效的
* 开启事务
    在一个事务内，Spring通过ThreadLocal始终使用同一个session，此时以及欢子啊事务内是有效的。
    
    
    
    
    
    
    
    
    
    
    
## MyBatis二级缓存

### 二级缓存的命中规则
MyBatis中的二级缓存在建立在SqlSessionFactory里面的，所以，如果不是同一个sqlsessionFactory的情况下就无法实现二级缓存的饿重用。
除此之外，它的命中规则也是和以及缓存是一样的，包括statementId,参数，分页参数，sql语句，环境等。    
    
    
    
### MyBatis二级缓存的生命周期
二级缓存产生的条件
    1. 满足一级缓存的产生条件
    2. close Session或者commit session
* 清除二级缓存都得条件
    * 创建新的sqlsession,
    * 提交事务 commit
    * update操作
* 注意，回滚rollback和clearCache无法清除缓存

* sql标签是否缓存数据
```xml
<!-- useCache="true"表示查询使用缓存 -->
    <select id="selectNoneClearCache" resultType="com.itdom.entity.Management" useCache="true">
        select * from <include refid="tbName"></include> where id=#{id}
    </select>
<!--表示不清除缓存    -->
    <update id="updateById" flushCache="false">
        update <include refid="tbName"></include> set username="张三" where id=#{id}
    </update>
```            
    
### eviction清除策略
LRU 最近最少使用:移除最长实践不被使用的对象 LinkHashMap
FIFO 先进先出:按照对象对象进入缓存的顺序来移除它们 LinkedList
SOFT 软引用:基于GC和软引用规则移除对象 SoftReference
WEAK 弱引用：基于GC和弱引用规则移除对象 WeakReference

SIZE引用数目
缓存引用的最大数目，默认是1024
当缓存引用达到最大数目的时候，按照eviction设定的策略执行清除


## MyBatis二级缓存的源码剖析
[MyBatis二级缓存的源码剖析](./ANALYSIS_2CACHE_SOURCE_CODE.MD)    
    
    
    
    
    
    
    
    
    
    
        