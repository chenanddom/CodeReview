# SEATA
SEATA是阿里旗下的分布式的事务的解决方案的一个框架，其使用的是两阶段提交的来实现数据的最终一致性。
Seata 将为用户提供了 AT、TCC、SAGA 和 XA 事务模式,SEATA主推AT模式

* AT模式的前提
    * 基于支持本地ACID事务的关系型数据库
    * Java应用，通过JDBC访问数据库



*  整体的机制(两阶段提交协议的演变)
    1. 一阶段：业务数据和回滚日志记录在同一个本地事务中提交，释放本地锁和连接资源
    2. 二阶段：
        1. 提交异步化，非常快速的完成。
        2. 回滚通过以极端的回滚日志进行反向补偿。
        
    
    
## SEATA的安装和使用

### seata安装
1.下载安装包seata-server-1.3.0.zip

[安装包seata-server-1.3.0.zip](./files\seata-server-1.3.0.zip)

2.下载seata的源代码，里面的有script\server\db目录的mysql.sql的文件，创建一个数据库名字为seata-server,并且切换到seata-server数据库并执行mysql.sql

3.
 解压seata-server-1.3.0.zip然后修改seata\seata\conf\file.conf(用于配置seata的数据源，便于seata的后期配置高可用，file的方式不够方便)
 解压seata-server-1.3.0.zip然后修改seata\seata\conf\registry.conf(用于配置seata和nacos结合，实现注册中心)
 
mysql.sql的脚本如下：
[mydql.sql](./files\mysql.sql) 

file.conf的配置如下:
```text
## transaction log store, only used in seata-server
store {
  ## store mode: file、db、redis
  # 这里将数据源由原来的file改成了db  
  mode = "db"
  ## file store property
  file {
    ## store location dir
    dir = "sessionStore"
    # branch session size , if exceeded first try compress lockkey, still exceeded throws exceptions
    maxBranchSessionSize = 16384
    # globe session size , if exceeded throws exceptions
    maxGlobalSessionSize = 512
    # file buffer size , if exceeded allocate new buffer
    fileWriteBufferCacheSize = 16384
    # when recover batch read size
    sessionReloadReadSize = 100
    # async, sync
    flushDiskMode = async
  }
    
  ## database store property
  db {
    ## the implement of javax.sql.DataSource, such as DruidDataSource(druid)/BasicDataSource(dbcp)/HikariDataSource(hikari) etc.
    datasource = "druid"
    ## mysql/oracle/postgresql/h2/oceanbase etc.
    dbType = "mysql"
    driverClassName = "com.mysql.cj.jdbc.Driver"
    #配置创建好的数据库
    url = "jdbc:mysql://127.0.0.1:3306/seata-server?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai"
    user = "test"
    password = "test123"
    minConn = 5
    maxConn = 30
    globalTable = "global_table"
    branchTable = "branch_table"
    lockTable = "lock_table"
    queryLimit = 100
    maxWait = 5000
  }

  ## redis store property
  redis {
    host = "127.0.0.1"
    port = "6379"
    password = ""
    database = "0"
    minConn = 1
    maxConn = 10
    queryLimit = 100
  }
}
``` 

4. 将seata-server源码下的seata\seata\script\config-center\config.txt的配置文件修改持久方式为db,并将db的数据源配置第3步修改的数据库

[seata\seata\script\config-center\config.txt](./files\config.txt)

主要修改的地方如下:
```text
# 修改存储方式
#Transaction storage configuration, only for the server. The file, DB, and redis configuration values are optional.
store.mode=db
store.lock.mode=file
store.session.mode=file
#Used for password encryption
store.publicKey=

# 配置数据源
#These configurations are required if the `store mode` is `db`. If `store.mode,store.lock.mode,store.session.mode` are not equal to `db`, you can remove the configuration block.
store.db.datasource=druid
store.db.dbType=mysql
store.db.driverClassName=com.mysql.jdbc.Driver
store.db.url=jdbc:mysql://127.0.0.1:3306/seata-server?useUnicode=true&rewriteBatchedStatements=true
store.db.user=root
store.db.password=root@123
store.db.minConn=5
store.db.maxConn=30
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.distributedLockTable=distributed_lock
store.db.queryLimit=100
store.db.lockTable=lock_table
store.db.maxWait=5000
```     

 5. 将第4步修改的好的文件同步到nacos，只需要将同目录下的seata\seata\script\config-center\nacos\nacos-config.sh执行即可
 
 具体文件如下：
 [执行nacos-config.sh脚本文件将设计到seata的变量同步到nacos](./files\nacos\nacos-config.sh)
 
 
### 客户端的配置
1.分别创建了order(订单)和stock(库存)两个服务模块
引入如下的依赖：
```xml
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
        </dependency>
```
订单模块需要额外引入feign实现远程调用
```xml
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```


2. 编写创建订单和扣减库存的接口
```java
@Service
public class OrderService {

    @Autowired
    private TOrderMapper orderMapper;
    @Autowired
    private StockRemoteService stockRemoteService;
    @Transactional
    @GlobalTransactional
    public Object add(){
        TOrder tOrder = new TOrder();
        tOrder.setUserId("1");
        tOrder.setAmount(10.00);
        tOrder.setOrderNo(UUID.randomUUID().toString().replace("-",""));
        tOrder.setCommodityCode("1001");
        tOrder.setCount(1);
        orderMapper.createOrder(tOrder);
        Boolean aBoolean = stockRemoteService.decreaseStock(tOrder.getCommodityCode(), tOrder.getCount());
        double a = 1/0;
        return "成功";
    }
}
```
````java
@Slf4j
@RestController
@RequestMapping("/stock")
public class StockController {


    @Autowired
    private TStockMapper tStockMapper;
    /**
     * 扣减库存
     */
    @PutMapping("/dec_stock")
    public Boolean decreaseStock(@RequestParam("commodityCode")String commodityCode,@RequestParam("count")Integer count) {
        log.info("请求库存微服务：{}", commodityCode);
        return tStockMapper.decreaseStock(commodityCode,count)>0?true:false;
    }

}

````

配置文件：
```yaml
# 配置文件的优先级(优先级大的会覆盖优先级别小的，并且形成互补)
seata:
  registry:
#    配置注册中心
    type: nacos
    nacos:
      server-addr: 127.0.0.1:8848
      username: nacos
      password: nacos
      group: SEATA_GROUP
      application: seata-server
      namespace: dev
  config:
    type: nacos
    nacos:
      server-addr: 127.0.0.1:8848
      username: nacos
      password: nacos
      group: SEATA_GROUP
      application: seata-server
      namespace: dev
# profile>默认的配置文件>extention-configs(下表越大优先级越大)>shared-configs(下标越大优先级越大)
feign:
  okhttp:
    enabled: true
  client:
    config:
      default:
        #不设置connectTimeout会导致readTimeout设置不生效
        connectTimeout: 3000
        readTimeout: 6000
```



详细代码可以查看:

[seata-order](https://github.com/chenanddom/springcloudalibaba/tree/master/seata-order)

[seata-stock](https://github.com/chenanddom/springcloudalibaba/tree/master/seata-stock)











