# MyCat分片

## 垂直分片
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

## 分片规则

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

*　枚举分片
通过在配置文件中配置可能的枚举值，指定数据分布到不同的数据节点上，本规则适用于按照省份或者状态拆分数据等业务，配置如下:
rule.xml
```xml
	<tableRule name="sharding-by-enum-status">
		<rule>
			<columns>status</columns>
			<algorithm>hash-int</algorithm>
		</rule>
	</tableRule>
	<function name="hash-int"
		class="io.mycat.route.function.PartitionByFileMap">
		<property name="mapFile">partition-hash-int.txt</property>
	</function>
```

[枚举值和分片的映射文件](./files\partition-hash-int.txt)
```text
1=0 #枚举值为1就在分片1
2=1 #枚举值为2就在分片2
3=2 #枚举值为3就在分片3
```

执行的sql脚本
```sql
mysql> CREATE TABLE `tb_user` (
    -> id bigint(20) NOT NULL COMMENT 'ID',
    -> username varchar(200) DEFAULT NULL COMMENT '姓名',
    -> status int(2) DEFAULT '1' COMMENT '1: 未启用, 2: 已启用, 3: 已关闭',
    -> PRIMARY KEY (`id`)
    -> ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
Query OK, 0 rows affected (0.25 sec)

mysql> show tables;
+-------------------+
| Tables in log_db  |
+-------------------+
| partition_rang_tb |
| tb_log            |
| tb_user           |
+-------------------+
3 rows in set (0.00 sec)

mysql> insert into tb_user (id,username ,status) values(1,'Tom',1);
Query OK, 1 row affected (0.07 sec)

mysql> insert into tb_user (id,username ,status) values(2,'Cat',2);
Query OK, 1 row affected (0.01 sec)

mysql> insert into tb_user (id,username ,status) values(3,'Rose',3);
Query OK, 1 row affected (0.01 sec)

mysql> insert into tb_user (id,username ,status) values(4,'Coco',2);
Query OK, 1 row affected (0.12 sec)

mysql> insert into tb_user (id,username ,status) values(5,'Lily',1);
Query OK, 1 row affected (0.01 sec)

mysql> select *from tb_user;
+----+----------+--------+
| id | username | status |
+----+----------+--------+
|  1 | Tom      |      1 |
|  5 | Lily     |      1 |
|  2 | Cat      |      2 |
|  4 | Coco     |      2 |
|  3 | Rose     |      3 |
+----+----------+--------+
5 rows in set (0.04 sec)

mysql> /*!mycat:dataNode=dn1*/select *from tb_user;
+----+----------+--------+
| id | username | status |
+----+----------+--------+
|  1 | Tom      |      1 |
|  5 | Lily     |      1 |
+----+----------+--------+
2 rows in set (0.01 sec)

mysql> /*!mycat:dataNode=dn2*/select *from tb_user;
+----+----------+--------+
| id | username | status |
+----+----------+--------+
|  2 | Cat      |      2 |
|  4 | Coco     |      2 |
+----+----------+--------+
2 rows in set (0.00 sec)

mysql> /*!mycat:dataNode=dn3*/select *from tb_user;
+----+----------+--------+
| id | username | status |
+----+----------+--------+
|  3 | Rose     |      3 |
+----+----------+--------+
1 row in set (0.00 sec)
```
* 范围求模算法
该算法为先进行范围分片，计算出分片组，在进行组内求模。
优点：综合了范围分片和求模分片的优点。分片组内使用求模可以保证组内的数据分布比较均匀，分盘组之间采用了范围分片可以兼顾范围分片的特点。
缺点:存在数据范围时固定值(非递增值)时，存在不方便扩展的情况，例如dataNode Group size从2扩展为4时，需要进行数据迁移才可以万恒；
![范围求模算法](./files\范围求模分片.PNG)

在上述配置文件中, 等号前面的范围代表一个分片组 , 等号后面的数字代表该分片组所拥有的分片数
量;

rule.xml
```xml
<tableRule name="auto-sharding-rang-mod">
	<rule>
		<columns>id</columns>
		<algorithm>rang-mod</algorithm>
	</rule>
</tableRule>

<function name="rang-mod" class="io.mycat.route.function.PartitionByRangeMod">
  <property name="mapFile">partition-range-mod.txt</property>
</function>
```

