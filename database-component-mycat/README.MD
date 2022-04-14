# MyCAT

## MyCAT的相关概念
* 逻辑库(schema)
```text
    MyCat是一个数据库中间件，通常对实际应用来说，并不需要知道中间件的存在，业务开发人员只需要
    知道数据库的概念，所以数据库中间件可以被看做是一个或多个数据库集群构成的逻辑库。
```

* 逻辑表
    既然有逻辑库，那么就会有逻辑表，分布式数据库中，对应用来说，读写数据的表就是逻辑表。逻辑
    表，可以是数据切分后，分布在一个或多个分片库中，也可以不做数据切分，不分片，只有一个表构
    成。
    * 分片表
        是指那些原有的很大数据的表，需要切分到多个数据库的表，这样，每个分片都有一部分数据，所有分
        片构成了完整的数据。 总而言之就是需要进行分片的表。如 ：tb_order 表是一个分片表, 数据按
        照规则被切分到dn1、dn2两个节点。
        
    *   非分片表
        一个数据库中并不是所有的表都很大，某些表是可以不用进行切分的，非分片是相对分片表来说的，就
        是那些不需要进行数据切分的表。如： tb_city是非分片表 , 数据只存于其中的一个节点 dn1
        上。    
    *   ER表
        关系型数据库是基于实体关系模型(Entity Relationship Model)的, MyCat中的ER表便来源于
        此。 MyCat提出了基于ER关系的数据分片策略 , 字表的记录与其所关联的父表的记录存放在同一个
        数据分片中, 通过表分组(Table Group)保证数据关联查询不会跨库操作。
    
    * 全局表
        在一个大型的项目中,会存在一部分字典表(码表) , 在其中存储的是项目中的一些基础的数据 , 而
        这些基础的数据 , 数据量都不大 , 在各个业务表中可能都存在关联 。当业务表由于数据量大而分
        片后 ， 业务表与附属的数据字典表之间的关联查询就变成了比较棘手的问题 ， 在MyCat中可以通
        过数据冗余来解决这类表的关联查询 ， 即所有分片都复制这一份数据（数据字典表），因此可以把这
        些冗余数据的表定义为全局表。
        
* 分片节点(dataNode)
    数据切分后，一个大表被分到不同的分片数据库上面，每个表分片所在的数据库就是分片节点
    （dataNode）。

