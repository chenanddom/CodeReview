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
server.xml

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