[范围求模](./files\partition-range-mod.txt)
```text
0-500M=1 # 0~500万存放在第一个节点
500M1-2000M=2 # 500万零1粗放在后面两个结点，取模存储
```
schema.xml文件配置
```xml
<table name="tb_stu" dataNode="dn1,dn2,dn3" rule="auto-sharding-rang-mod"/>
```
sql脚本的
```sql
mysql> CREATE TABLE `tb_stu` (
    -> id bigint(20) NOT NULL COMMENT 'ID',
    -> username varchar(200) DEFAULT NULL COMMENT '姓名',
    -> status int(2) DEFAULT '1' COMMENT '1: 未启用, 2: 已启用, 3: 已关闭',
    -> PRIMARY KEY (`id`)
    -> ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
mysql> insert into tb_stu (id,username ,status) values(1,'Tom',1);
mysql> insert into tb_stu (id,username ,status) values(2,'Cat',2);
mysql> insert into tb_stu (id,username ,status) values(3,'Rose',3);
mysql> insert into tb_stu (id,username ,status) values(4,'Coco',2);
mysql> insert into tb_stu (id,username ,status) values(5,'Lily',1);
mysql> insert into tb_stu (id,username ,status) values(5000001,'Roce',1);
mysql> insert into tb_stu (id,username ,status) values(5000002,'Jexi',2);
mysql> insert into tb_stu (id,username ,status) values(5000003,'Mini',1);
```
执行结果

![执行结果](./files\范围取模分片-1.PNG)

* 固定分片hash算法
 该算法类似于十进制的求模算法，但是为二级制的操作，例如，去id的二进制低10位与1111111111进行&运算
 最小值
0 0 0 0 0 0 0 0 0 0
&
1 1 1 1 1 1 1 1 1 1
———————————————————————
0 0 0 0 0 0 0 0 0 0     =0

最大值
1 1 1 1 1 1 1 1 1 1
& 
1 1 1 1 1 1 1 1 1 1 
———————————————————————
1 1 1 1 1 1 1 1 1 1    =1023
优点：这种策略比较灵活，可以均匀的分配也可以非均匀的分配，各个节点的分配比例和容量大小由partitionCount和partitionLength两个参数决定
缺点： 和取模分片一样扩容需要进行数据的迁移

rule.xml的配置如下:
```xml
	<tableRule name="sharding-by-long-hash">
		<rule>
			<columns>id</columns>
			<algorithm>fixed-hash</algorithm>
		</rule>
	</tableRule>

	<function name="fixed-hash" class="io.mycat.route.function.PartitionByLong">
		<property name="partitionCount">1,2</property>
		<property name="partitionLength">256,512</property>
	</function
<!--在示例中配置的分片策略，希望将数据水平分成3份，前两份各占 25%，第三份占 50%。-->

```
约束 :
1). 分片长度 : 默认最大2^10 , 为 1024 ;
2). count, length的数组长度必须是一致的 ;
3). 两组数据的对应情况: (partitionCount[0]partitionLength[0])=
(partitionCount[1]partitionLength[1])
以上分为三个分区:0-255,256-511,512-1023