* 节点主机(dataHost)
    数据切分后，每个分片节点（dataNode）不一定都会独占一台机器，同一机器上面可以有多个分片数
    据库，这样一个或多个分片节点（dataNode）所在的机器就是节点主机（dataHost）,为了规避单节
    点主机并发数限制，尽量将读写压力高的分片节点（dataNode）均衡的放在不同的节点主机
    （dataHost）。       
    
    
 ## 分片配置测试
 server.xml
 ```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- - - Licensed under the Apache License, Version 2.0 (the "License"); 
	- you may not use this file except in compliance with the License. - You 
	may obtain a copy of the License at - - http://www.apache.org/licenses/LICENSE-2.0 
	- - Unless required by applicable law or agreed to in writing, software - 
	distributed under the License is distributed on an "AS IS" BASIS, - WITHOUT 
	WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. - See the 
	License for the specific language governing permissions and - limitations 
	under the License. -->
<!DOCTYPE mycat:server SYSTEM "server.dtd">
<mycat:server xmlns:mycat="http://io.mycat/">
	<system>
	<property name="nonePasswordLogin">0</property> <!-- 0为需要密码登陆、1为不需要密码登陆 ,默认为0，设置为1则需要指定默认账户-->
	<property name="useHandshakeV10">1</property>
	<property name="useSqlStat">1</property>  <!-- 1为开启实时统计、0为关闭 -->
	<property name="useGlobleTableCheck">0</property>  <!-- 1为开启全加班一致性检测、0为关闭 -->
		<property name="sqlExecuteTimeout">300</property>  <!-- SQL 执行超时 单位:秒-->
		<property name="sequnceHandlerType">2</property>
		<!--<property name="sequnceHandlerPattern">(?:(\s*next\s+value\s+for\s*MYCATSEQ_(\w+))(,|\)|\s)*)+</property>-->
		<!--必须带有MYCATSEQ_或者 mycatseq_进入序列匹配流程 注意MYCATSEQ_有空格的情况-->
		<property name="sequnceHandlerPattern">(?:(\s*next\s+value\s+for\s*MYCATSEQ_(\w+))(,|\)|\s)*)+</property>
	<property name="subqueryRelationshipCheck">false</property> <!-- 子查询中存在关联查询的情况下,检查关联字段中是否有分片字段 .默认 false -->
      <!--  <property name="useCompression">1</property>--> <!--1为开启mysql压缩协议-->
        <!--  <property name="fakeMySQLVersion">5.6.20</property>--> <!--设置模拟的MySQL版本号-->
	<!-- <property name="processorBufferChunk">40960</property> -->
	<!-- 
	<property name="processors">1</property> 
	<property name="processorExecutor">32</property> 
	 -->
        <!--默认为type 0: DirectByteBufferPool | type 1 ByteBufferArena | type 2 NettyBufferPool -->
		<property name="processorBufferPoolType">0</property>
		<!--默认是65535 64K 用于sql解析时最大文本长度 -->
		<!--<property name="maxStringLiteralLength">65535</property>-->
		<!--<property name="sequnceHandlerType">0</property>-->
		<!--<property name="backSocketNoDelay">1</property>-->
		<!--<property name="frontSocketNoDelay">1</property>-->
		<!--<property name="processorExecutor">16</property>-->
		<!--
			<property name="serverPort">8066</property> <property name="managerPort">9066</property> 
			<property name="idleTimeout">300000</property> <property name="bindIp">0.0.0.0</property>
			<property name="dataNodeIdleCheckPeriod">300000</property> 5 * 60 * 1000L; //连接空闲检查
			<property name="frontWriteQueueSize">4096</property> <property name="processors">32</property> -->
		<!--分布式事务开关，0为不过滤分布式事务，1为过滤分布式事务（如果分布式事务内只涉及全局表，则不过滤），2为不过滤分布式事务,但是记录分布式事务日志-->
		<property name="handleDistributedTransactions">0</property>
		
			<!--
			off heap for merge/order/group/limit      1开启   0关闭
		-->
		<property name="useOffHeapForMerge">0</property>

		<!--
			单位为m
		-->
        <property name="memoryPageSize">64k</property>

		<!--
			单位为k
		-->
		<property name="spillsFileBufferSize">1k</property>

		<property name="useStreamOutput">0</property>

		<!--
			单位为m
		-->
		<property name="systemReserveMemorySize">384m</property>


		<!--是否采用zookeeper协调切换  -->
		<property name="useZKSwitch">false</property>

		<!-- XA Recovery Log日志路径 -->
		<!--<property name="XARecoveryLogBaseDir">./</property>-->

		<!-- XA Recovery Log日志名称 -->
		<!--<property name="XARecoveryLogBaseName">tmlog</property>-->
		<!--如果为 true的话 严格遵守隔离级别,不会在仅仅只有select语句的时候在事务中切换连接-->
		<property name="strictTxIsolation">false</property>
		
		<property name="useZKSwitch">true</property>
		
	</system>
	
	<!-- 全局SQL防火墙设置 -->
	<!--白名单可以使用通配符%或着*-->
	<!--例如<host host="127.0.0.*" user="root"/>-->
	<!--例如<host host="127.0.*" user="root"/>-->
	<!--例如<host host="127.*" user="root"/>-->
	<!--例如<host host="1*7.*" user="root"/>-->
	<!--这些配置情况下对于127.0.0.1都能以root账户登录-->
	<!--
	<firewall>
	   <whitehost>
	      <host host="1*7.0.0.*" user="root"/>
	   </whitehost>
       <blacklist check="false">
       </blacklist>
	</firewall>
	-->

	<user name="root" defaultAccount="true">
		<property name="password">root@123</property>
		<property name="schemas">mycatdb</property>
		
		<!-- 表级 DML 权限设置 -->
		<!-- 		
		<privileges check="false">
			<schema name="TESTDB" dml="0110" >
				<table name="tb01" dml="0000"></table>
				<table name="tb02" dml="1111"></table>
			</schema>
		</privileges>		
		 -->
	</user>

	<user name="user">
		<property name="password">root@123</property>
		<property name="schemas">mycatdb</property>
		<property name="readOnly">true</property>
	</user>

</mycat:server>

```   
schema.xml
```xml
<?xml version="1.0"?>
<!DOCTYPE mycat:schema SYSTEM "schema.dtd">
<mycat:schema xmlns:mycat="http://io.mycat/">

	<schema name="TESTDB" checkSQLschema="true" sqlMaxLimit="100">
		<!-- auto sharding by id (long) -->
		<table name="travelrecord" dataNode="dn1,dn2,dn3" rule="auto-sharding-long" />
		<!-- <table name="oc_call" primaryKey="ID" dataNode="dn1$0-743" rule="latest-month-calldate"
			/> -->
	</schema>
	<!-- <dataNode name="dn1$0-743" dataHost="localhost1" database="db$0-743"
		/> -->
	<dataNode name="dn1" dataHost="localhost1" database="db1" />
	<dataNode name="dn2" dataHost="localhost1" database="db2" />
	<dataNode name="dn3" dataHost="localhost1" database="db3" />
	<!--<dataNode name="dn4" dataHost="sequoiadb1" database="SAMPLE" />
	 <dataNode name="jdbc_dn1" dataHost="jdbchost" database="db1" />
	<dataNode	name="jdbc_dn2" dataHost="jdbchost" database="db2" />
	<dataNode name="jdbc_dn3" 	dataHost="jdbchost" database="db3" /> -->
	<dataHost name="localhost1" maxCon="1000" minCon="10" balance="0"
			  writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<!-- can have multi write hosts -->
		<writeHost host="hostM1" url="localhost:3306" user="root"
				   password="123456">
		</writeHost>
		<!-- <writeHost host="hostM2" url="localhost:3316" user="root" password="123456"/> -->
	</dataHost>
	<!--
		<dataHost name="sequoiadb1" maxCon="1000" minCon="1" balance="0" dbType="sequoiadb" dbDriver="jdbc">
		<heartbeat> 		</heartbeat>
		 <writeHost host="hostM1" url="sequoiadb://1426587161.dbaas.sequoialab.net:11920/SAMPLE" user="jifeng" 	password="jifeng"></writeHost>
		 </dataHost>

	  <dataHost name="oracle1" maxCon="1000" minCon="1" balance="0" writeType="0" 	dbType="oracle" dbDriver="jdbc"> <heartbeat>select 1 from dual</heartbeat>
		<connectionInitSql>alter session set nls_date_format='yyyy-mm-dd hh24:mi:ss'</connectionInitSql>
		<writeHost host="hostM1" url="jdbc:oracle:thin:@127.0.0.1:1521:nange" user="base" 	password="123456" > </writeHost> </dataHost>

		<dataHost name="jdbchost" maxCon="1000" 	minCon="1" balance="0" writeType="0" dbType="mongodb" dbDriver="jdbc">
		<heartbeat>select 	user()</heartbeat>
		<writeHost host="hostM" url="mongodb://192.168.0.99/test" user="admin" password="123456" ></writeHost> </dataHost>

		<dataHost name="sparksql" maxCon="1000" minCon="1" balance="0" dbType="spark" dbDriver="jdbc">
		<heartbeat> </heartbeat>
		 <writeHost host="hostM1" url="jdbc:hive2://feng01:10000" user="jifeng" 	password="jifeng"></writeHost> </dataHost> -->

	<!-- <dataHost name="jdbchost" maxCon="1000" minCon="10" balance="0" dbType="mysql"
		dbDriver="jdbc"> <heartbeat>select user()</heartbeat> <writeHost host="hostM1"
		url="jdbc:mysql://localhost:3306" user="root" password="123456"> </writeHost>
		</dataHost> -->
</mycat:schema>
```

