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