schema.xml的配置如下:
```xml
<?xml version="1.0"?>
<!DOCTYPE mycat:schema SYSTEM "schema.dtd">
<mycat:schema xmlns:mycat="http://io.mycat/">
	<schema name="log_db" checkSQLschema="false" sqlMaxLimit="100">
	<table name="tb_log" dataNode="dn1,dn2,dn3" primaryKey="id" rule="mod-long"/>
	<table name="partition_rang_tb" dataNode="dn1,dn2,dn3" primaryKey="id" rule="auto-sharding-long"/>
	<table name="tb_user" dataNode="dn1,dn2,dn3" rule="sharding-by-enum-status"/>
	<table name="tb_stu" dataNode="dn1,dn2,dn3" rule="auto-sharding-rang-mod"/>
	<table name="tb_brand" dataNode="dn1,dn2,dn3" rule="sharding-by-long-hash"/>
	</schema>
		<dataNode name="dn1" dataHost="dh1" database="log_db" />
		<dataNode name="dn2" dataHost="dh1" database="log_db2" />
		<dataNode name="dn3" dataHost="dh1" database="log_db3" />
	<dataHost name="dh1" maxCon="1000" minCon="10" balance="0"
			  writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM1" url="192.168.0.104:3307" user="root"
				   password="root@123">
		</writeHost>
	</dataHost>
</mycat:schema>
```
```sql
mysql> CREATE TABLE `tb_brand` (
    -> id int(11) NOT NULL COMMENT 'ID',
    -> name varchar(200) DEFAULT NULL COMMENT '名称',
    -> firstChar char(1) COMMENT '首字母',
    -> PRIMARY KEY (`id`)
    -> ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
Query OK, 0 rows affected (0.15 sec)

mysql> insert into tb_brand (id,name ,firstChar) values(1,'七匹狼','Q');
mysql> insert into tb_brand (id,name ,firstChar) values(529,'八匹狼','B');
mysql>  insert into tb_brand (id,name ,firstChar) values(1203,'九匹狼','J');
mysql> insert into tb_brand (id,name ,firstChar) values(1205,'十匹狼','S');
mysql> insert into tb_brand (id,name ,firstChar) values(1719,'六匹狼','L');
mysql> select *from tb_brand;
+------+--------+-----------+
| id   | name   | firstChar |
+------+--------+-----------+
|    1 | 七匹狼 | Q         |
| 1203 | 九匹狼 | J         |
| 1205 | 十匹狼 | S         |
|  529 | 八匹狼 | B         |
| 1719 | 六匹狼 | L         |
+------+--------+-----------+
5 rows in set (0.11 sec)
mysql> /*!mycat:dataNode=dn1*/select *from tb_brand;
+------+--------+-----------+
| id   | name   | firstChar |
+------+--------+-----------+
|    1 | 七匹狼 | Q         |
| 1203 | 九匹狼 | J         |
| 1205 | 十匹狼 | S         |
+------+--------+-----------+
3 rows in set (0.02 sec)
mysql> /*!mycat:dataNode=dn2*/select *from tb_brand;
Empty set (0.00 sec)
mysql> /*!mycat:dataNode=dn3*/select *from tb_brand;
+------+--------+-----------+
| id   | name   | firstChar |
+------+--------+-----------+
|  529 | 八匹狼 | B         |
| 1719 | 六匹狼 | L         |
+------+--------+-----------+
2 rows in set (0.00 sec)
```
* 取模范围算法
该算法先进行取模，然后根据取模值所属的范围进行分片。
优点：可以自主决定取模后数据的结点分布
缺点：dataNode划分节点时实现建好的，需要扩展就比较麻烦
rule.xml的配置如下:
```xml
<tableRule name="sharding-by-pattern">
		<rule>
		<columns>id</columns>
		<algorithm>sharding-by-pattern</algorithm>
		</rule>
	</tableRule>
	<function name="sharding-by-pattern" class="io.mycat.route.function.PartitionByPattern">
		<property name="mapFile">partition-pattern.txt</property>
		<property name="defaultNode">0</property>
		<property name="patternValue">96</property>
	</function>
```

|属性|描述|
| ---- | ---- |
|columns|标识将要分片的表字段|
|algorithm|指定分片函数与function的对应关系|
|class|指定分片算法的类|
|mapFile|对应的外部配置文件|
|defaultNode|默认结点，如果id不是数字无法求模，将分配到defaultNode上|
|patternValue|求模基数|