MyCat原理介绍
MyCat原理中最重要的一个动词就是 "拦截", 它拦截了用户发送过来的SQL语句, 首先对SQL语句做
一些特定的分析,如分片分析、路由分析、读写分离分析、缓存分析等，然后将此SQL语句发往后端的
真实数据库，并将返回的结果做适当处理，最终再返回给用户，如图所示。
![mycat的运行原理](./files\mycat的运行原理.png)
在图中,user表被分为三个分片节点dn1、dn2、dn3, 他们分布式在三个MySQL
Server(dataHost)上，因此可以使用1-N台服务器来分片,分片规则(sharding rule)为典型的字
符串枚举分片规则, 一个规则的定义是分片字段+分片函数。这里的分片字段为 status，分片函数则
为字符串枚举方式。
INSERT INTO TB_TEST(ID,TITLE) VALUES(1,'goods1');
INSERT INTO TB_TEST(ID,TITLE) VALUES(2,'goods2');
INSERT INTO TB_TEST(ID,TITLE) VALUES(3,'goods3');
1
2
3
INSERT INTO TB_TEST(ID,TITLE) VALUES(5000001,'1 goods5000001');
1 INSERT INTO TB_TEST(ID,TITLE) VALUES(10000001,'goods10000001');
MyCat收到一条SQL语句时，首先解析SQL语句涉及到的表，接着查看此表的定义，如果该表存在分片
规则，则获取SQL语句里分片字段的值，并匹配分片函数，得到该SQL语句对应的分片列表，然后将
SQL语句发送到相应的分片去执行，最后处理所有分片返回的数据并返回给客户端。以"select *
from user where status='0'" 为例, 查找 status='0' ,按照分片函数, '0' 值存放在
dn1,于是SQL语句被发送到第一个节点中执行, 然后再将查询的结果返回给用户。
如果发送的SQL语句为 "select * from user where status in ('0','1')" , 那么SQL语
句会被发送到dn1,dn2对应的主机上执行, 然后将结果集合并后输出给用户。


## MyCat配置文件详解
###server.xml
* system标签的内的解析

|属性|取值|含义|
| ---- | ---- | ---- |
|chaset|utf8设置Mycat的字符集，字符需要于MySQL字符集保持一直||
|nonePasswordLogin|0,1|0为需要密码登录，1为不需要密码登录，默认为0，设置为1则需要指定默认的账户|
|useHandshakeV10|0,1|使用盖选项主要的目的是为了能够兼容高版本的jdbc驱动，是否采用HandshakeV10Packet来于clien进行通信，1：是，0：否|
|useSqlStat|0，1|开启SQL实时的统计，1为开启，0为关闭；开启之后MyCat会自动统计SQL语句的执行的情况查看MyCat执行的SQL, 执行效率比较低的SQL, SQL的整体执行情况、读写比例等 ; show @@sql ; show @@sql.slow ; show @@sql.sum ;|
|useGlobleTableCheck|0,1|是否开启全局白哦的一致性检测。1为开启，0为关闭|
|sqlExecuteTimeout|1000|SQL语句执行的超时时间 , 单位为 s ;|
|sequnceHandlerType|0,1,2|用来指定MyCat全局序列类型，0为本地文件，1为数据库方式，2为时间戳的方式，默认使用本地文件方式，文件方式主要用于测试|
|sequnceHandlerPattern|正则表达式|必须要要有MYCATSEQ或者mycatseq进入序列的匹配流程注意MYCATSEQ_有空格的情况|
|subquertRelationShipCheck|true,false|子查询中存在关联拆线呢的情况下，检查关联字段中是否有分片字段，默认false|
|useCompression|0，1|开启mysql压缩协议，0：关闭，1开启|
|fakeMySQLVersion|5.5,5.6|设置模拟的MySQL版本号|
|defaultSqlParser| |由于MyCat的最初版本使用了FoundationDB的SQL解析器, 在MyCat1.3后增加了Druid解析器, 所以要设置defaultSqlParser属性来指定默认的解析器; 解析器有两个 :druidparser 和 fdbparser, 在MyCat1.4之后,默认是druidparser,fdbparser已经废除了|
|processors|1,2....|指定系统可用的线程数量, 默认值为CPU核心x 每个核心运行线程数量; processors 会 影响processorBufferPool,processorBufferLocalPercent,processorExecutor属性, 所有, 在性能调优时, 可以适当地修改processors值|
|processorBufferChunk| |指定每次分配Socket Direct Buffer默认值为4096字节, 也会影响BufferPool长度, 如果一次性获取字节过多而导致buffer不够用, 则会出现警告, 可以调大该值|
|processorExecutor | |指定NIOProcessor上共享 businessExecutor固定线程池的大小; MyCat把异步任务交给 businessExecutor 线程池中, 在新版本的MyCat中这个连接池使用频次不高, 可以适当地把该值调小|
|packetHeaderSize| |指定MySQL协议中的报文头长度, 默认4个字节|
|maxPacketSize  | |指定MySQL协议可以携带的数据最大大小, 默认值为16M|
|idleTimeout|30|指定连接的空闲时间的超时长度;如果超时,将闭资源并回收, 默认30分钟|
|txIsolation|1,2,3,4|初始化前端连接的事务隔离级别,默认为REPEATED_READ , 对应数字为3READ_UNCOMMITED=1; READ_COMMITTED=2; REPEATED_READ=3; SERIALIZABLE=|
|sqlExecuteTimeout|300|执行SQL的超时时间, 如果SQL语句执行超时, 将关闭连接; 默认300秒;|
|serverPort|8066|定义MyCat的使用端口, 默认8066|
|managerPort|9066|定义MyCat的管理端口, 默认9066|

