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
 
 
 
 