schema.xml
```xml
	<table name="tb_mod_range" dataNode="dn1,dn2,dn3" rule="sharding-by-pattern"/>
```
重启mycat即可生效了
sql脚本
```sql

mysql> CREATE TABLE `tb_mod_range` (
    -> id int(11) NOT NULL COMMENT 'ID',
    -> name varchar(200) DEFAULT NULL COMMENT '名称',
    -> PRIMARY KEY (`id`)
    -> ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
mysql> insert into tb_mod_range (id,name) values(1,'Test1');
mysql> insert into tb_mod_range (id,name) values(2,'Test2');
mysql> insert into tb_mod_range (id,name) values(3,'Test3');
mysql> insert into tb_mod_range (id,name) values(4,'Test4');
mysql> insert into tb_mod_range (id,name) values(5,'Test5');
mysql> show tables;
+-------------------+
| Tables in log_db  |
+-------------------+
| partition_rang_tb |
| tb_brand          |
| tb_log            |
| tb_mod_range      |
| tb_stu            |
| tb_user           |
+-------------------+
6 rows in set (0.01 sec)
mysql> select *from tb_mod_range;
+----+-------+
| id | name  |
+----+-------+
|  1 | Test1 |
|  2 | Test2 |
|  3 | Test3 |
|  4 | Test4 |
|  5 | Test5 |
+----+-------+
5 rows in set (0.04 sec)

mysql> /*!mycat:dataNode=dn1*/select *from tn_mod_range;
ERROR 1146 (HY000): Table 'log_db.tn_mod_range' doesn't exist
mysql> /*!mycat:dataNode=dn1*/select *from tb_mod_range;
+----+-------+
| id | name  |
+----+-------+
|  1 | Test1 |
|  2 | Test2 |
|  3 | Test3 |
|  4 | Test4 |
|  5 | Test5 |
+----+-------+
5 rows in set (0.00 sec)

mysql> /*!mycat:dataNode=dn2*/select *from tb_mod_range;
Empty set (0.01 sec)

mysql> /*!mycat:dataNode=dn3*/select *from tb_mod_range;
Empty set (0.00 sec)

mysql> insert into tb_mod_range (id,name) values(97,'Test97');
Query OK, 1 row affected (0.01 sec)

mysql> insert into tb_mod_range (id,name) values(98,'Test97');
Query OK, 1 row affected (0.01 sec)

mysql> /*!mycat:dataNode=dn3*/select *from tb_mod_range;
Empty set (0.00 sec)

mysql> /*!mycat:dataNode=dn2*/select *from tb_mod_range;
Empty set (0.00 sec)

mysql> /*!mycat:dataNode=dn1*/select *from tb_mod_range;
+----+--------+
| id | name   |
+----+--------+
|  1 | Test1  |
|  2 | Test2  |
|  3 | Test3  |
|  4 | Test4  |
|  5 | Test5  |
| 97 | Test97 |
| 98 | Test97 |
+----+--------+
7 rows in set (0.00 sec)

mysql> insert into tb_mod_range (id,name) values(99,'Test99');
Query OK, 1 row affected (0.00 sec)

mysql> /*!mycat:dataNode=dn1*/select *from tb_mod_range;
+----+--------+
| id | name   |
+----+--------+
|  1 | Test1  |
|  2 | Test2  |
|  3 | Test3  |
|  4 | Test4  |
|  5 | Test5  |
| 97 | Test97 |
| 98 | Test97 |
| 99 | Test99 |
+----+--------+
8 rows in set (0.00 sec)

mysql> insert into tb_mod_range (id,name) values(100,'Test100');
Query OK, 1 row affected (0.01 sec)

mysql> /*!mycat:dataNode=dn1*/select *from tb_mod_range;
+-----+---------+
| id  | name    |
+-----+---------+
|   1 | Test1   |
|   2 | Test2   |
|   3 | Test3   |
|   4 | Test4   |
|   5 | Test5   |
|  97 | Test97  |
|  98 | Test97  |
|  99 | Test99  |
| 100 | Test100 |
+-----+---------+
9 rows in set (0.00 sec)

mysql> insert into tb_mod_range (id,name) values(199,'Test199');
Query OK, 1 row affected (0.01 sec)

mysql> /*!mycat:dataNode=dn1*/select *from tb_mod_range;
+-----+---------+
| id  | name    |
+-----+---------+
|   1 | Test1   |
|   2 | Test2   |
|   3 | Test3   |
|   4 | Test4   |
|   5 | Test5   |
|  97 | Test97  |
|  98 | Test97  |
|  99 | Test99  |
| 100 | Test100 |
| 199 | Test199 |
+-----+---------+
10 rows in set (0.12 sec)

mysql> insert into tb_mod_range (id,name) values(10001,'Test10000');
Query OK, 1 row affected (0.01 sec)

mysql> /*!mycat:dataNode=dn1*/select *from tb_mod_range;
+-------+-----------+
| id    | name      |
+-------+-----------+
|     1 | Test1     |
|     2 | Test2     |
|     3 | Test3     |
|     4 | Test4     |
|     5 | Test5     |
|    97 | Test97    |
|    98 | Test97    |
|    99 | Test99    |
|   100 | Test100   |
|   199 | Test199   |
| 10001 | Test10000 |
+-------+-----------+
11 rows in set (0.00 sec)

mysql> insert into tb_mod_range (id,name) values(92,'Test92');
Query OK, 1 row affected (0.01 sec)

mysql> /*!mycat:dataNode=dn1*/select *from tb_mod_range;
+-------+-----------+
| id    | name      |
+-------+-----------+
|     1 | Test1     |
|     2 | Test2     |
|     3 | Test3     |
|     4 | Test4     |
|     5 | Test5     |
|    97 | Test97    |
|    98 | Test97    |
|    99 | Test99    |
|   100 | Test100   |
|   199 | Test199   |
| 10001 | Test10000 |
+-------+-----------+
11 rows in set (0.00 sec)

mysql> /*!mycat:dataNode=dn3*/select *from tb_mod_range;
+----+--------+
| id | name   |
+----+--------+
| 92 | Test92 |
+----+--------+
1 row in set (0.00 sec)
```