* user标签的解析
user标签主要用于定义登录MyCat的用户和权限:
    1. <user name="root" defaultAccount="true">：name属性用于声明用户名；
    2. <property name="schemas"">mycat</property>:能够访问的逻辑库，多个的话使用","分割
    3. <property name="password">123456</property>:指定用户访问MyCat的密码。
    4. <property name="readOnly">true</property>:是否只读
    5. <property name="benchmark">11111</property>:指定前端的这个题连接数量，0或者不设置标识不限制。
    6. <property name="usingDecrypt">0</property>:是否对密码加密，默认数据0否，1是。
    加密方式如下：
```text
[root@localhost lib]# java -cp Mycat-server-1.6.7.3-release.jar io.mycat.util.DecryptUtil 0:root:test123
其中 0:user:password是加密字符串,有两种格式

dataHost加密格式
1:hostM1:root:123456
1代表是dataHost加密
hostM1是<writeHost host="hostM1" 
root是user="root"
123456是 password=明文密码(123456)

对应writeHost配置
		<writeHost host="hostM1" url="localhost:3306" user="root"
				   password="BpkNIjF7LfzS1C76HT7B1bJgmGIDtPihqIvHBlC92L1IFqsMfoJEMk1EkxSzjasWB4GWoUcODYO4AaJstdAp5w==" usingDecrypt="1">
			<!-- can have multi read hosts -->
		</writeHost>

mycat用户登录密码加密格式
0:root:123456
0代表mycat用户登录密码加密
	<user name="root" defaultAccount="true">
		<property name="usingDecrypt">1</property>
		<property name="password">d6D+pOmkuUoY09p4/aivwMsScLa7zfjIwAxvkEhr3v7en06mEXoX9DTTjQNug5CfvGf7Wy9oLcthYI3yLMSjIg==</property>
		<property name="schemas">TESTDB</property>
kOdSW+qrXfdYoa+qhNBZVARSxlhCZFS+PwP2NTzKYtb8rOntlZa+XFfxUPgE+3spkNpZgiNlDonoI1zz6OGwcg==
    
```
user标签对应的配置
```xml
<user name="root" defaultAccount="true">
		<!--<property name="password">test123</property>-->
		<property name="password">kOdSW+qrXfdYoa+qhNBZVARSxlhCZFS+PwP2NTzKYtb8rOntlZa+XFfxUPgE+3spkNpZgiNlDonoI1zz6OGwcg==</property>
		<property name="schemas">mycatdb</property>
		<property name="usingDecrypt">1</property>
		
		<!-- 表级 DML 权限设置 -->
		<!-- 		
		<privileges check="false">
			<schema name="TESTDB" dml="0110" >
				<table name="tb01" dml="0000"></table>
				<table name="tb02" dml="1111"></table>
			</schema>
		</privileges>		
		 -->
	</user>
```
重启mycat-server
```shell script
./bin/mycat restart
```
再次登录可以成功:

![重启mycat-server再次登录可以成功](./files\密码加密后登录.PNG)


* 防火墙firewall 标签
firewall标签是用来定义防火墙；firewall下的whitehost标签用来定义IP白名单，backlist用来定义SQL黑名单
```xml
<firewall>
	   <whitehost>
	      <host host="1*7.0.0.*" user="root"/>
	   </whitehost>
       <blacklist check="false">
       </blacklist>
	</firewall>
```
SQL黑名单的相关配置