* 字符串hash求模分片算法
与求模算法类似，该算法支持数值，符好，字母取模，首截取长度为prefixLength的子串，在对子串中每一个字符的ASCII码求和，然后对求和值进行取模运算。
(sum%patternValue)，就可以计算出子串的分片数
优点：可以自主决定取模后数据的节点分布
缺点:dataNode划分系欸但是实现建好的，需要扩展时比较麻烦

rule.xml
```xml
	
	<tableRule name="sharding-by-prefixpattern2">
		<rule>
		<columns>username</columns>
		<algorithm>sharding-by-prefixpattern</algorithm>
		</rule>
	</tableRule>
	
	
	<function name="sharding-by-prefixpattern" class="io.mycat.route.function.PartitionByPrefixPattern">
		<property name="mapFile">partition-prefixpattern.txt</property>
		<property name="prefixLength">5</property>
		<property name="patternValue">96</property>
	</function>
```

schema.xml
```xml
	<table name="tb_app" dataNode="dn1,dn2,dn3" rule="sharding-by-prefixpattern2"/>
```

sql
```sql
CREATE TABLE `tb_app` (
id varchar(10) NOT NULL COMMENT 'ID',
name varchar(200) DEFAULT NULL COMMENT '名称',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into tb_app (id,name) values('00-00001','Testx00001');
insert into tb_app (id,name) values('01-00001','Test100001');
insert into tb_app (id,name) values('01-00002','Test200001');
```



* 应用指定算法
由运行阶段应用自主决定路由到那个分片，直接根据字符子串(必须时数字)计算分片号，配置如下：
rule.xml
```xml
	<function name="sharding-by-substring" class="io.mycat.route.function.PartitionDirectBySubString">
		<!--字符串起始索引-->
		<property name="startIndex">0</property> <!-- zero-based -->
		<!--字符长度-->
		<property name="size">2</property>
		<!--分片的数量-->
		<property name="partitionCount">3</property>
		<property name="defaultPartition">0</property>
	</function>
	<function name="sharding-by-prefixpattern" class="io.mycat.route.function.PartitionByPrefixPattern">
		<property name="mapFile">partition-prefixpattern.txt</property>
		<property name="prefixLength">5</property>
		<!--取模基础值-->
		<property name="patternValue">96</property>
	</function>
```
schema.xml
```xml
	<table name="tb_app" dataNode="dn1,dn2,dn3" rule="sharding-by-substring"/>
```

重启mycat然后到log_db数据库就可以查看到逻辑表tb_app

```sql
mysql> CREATE TABLE `tb_app` (
    -> id varchar(10) NOT NULL COMMENT 'ID',
    -> name varchar(200) DEFAULT NULL COMMENT '名称',
    -> PRIMARY KEY (`id`)
    -> ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
mysql> insert into tb_app (id,name) values('00-00001','Testx00001');
mysql> insert into tb_app (id,name) values('01-00001','Test100001');
mysql> insert into tb_app (id,name) values('01-00002','Test200001');
mysql> insert into tb_app (id,name) values('02-00001','Test300001');
mysql> insert into tb_app (id,name) values('02-00002','TesT400001');
mysql> /**/select *from tb_app;
+----------+------------+
| id       | name       |
+----------+------------+
| 00-00001 | Testx00001 |
| 01-00001 | Test100001 |
| 01-00002 | Test200001 |
| 02-00001 | Test300001 |
| 02-00002 | TesT400001 |
+----------+------------+
mysql> /*!mycat:dataNode=dn1*/select *from tb_app;
+----------+------------+
| id       | name       |
+----------+------------+
| 00-00001 | Testx00001 |
+----------+------------+
mysql> /*!mycat:dataNode=dn2*/select *from tb_app;
+----------+------------+
| id       | name       |
+----------+------------+
| 01-00001 | Test100001 |
| 01-00002 | Test200001 |
+----------+------------+
mysql> /*!mycat:dataNode=dn3*/select *from tb_app;
+----------+------------+
| id       | name       |
+----------+------------+
| 02-00001 | Test300001 |
| 02-00002 | TesT400001 |
+----------+------------+
```
* 字符串hash解析算法分片
截取字符串中的指定位置的子字符串，进行hash算法，算出分片，配置如下:
rule.xml
```xml
<tableRule name="sharding-by-stringhash">
		<rule>
			<columns>name</columns>
			<algorithm>sharding-by-stringhash2</algorithm>
		</rule>
	</tableRule>
	<function name="sharding-by-stringhash2" class="io.mycat.route.function.PartitionByString">
			<!--hash求模基础;length*count=1024(出于性能考虑)-->
			<property name="partitionLength">512</property> <!-- zero-based -->
			<!--分区数-->
			<property name="partitionCount">2</property>
			<!--hash运算位，根据子字符串的hash运算；0代表string.length()，-1代表str.length()-1，大于0只代表数字自身；可以理解为substring(start,end),start为0则只标识0-->
			<property name="hashSlice">0:2</property>
	</function>
	
```
schema.xml
```xml
	<table name="tb_strhash" dataNode="dn1,dn2,dn3" rule="sharding-by-stringhash"/>
```