|配置项|缺省值|描述|
| ---- | ---- | ---- |
|selectAllow|true|是否允许执行SELECT语句|
|selectAllColumnAllow|true|是否允许执行 SELECT * FROM T 这样的语句。如果设置为 false，不允许执行select * from t，但可以select *from (select id, name from t) a。这个选项是防御程序通过调用 select *获得数据表的结构信息。|
|selectIntoAllow|true|SELECT查询中是否允许INTO子句|
|deleteAllow|true|是否允许执行DELETE语句|
|updateAllow|true|是否允许执行UPDATE语句|
|insertAllow|true|是否允许执行INSERT|
|replaceAllow|true|是否允许执行REPLACE语句|
|mergeAllow|true|是否允许执行merge语句，这个旨在oracle数据库使用|
|callAllow|true|是否允许通过jdbc的call语法调用存储过程|
|setAllow|true|是否允许使用SET语法|
|truncateAllow|true|truncate 语句是危险，缺省打开，若需要自行关闭|
|createTableAllow|true|是否允许创建表|
|alterTableAllow|true|是否允许执行 Alter Table 语句|
|dropTableAllow|true|是否允许修改表|
|commentAllow|false|是否允许语句中存在注释，Oracle 的用户不用担心，Wall能够识别 hints和注释的区别|
|noneBaseStatementAllow|false|是否允许非以上基本语句的其他语句，缺省关闭，通过这个选项就能够屏蔽 DDL。|
|multiStatementAllow|false|是否允许一次执行多条语句，缺省关闭|
|useAllow|true|是否允许执行 mysql 的 use 语句，缺省打开|
|describeAllow|true|是否允许执行 mysql 的 describe 语句，缺省打开|
|showAllow|true|是否允许执行 mysql 的 show 语句，缺省打开|
|commitAllow  |true|是否允许执行 commit 操作|
|rollbackAllow |true| 是否允许执行 roll back 操作|
| 是否允许执行 roll back 操作|true|检查 SELECT 语句的 WHERE 子句是否是一个永真条件|
|selectHavingAlwayTrueCheck |true|检查 SELECT 语句的 HAVING 子句是否是 一个永真条件|
|deleteWhereAlwayTrueCheck|true|检查 DELETE 语句的 WHERE 子句是否是一个永真条件|
|deleteWhereNoneCheck|false|检查 DELETE 语句是否无 where 条件，这是有风险的，但不是 SQL 注入类型的风险|
|updateWhereAlayTrueCheck|true|检查 UPDATE 语句的 WHERE 子句是否是一个永真条件|
|updateWhereNoneCheck|false|检查 UPDATE 语句是否无 where 条件，这是有风险的，但不是SQL 注入类型的风险|
|conditionAndAlwayTrueAllow|false|检查查询条件(WHERE/HAVING 子句)中是否包含 AND 永真条件|
|conditionAndAlwayFalseAllow|false|检查查询条件(WHERE/HAVING 子句)中是否包含 AND 永假条件|
|conditionLikeTrueAllow|true|检查查询条件(WHERE/HAVING 子句)中是否包含 LIKE 永真条件|
|selectIntoOutfileAllow|false|SELECT ... INTO OUTFILE 是否允许，这个是 mysql 注入攻击的常见手段，缺省是禁止的|
|selectUnionCheck|true|检测 SELECT UNION|
|selectMinusCheck|true|检测 SELECT MINUS|
|selectExceptCheck|true|检测 SELECT EXCEPT|
|selectIntersectCheck |true|检测 SELECT INTERSECT|
...




### schema.xml
schema.xml作为MyCat中最重要的配置文件之一，涵盖了MyCat的逻辑库、表、分片规则、分配结点及数据源的配置

* schema标签
schema标签用于定义MyCat示例中的逻辑库，一个MyCat实例中，可以有多个逻辑库，可以通过schema标签来划分不同的逻辑库。MyCat中的逻辑库的概念
，等同于MySQL中的database概念，需要操作某个罗基库下的表时，也需要切换逻辑库。
```xml
<schema name="mycatdb" checkSQLschema="true" sqlMaxLimit="100">
			<table name="TB_TEST" primaryKey="id" dataNode="dn1,dn2,dn3" />
</schema>
```
1. name标识逻辑库的名字，可以自己定义任何合法的字符串。
2. checkSQLschema取值为true/false如果设置为true时，如果我们执行了语句为"select *from mycatdb.tb_test;",则MyCat会把自动把schema字符
去掉，把SQL 修改为"select * from tb_test",可以避免SQL发生到后端数据库时，报table不存在的异常。不过当我们在编写SQL语句时, 指定了一个不
存在schema, MyCat是不会帮我们自动去除的 ,这个时候数据库就会报错, 所以在编写SQL语句时,最好不要加逻辑库的库名, 直接查询表即可。
3.sqlMaxLimit
当盖属性设置为某个数值时，每次执行的SQL语句如果没有加上limit语句，Mycat也会自动加上limit语句后面加上对应的数值。也就是说，如果我们设置概知为
100，则执行select *from tb_test 于与 select * from tb_test limit 100 是相同的效果 。所以在正常的使用中, 建立设置该值 , 这样就可以避免每次有过多的数据返回。

* 子标签table
```xml
<table name="TB_TEST" dataNode="dn1,dn2,dn3" rule="auto-1 sharding-long" />
```
|属性名称|值|含义|
| ---- | ---- | ---- |
|name|string|定义逻辑表的表名 , 在该逻辑库下必须唯一。|
|dataNode|string|定义的逻辑表所属的dataNode , 该属性需要与dataNode标签中的name属性的值对应。 如果一张表拆分的数据，存储在多个数据节点上，多个节点的名称使用","分隔 。|
|rule|string|该属性用于指定逻辑表的分片规则的名字, 规则的名字是在rule.xml文件中定义的, 必须与tableRule标签中name属性对应。|
|ruleRquired|boolean|盖属性用于指定表是否绑定分片规则，如果配置为true，但是没有具体的rule，程序会报错|
|primaryKey|string|逻辑表的主键|
|type|string|该属性定义了逻辑表的类型，目前逻辑鸟只有全局表和普通表全局表：type的值是 global , 代表 全局表 。普通表：无s|
|autoIncrement|boolean|mysql对非自增长主键，使用last_insert_id() 是不会返回结果的，只会返回0。所以，只有定义了自增长主键的表，才可以用last_insert_id()返回主键值。 mycat提供了自增长主键功能，但是对应的mysql节点上数据表，没有auto_increment,那么在mycat层调用last_insert_id()也是不会返回结果的。如果使用这个功能， 则最好配合数据库模式的全局序列。使用 autoIncrement="true" 指定该表使用自增长主键,这样MyCat才不会抛出 "分片键找不到" 的异常。 autoIncrement的默认值为false|
|needAddLimit|boolean|指定表是否需要自动在每个语句的后面加上limit限制, 默认为true。|


* dataNode 标签
dataNode标签中定义了MyCat中的数据节点, 也就是我们通常说的数据分片。一个dataNode标签就
是一个独立的数据分片。
具体的属性 ：

|属性|含义|描述|
| ---- | ---- | ---- |
|name|数据节点的名称|需要唯一 ; 在table标签中会引用这个名字, 标识表与分片的对应关系|
|dataHost|数据库实例主机名称|引用自 dataHost 标签中name属性|
|database|定义分片所属的数据库||



* dataHost 标签

该标签在MyCat逻辑库中作为底层标签存在, 直接定义了具体的数据库实例、读写分离、心跳语句。
```xml
	<dataHost name="localhost1" maxCon="1000" minCon="10" balance="0"
			  writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<!-- can have multi write hosts -->
		<writeHost host="hostM1" url="192.168.0.104:3307" user="root"
				   password="root@123">
		</writeHost>
		<!-- <writeHost host="hostM2" url="localhost:3316" user="root" password="123456"/> -->
	</dataHost>
```

## MyCat分片

### 垂直分片
schema.xml配置如下：
```xml
<?xml version="1.0"?>
<!DOCTYPE mycat:schema SYSTEM "schema.dtd">
<mycat:schema xmlns:mycat="http://io.mycat/">
	<schema name="mycatdb" checkSQLschema="true" sqlMaxLimit="100">
		<table name="tb_areas_city" dataNode="dn1" primaryKey="id" />
		<table name="tb_areas_provinces" dataNode="dn1" primaryKey="id" />
		<table name="tb_areas_region" dataNode="dn1" primaryKey="id" />
		<table name="tb_user" dataNode="dn1" primaryKey="id" />
		<table name="tb_user_address" dataNode="dn1" primaryKey="id" />
		<table name="tb_goods_base" dataNode="dn2" primaryKey="id" />
		<table name="tb_goods_desc" dataNode="dn2" primaryKey="goods_id" />
		<table name="tb_goods_item_cat" dataNode="dn2" primaryKey="id" />
		<table name="tb_order_item" dataNode="dn3" primaryKey="id" />
		<table name="tb_order_master" dataNode="dn3" primaryKey="order_id" />
		<table name="tb_order_pay_log" dataNode="dn3" primaryKey="out_trade_no"/>
	</schema>
		<dataNode name="dn1" dataHost="dh1" database="user_db" />
		<dataNode name="dn2" dataHost="dh1" database="goods_db" />
		<dataNode name="dn3" dataHost="dh1" database="order_db" />
	<dataHost name="dh1" maxCon="1000" minCon="10" balance="0"
			  writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM1" url="192.168.0.104:3307" user="root"
				   password="root@123">
		</writeHost>
	</dataHost>
</mycat:schema>
```
SQL如下:
[user表](./files\user.sql)
[order表](./files\order.sql)
[goods表](./files\goods.sql)

完成上述的配置后重启mycat,重新登录mycat可以执行sql

注意：
对于需要跨库执行的join的并且关联查询的表是不大的表，可以设置称为全局表
 ```xml
		<table name="tb_user" dataNode="dn1" primaryKey="id" />
		<table name="tb_user_address" dataNode="dn1" primaryKey="id" />
		<table name="tb_goods_base" dataNode="dn2" primaryKey="id" />
		<table name="tb_goods_desc" dataNode="dn2" primaryKey="goods_id" />
		<table name="tb_goods_item_cat" dataNode="dn2" primaryKey="id" />
		<table name="tb_order_item" dataNode="dn3" primaryKey="id" />
		<table name="tb_order_master" dataNode="dn3" primaryKey="order_id" />
		<table name="tb_order_pay_log" dataNode="dn3" primaryKey="out_trade_no"/>
	
		<table name="tb_areas_city" dataNode="dn1,dn2,dn3" primaryKey="id" type="global"/>
		<table name="tb_areas_provinces" dataNode="dn1,dn2,dn3" primaryKey="id" type="global"/>
		<table name="tb_areas_region" dataNode="dn1,dn2,dn3" primaryKey="id" type="global"/>
	
	</schema>
```
除了mycat的配置schema.xml配置需要修改之外，同时在user_db,order_db,goods_db也需要有tb_areas_city、
tb_areas_provinces、tb_areas_region表