```sql
mysql> create table tb_strhash(
    -> name varchar(20) primary key,
    -> content varchar(100)
    -> )engine=InnoDB DEFAULT CHARSET=utf8mb4;
Query OK, 0 rows affected (0.23 sec)
mysql> INSERT INTO tb_strhash (name,content) VALUES('T1001', UUID());
mysql> INSERT INTO tb_strhash (name,content) VALUES('ROSE', UUID());
mysql> INSERT INTO tb_strhash (name,content) VALUES('JERRY', UUID());
mysql> INSERT INTO tb_strhash (name,content) VALUES('CRISTINA', UUID());
mysql> INSERT INTO tb_strhash (name,content) VALUES('TOMCAT', UUID());
mysql> show tables;
+-------------------+
| Tables in log_db  |
+-------------------+
| partition_rang_tb |
| tb_app            |
| tb_brand          |
| tb_log            |
| tb_mod_range      |
| tb_strhash        |
| tb_stu            |
| tb_u              |
| tb_u2             |
| tb_user           |
+-------------------+
10 rows in set (0.01 sec)

mysql> select *from tb_strhash;
+----------+--------------------------------------+
| name     | content                              |
+----------+--------------------------------------+
| CRISTINA | 26d2a91b-bc53-11ec-8543-aced5c5877c6 |
| JERRY    | 26aed3a9-bc53-11ec-8543-aced5c5877c6 |
| ROSE     | 26ac0a5d-bc53-11ec-8543-aced5c5877c6 |
| T1001    | 26a9304a-bc53-11ec-8543-aced5c5877c6 |
| TOMCAT   | 245f4650-bc53-11ec-8543-aced5c5877c6 |
+----------+--------------------------------------+
5 rows in set (0.04 sec)

mysql> /*!mycat:dataNode=dn1*/select *from tb_strhash;
+----------+--------------------------------------+
| name     | content                              |
+----------+--------------------------------------+
| CRISTINA | 26d2a91b-bc53-11ec-8543-aced5c5877c6 |
| JERRY    | 26aed3a9-bc53-11ec-8543-aced5c5877c6 |
+----------+--------------------------------------+
2 rows in set (0.01 sec)

mysql> /*!mycat:dataNode=dn2*/select *from tb_strhash;
+--------+--------------------------------------+
| name   | content                              |
+--------+--------------------------------------+
| ROSE   | 26ac0a5d-bc53-11ec-8543-aced5c5877c6 |
| T1001  | 26a9304a-bc53-11ec-8543-aced5c5877c6 |
| TOMCAT | 245f4650-bc53-11ec-8543-aced5c5877c6 |
+--------+--------------------------------------+
3 rows in set (0.00 sec)

mysql> /*!mycat:dataNode=dn3*/select *from tb_strhash;
Empty set (0.00 sec)
```