![关联查询](./files\全局表解决关联查询.PNG)

### 水平分片和取模分片
schema.xml的修改如下:
```xml
<?xml version="1.0"?>
<!DOCTYPE mycat:schema SYSTEM "schema.dtd">
<mycat:schema xmlns:mycat="http://io.mycat/">
	
	<schema name="log_db" checkSQLschema="false" sqlMaxLimit="100">
	<table name="tb_log" dataNode="dn1,dn2,dn3" primaryKey="id" rule="mod-long"/>
	</schema>
		<dataNode name="dn1" dataHost="dh1" database="log_db" />
		<dataNode name="dn2" dataHost="dh1" database="log_db2" />
		<dataNode name="dn3" dataHost="dh1" database="log_db3" />
		
	<dataHost name="dh1" maxCon="1000" minCon="10" balance="0"
			  writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<!-- can have multi write hosts -->
		<writeHost host="hostM1" url="192.168.1.104:3307" user="test"
				   password="test@123">
		</writeHost>
	</dataHost>
</mycat:schema>
```
server.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mycat:server SYSTEM "server.dtd">
<mycat:server xmlns:mycat="http://io.mycat/">
	<system>
	<property name="nonePasswordLogin">0</property> <!-- 0为需要密码登陆、1为不需要密码登陆 ,默认为0，设置为1则需要指定默认账户-->
	<property name="useHandshakeV10">1</property>
	<property name="useSqlStat">1</property>  <!-- 1为开启实时统计、0为关闭 -->
	<property name="useGlobleTableCheck">0</property>  <!-- 1为开启全加班一致性检测、0为关闭 -->
		<property name="sqlExecuteTimeout">300</property>  <!-- SQL 执行超时 单位:秒-->
		<property name="sequnceHandlerType">2</property>
		<property name="sequnceHandlerPattern">(?:(\s*next\s+value\s+for\s*MYCATSEQ_(\w+))(,|\)|\s)*)+</property>
	<property name="subqueryRelationshipCheck">false</property> <!-- 子查询中存在关联查询的情况下,检查关联字段中是否有分片字段 .默认 false -->
		<property name="processorBufferPoolType">0</property>
		<property name="handleDistributedTransactions">0</property>
		<property name="useOffHeapForMerge">0</property>
        <property name="memoryPageSize">64k</property>
		<property name="spillsFileBufferSize">1k</property>
		<property name="useStreamOutput">0</property>
		<property name="systemReserveMemorySize">384m</property>
		<property name="useZKSwitch">false</property>
		<property name="strictTxIsolation">false</property>
		<property name="useZKSwitch">true</property>
	</system>
	<user name="root" defaultAccount="true">
		<property name="password">kOdSW+qrXfdYoa+qhNBZVARSxlhCZFS+PwP2NTzKYtb8rOntlZa+XFfxUPgE+3spkNpZgiNlDonoI1zz6OGwcg==</property>
		<property name="schemas">log_db</property>
		<property name="usingDecrypt">1</property>
	</user>
	<user name="user">
		<property name="password">test123</property>
		<property name="schemas">log_db</property>
		<property name="readOnly">true</property>
	</user>