* 一致性hash算法
一致性Hash算法有效的解决了分布式数据的拓容问题，配置如下:
rule.xml
```xml
	<tableRule name="sharding-by-stringhash">
		<rule>
			<columns>name</columns>
			<algorithm>sharding-by-stringhash2</algorithm>
		</rule>
	</tableRule>
	<function name="murmur"
		class="io.mycat.route.function.PartitionByMurmurHash">
		<property name="seed">0</property><!-- 默认是0 -->
		<property name="count">3</property><!-- 要分片的数据库节点数量，必须指定，否则没法分片 -->
		<property name="virtualBucketTimes">160</property><!-- 一个实际的数据库节点被映射为这么多虚拟节点，默认是160倍，也就是虚拟节点数是物理节点数的160倍 -->
		<!-- <property name="weightMapFile">weightMapFile</property> 节点的权重，没有指定权重的节点默认是1。以properties文件的格式填写，以从0开始到count-1的整数值也就是节点索引为key，以节点权重值为值。所有权重值必须是正整数，否则以1代替 -->
		<!-- <property name="bucketMapPath">/etc/mycat/bucketMapPath</property> 
			用于测试时观察各物理节点与虚拟节点的分布情况，如果指定了这个属性，会把虚拟节点的murmur hash值与物理节点的映射按行输出到这个文件，没有默认值，如果不指定，就不会输出任何东西 -->
	</function>
	
```
schema.xml
```xml
	<table name="tb_strhash" dataNode="dn1,dn2,dn3" rule="sharding-by-stringhash"/>
```
执行脚本
```sql
create table tb_order(
id int(11) primary key,
money int(11),
content varchar(200)
)engine=InnoDB ;
2). 插入数据
INSERT INTO tb_order (id,money,content) VALUES(1, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(212, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(312, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(412, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(534, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(621, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(754563, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(8123, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(91213, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(23232, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(112321, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(21221, 100 , UUID());
```
执行查询的sql
```sql
mysql> select *from tb_order;
+--------+-------+--------------------------------------+
| id     | money | content                              |
+--------+-------+--------------------------------------+
|    412 |   100 | 902d8518-bc57-11ec-8543-aced5c5877c6 |
|    621 |   100 | 902fdca4-bc57-11ec-8543-aced5c5877c6 |
|  12132 |   100 | 942b6a97-bc57-11ec-8543-aced5c5877c6 |
|      1 |   100 | 9028c109-bc57-11ec-8543-aced5c5877c6 |
|    212 |   100 | 902ae2d0-bc57-11ec-8543-aced5c5877c6 |
|    312 |   100 | 902c3a16-bc57-11ec-8543-aced5c5877c6 |
|    534 |   100 | 902e5966-bc57-11ec-8543-aced5c5877c6 |
|   8123 |   100 | 9031de3e-bc57-11ec-8543-aced5c5877c6 |
|  21221 |   100 | 90f08112-bc57-11ec-8543-aced5c5877c6 |
|  91213 |   100 | 9034421c-bc57-11ec-8543-aced5c5877c6 |
| 212132 |   100 | 9491d60f-bc57-11ec-8543-aced5c5877c6 |
|  23232 |   100 | 90365de7-bc57-11ec-8543-aced5c5877c6 |
| 112132 |   100 | 942890f3-bc57-11ec-8543-aced5c5877c6 |
| 112321 |   100 | 9039b23d-bc57-11ec-8543-aced5c5877c6 |
| 124321 |   100 | 942d00d2-bc57-11ec-8543-aced5c5877c6 |
| 754563 |   100 | 9030f69f-bc57-11ec-8543-aced5c5877c6 |
+--------+-------+--------------------------------------+
16 rows in set (0.03 sec)

mysql> /*!mycat:dataNode=dn1*/select *from tb_order;
+-------+-------+--------------------------------------+
| id    | money | content                              |
+-------+-------+--------------------------------------+
|   412 |   100 | 902d8518-bc57-11ec-8543-aced5c5877c6 |
|   621 |   100 | 902fdca4-bc57-11ec-8543-aced5c5877c6 |
| 12132 |   100 | 942b6a97-bc57-11ec-8543-aced5c5877c6 |
+-------+-------+--------------------------------------+
3 rows in set (0.01 sec)

mysql> /*!mycat:dataNode=dn2*/select *from tb_order;
+--------+-------+--------------------------------------+
| id     | money | content                              |
+--------+-------+--------------------------------------+
|      1 |   100 | 9028c109-bc57-11ec-8543-aced5c5877c6 |
|    212 |   100 | 902ae2d0-bc57-11ec-8543-aced5c5877c6 |
|    312 |   100 | 902c3a16-bc57-11ec-8543-aced5c5877c6 |
|    534 |   100 | 902e5966-bc57-11ec-8543-aced5c5877c6 |
|   8123 |   100 | 9031de3e-bc57-11ec-8543-aced5c5877c6 |
|  21221 |   100 | 90f08112-bc57-11ec-8543-aced5c5877c6 |
|  91213 |   100 | 9034421c-bc57-11ec-8543-aced5c5877c6 |
| 212132 |   100 | 9491d60f-bc57-11ec-8543-aced5c5877c6 |
+--------+-------+--------------------------------------+
8 rows in set (0.00 sec)

mysql> /*!mycat:dataNode=dn3*/select *from tb_order;
+--------+-------+--------------------------------------+
| id     | money | content                              |
+--------+-------+--------------------------------------+
|  23232 |   100 | 90365de7-bc57-11ec-8543-aced5c5877c6 |
| 112132 |   100 | 942890f3-bc57-11ec-8543-aced5c5877c6 |
| 112321 |   100 | 9039b23d-bc57-11ec-8543-aced5c5877c6 |
| 124321 |   100 | 942d00d2-bc57-11ec-8543-aced5c5877c6 |
| 754563 |   100 | 9030f69f-bc57-11ec-8543-aced5c5877c6 |
+--------+-------+--------------------------------------+
5 rows in set (0.00 sec)
```


* 日期分片算法
rule.xml
```xml
<tableRule name="sharding-by-date">
<rule>
<columns>create_time</columns>
<algorithm>sharding-by-date</algorithm>
</rule>
</tableRule>
<function name="sharding-by-date"
class="io.mycat.route.function.PartitionByDate">
<property name="dateFormat">yyyy-MM-dd</property>
<property name="sBeginDate">2020-01-01</property>
<property name="sEndDate">2020-12-31</property>
<property name="sPartionDay">10</property>
</function>
```
|属性|描述|
| ---- | ---- |
|dateFormat|日期格式|
|sBeginDate|开始日期|
|sEndDate|结束日期，如果配置了结束日期，则代码数到达了这个日期的分片之后，会重复开始分片插入|
|sPartitionDay|分区天数。默认值10，从开始日期算起，每10一个分期|

注意：配置规则的表的 dataNode 的分片，必须和分片规则数量一致，例如 2020-01-01 到
2020-12-31 ，每10天一个分片，一共需要37个分片。

* 单月小时算法
单月内按照小时拆分，最小粒度时小时，一条最多可以由24个分片，最小1个分片，下个月从头开始循环，每个月末需要手动清理数据
rule.xml
```xml
<tableRule name="sharding-by-hour">
<rule>
<columns>create_time</columns>
<algorithm>sharding-by-hour</algorithm>
</rule>
</tableRule>
<function name="sharding-by-hour"
class="io.mycat.route.function.LatestMonthPartion">
<property name="splitOneDay">24</property>
</function>
```
|属性|描述|
| ---- | ---- |
|splitOneDay |一天切分的分片数|


*　自然月分片算法
使用场景为按照月份分区，每个自然月一个分片，配置如下：
rule.xml
```xml
<tableRule name="sharding-by-month">
<rule>
<columns>create_time</columns>
<algorithm>sharding-by-month</algorithm>
</rule>
</tableRule>
<function name="sharding-by-month"
class="io.mycat.route.function.PartitionByMonth">
<property name="dateFormat">yyyy-MM-dd</property>
<property name="sBeginDate">2020-01-01</property>
<property name="sEndDate">2020-12-31</property>
</function>
```

*　日期范围hash算法＼
其四小和范围取模分片的思想时一样的，先根据日期进行范围分片求出分片组，再个奴家时间hash是的短期内数据分布的更加均匀；
优点：可以避免扩容时数据迁移，又可以一定成都上避免范围分片的热点问题
注意 : 要求日期格式尽量精确些，不然达不到局部均匀的目的

rule.xml
```xml
<tableRule name="range-date-hash">
<rule>
<columns>create_time</columns>
<algorithm>range-date-hash</algorithm>
</rule>
</tableRule>
<function name="range-date-hash"
class="io.mycat.route.function.PartitionByRangeDateHash">
<property name="dateFormat">yyyy-MM-dd HH:mm:ss</property>
<property name="sBeginDate">2020-01-01 00:00:00</property>
<property name="groupPartionSize">6</property>
<property name="sPartionDay">10</property>
</function>
```
|属性|描述|
| ---- | ---- |
|groupPartitionSize|每组的分片的数量|
|sPartionDay|代表多少天为1组|