</mycat:server>
```
执行以下的脚本重启mycat
```shell script
./bin/mycat restart
```
登录mycat并切换到log_db
```sql
CREATE TABLE `tb_log` (
`id` bigint(20) NOT NULL COMMENT 'ID',
`model_name` varchar(200) DEFAULT NULL COMMENT '模块名',
`model_value` varchar(200) DEFAULT NULL COMMENT '模块值',
`return_value` varchar(200) DEFAULT NULL COMMENT '返回值',
`return_class` varchar(200) DEFAULT NULL COMMENT '返回值类型',
`operate_user` varchar(20) DEFAULT NULL COMMENT '操作用户',
`operate_time` varchar(20) DEFAULT NULL COMMENT '操作时间',
`param_and_value` varchar(500) DEFAULT NULL COMMENT '请求参数名及参数值',
`operate_class` varchar(200) DEFAULT NULL COMMENT '操作类',
`operate_method` varchar(200) DEFAULT NULL COMMENT '操作方法',
`cost_time` bigint(20) DEFAULT NULL COMMENT '执行方法耗时, 单位 ms',
`source` int(1) DEFAULT NULL COMMENT '来源 : 1 PC , 2 Android , 3 IOS',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- 添加数据的脚本
INSERT INTO `tb_log` (`id`, `model_name`, `model_value`, `return_value`,`return_class`, `operate_user`, `operate_time`, `param_and_value`,`operate_class`, `operate_method`, `cost_time`，`source`)VALUES('1','user','insert','success','java.lang.String','10001','2020-02-2618:12:28','{\"age\":\"20\",\"name\":\"Tom\",\"gender\":\"1\"}','cn.itcast.controller.UserController','insert','10',1);
INSERT INTO `tb_log` (`id`, `model_name`, `model_value`, `return_value`,`return_class`, `operate_user`, `operate_time`, `param_and_value`,`operate_class`, `operate_method`, `cost_time`，`source`)VALUES('2','user','insert','success','java.lang.String','10001','2020-02-2618:12:27','{\"age\":\"20\",\"name\":\"Tom\",\"gender\":\"1\"}','cn.itcast.controller.UserController','insert','23',1);
INSERT INTO `tb_log` (`id`, `model_name`, `model_value`, `return_value`,`return_class`, `operate_user`, `operate_time`, `param_and_value`,`operate_class`, `operate_method`, `cost_time`，`source`)VALUES('3','user','update','success','java.lang.String','10001','2020-02-2618:16:45','{\"age\":\"20\",\"name\":\"Tom\",\"gender\":\"1\"}','cn.itcast.controller.UserController','update','34',1);
INSERT INTO `tb_log` (`id`, `model_name`, `model_value`, `return_value`,`return_class`, `operate_user`, `operate_time`, `param_and_value`,`operate_class`, `operate_method`, `cost_time`，`source`)VALUES('4','user','update','success','java.lang.String','10001','2020-02-2618:16:45','{\"age\":\"20\",\"name\":\"Tom\",\"gender\":\"1\"}','cn.itcast.controller.UserController','update','13',2);
INSERT INTO `tb_log` (`id`, `model_name`, `model_value`, `return_value`,`return_class`, `operate_user`, `operate_time`, `param_and_value`,`operate_class`, `operate_method`, `cost_time`，`source`)VALUES('5','user','insert','success','java.lang.String','10001','2020-02-2618:30:31','{\"age\":\"200\",\"name\":\"TomCat\",\"gender\":\"0\"}','cn.itcast.controller.UserController','insert','29',3);
INSERT INTO `tb_log` (`id`, `model_name`, `model_value`, `return_value`,`return_class`, `operate_user`, `operate_time`, `param_and_value`,`operate_class`, `operate_method`, `cost_time`，`source`)VALUES('6','user','find','success','java.lang.String','10001','2020-02-2618:30:31','{\"age\":\"200\",\"name\":\"TomCat\",\"gender\":\"0\"}','cn.itcast.controller.UserController','find','29',2);
-- 查询每个物理结点的数据脚本
 /*!mycat:dataNode=dn1*/select * from tb_log;
 /*!mycat:dataNode=dn2*/select * from tb_log;
 /*!mycat:dataNode=dn3*/select * from tb_log;
```
执行脚本的效果：
![执行脚本的效果](./files\水平分片和取模规则的使用.PNG)

* 范围分片
根据指定的字段及其配置的范围于数据结点的对应的情况，来决定该数据属于哪一个分片，配置如下:
```xml
	
	<schema name="log_db" checkSQLschema="false" sqlMaxLimit="100">
	<!-- 范围分片 -->
	<table name="partition_rang_tb" dataNode="dn1,dn2,dn3" primaryKey="id" rule="auto-sharding-long"/>
	</schema>


<tableRule name="auto-sharding-long">
		<rule>
			<columns>id</columns>
			<algorithm>rang-long</algorithm>
		</rule>
	</tableRule>

	<function name="rang-long"
		class="io.mycat.route.function.AutoPartitionByLong">
		<property name="mapFile">autopartition-long.txt</property>
	</function>
```

|属性|描述|
| ---- | ---- |
|columns|标识将要分片的表字段|
|algorithm|指定分片函数于function的对应关系|
|class|指定该分片算法对应的类|
|mapFile|对应的外部配置文件|
|type|默认值为0;0标识Integer,1标识string|
|defaultNode|默认结点，默认系欸点的所用:美剧分片时，如果碰到不识别的枚举值，就让它路由到默认结点，如果没有默认值，碰到不知别的则报错|


autopartition-long.txt配置如下：
```text
# range start-end ,data node index
# K=1000,M=10000.
0-500M=0 # 0-500w范围在1号分片
500M-1000M=1 #500w-1000w的范围的在2号分片
1000M-1500M=2 #1000w-1500w的范围在3号分片
```

执行的脚本如下:
```sql
mysql> CREATE TABLE `partition_rang_tb` (
    -> id bigint(20) NOT NULL COMMENT 'ID',
    -> operateuser varchar(200) DEFAULT NULL COMMENT '姓名',
    -> operation int(2) DEFAULT NULL COMMENT '1: insert, 2: delete, 3: update , 4:
    '> select',
    -> PRIMARY KEY (`id`)
    -> ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
mysql>
mysql> insert into partition_rang_tb(id,operateuser ,operation) values(1,'Tom',1);
mysql> insert into partition_rang_tb(id,operateuser ,operation) values(2,'Cat',2);
mysql> insert into partition_rang_tb(id,operateuser ,operation) values(3,'Rose',3);
mysql> insert into partition_rang_tb(id,operateuser ,operation) values(4,'Coco',2);
mysql> insert into partition_rang_tb(id,operateuser ,operation) values(5,'Lily',1);
mysql> /*!mycat:dataNode=dn1*/select *from partition_rang_tb;
+----+-------------+-----------+
| id | operateuser | operation |
+----+-------------+-----------+
|  1 | Tom         |         1 |
|  2 | Cat         |         2 |
|  3 | Rose        |         3 |
|  4 | Coco        |         2 |
|  5 | Lily        |         1 |
+----+-------------+-----------+
5 rows in set (0.01 sec)

mysql> /*!mycat:dataNode=dn2*/select *from partition_rang_tb;
Empty set (0.00 sec)

mysql> /*!mycat:dataNode=dn3*/select *from partition_rang_tb;
Empty set (0.01 sec)
```
由上图可以知道，插入的数据都在数据结点1里面


![范围分片](./files\范围分片-1.PNG)
















































