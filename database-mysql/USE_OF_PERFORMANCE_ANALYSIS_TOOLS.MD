[上一章(索引的创建原则)](./PRINCIPLES_INDEX_CREATION_DESIGN.MD)
#性能分析工具的使用
当我们遇到数据库调优问题的时候，该如何思考呢？这里把思考的流程整理成下面这张图。
整个流程划分成了观察（Show status） 和行动（Action） 两个部分。字母 S 的部分代表观察（会使
用相应的分析工具），字母 A 代表的部分是行动（对应分析可以采取的行动）。
![性能分析的步骤](./files\性能分析-1.png)

![性能分析的重要性](./files\性能分析-2.png)


## 查看系统性能参数
在MySQL中，可以使用SHOW STATUS 语句查询一些MySQL数据库服务器的性能参数、执行频率。
SHOW STATUS语句语法如下：
```sql
SHOW [GLOBAL|SESSION] STATUS LIKE '参数'
```
常见的参数有下面一些:
* Connections:连接MySQL服务器的次数。

* Uptime:MySQL服务器的上线时间。

* Slow_queries:慢查询的次数。

* Innodb_rows_read:SELECT查询返回的行数。

* Innodb_rows_insert:执行INSERT操作插入的行数

* Innodb_rows_uodated:执行UPDATE操作更新的行数。

* Innodb_rows_deleted:执行DELETE操作的行数。

* Com_select:查询操作的次数。

* Com_insert:插入操作的次数。对于批量插入的INSERT操作，只累加一次。

* Com_update:更新操作的次数。

* Com_delete:删除操作的次数。


### 统计成本
```sql
SHOW STATUS LIKE 'last_query_cost';
```
![性能分析](./files\性能分析-3.PNG)
执行
```sql
 select *from student_info where id between 900001 and 900100;
```
[性能分析](./files\性能分析-4.PNG)

从上面的sql的执行结果可以看到，第一个sql执行只是加载了1个页，第二个加载了20个页，相差了20倍，但是SQL执行的时间基本上时一样的，就是因为
采用了顺序读取的方式将页面一次性加载到缓存池中，然后再进行查找，虽然页数量(last_query_cost)增加了不少，但是通过缓冲查集的机制，并没有
增加多少查询时间。
使用场景:它对于比较开销时非常有用的，特别是我们也有好几种查询方式可选的时候。
```text
1. 位置决定效率。如果页就是再数据库缓冲池中的，那么效率时最高的，否则还需要从内存或者磁盘中进行读取，当然针对单个页来读取来说，如果页存在
于内存中，会比在磁盘中读取效率高很多。
2. 批量决定效率。如果我们从磁盘中对单一页进行随机读取，那么效率就会很低(差不多就要10ms)，而采用顺序的读取的方式，批量对页进行读取，平均
一页的读取效率就会提升很多，甚至要快于单页面在内存中的随机读取。
遇到IO就要考虑，常用的数放在缓冲池中，数据读取要从批量从磁盘读取，充分利用磁盘的IO.


```

### 开启慢查询日志参数
1. 开启slow_query_log
在使用前，我们需要先看下慢查询是否已经开启，使用下面这条命令即可:
```text
-- 设置开启慢查询日志
SET GLOBAL slow_query_log=on;
-- 查看慢查询日志是否开启
show variables like '%slow_query_log%'
-- 查看慢查询的界定时长，默认时10s
SET long_query_time=1;
```
![查看慢查询日志是否开启](./files\性能分析-5.PNG)

执行耗时SQL
```sql
-- 此句sql执行超过了1秒就记录进慢查询日志
select *from student_info where name='%ILH' or name='%LJpx%' or name like '%add' or name like '%mm%' or name like '%000%' or name like '%%%%' or name like '%888888%';
```
![生成慢查询日志](./files\性能分析-6.PNG)
![慢查询日志内容](./files\性能分析-7.PNG)

* 分析慢查询日志
在window平台需要安装perl语言的允许环境
![性能分析语句](./files\性能分析-9.PNG)
![新分析生成的文件](./files\性能分析-8.PNG)
![新分析生成的文件的内容](./files\性能分析-10.PNG)

* 删除慢查询日志
    1. 可以直接删除文件
    2. 删除文件后可以使用命令将文件复位：mysqladmin  -hlocalhost -uroot -P 3306 -proot@123 flush-logs slow


* 关闭慢查询日志
```sql
SET slow_query_log=off;
```



### 查看 SQL 执行成本：SHOW PROFILE
```sql
-- 开启profile
SET GLOBAL profiling=1;
SET profiling=ON;
-- 查看profile
SHOW profiles;
-- 查看具体的查询的消耗
show profile for query QUERY_ID
```
![show profile查看查询的消耗](./files\性能分析-11.PNG)

① ALL：显示所有的开销信息。
② BLOCK IO：显示块IO开销。 
③ CONTEXT SWITCHES：上下文切换开销。 
④ CPU：显示CPU开销信息。 
⑤ IPC：显示发送和接收开销信息。 
⑥ MEMORY：显示内存开销信息。 
⑦ PAGE FAULTS：显示页面错误开销信息。 
⑧ SOURCE：显示和Source_function，Source_file，Source_line相关的开销信息。 
⑨ SWAPS：显示交换次数开销信息。

## EXPLAIN 分析慢查询的饿sql
定位了慢查询的SQL之后，我们就可以使用EXPLAIN或者DESCRIBE工具做针对性的分析查询语句。DESCRIBE语句的使用方法与EXPLAIN语句是一样的，并且分析结果也是一样的。
MySQL中有专门负责优化SELECT语句的的优化器模块，主要的功能：通过计算分析系统中收集到的统计信息，为客户端请求的Query提供它认位最有的执行
计划(它认位最有的数据检索方式，但不见得是DBA认位是最优的，这部分最耗时)。
这个执行计划展示了接下来具体执行查询的方式，比如多表连接的顺序是什么，对于每个表采用什么访问方法来具体执行查询等待。MySQL为我们提供了EXPLAIN
语句来帮助我们查看某个查询语句的具体执行计划，大家看懂EXPLAIN语句的各个输出项，可以有针对性的提升我们查询语句的性能.

EXPLAIN输出的信息

|**列名**|**描述**|
| ---- | ---- |
|id|在一个大的查询语句中每个SELECT关键字对应一个唯一的id|
|select_type|SELECT关键字对应的那个查询的类型|
|table|表名|
|partitions|匹配的分区信息|
|type|针对单表的访问方法|
|possible_keys|可能用到的索引|
|key|实际上使用的索引|
|key_len|实际上使用的到索引长度|
|ref|当使用索引列值查询时，与索引列进行等值皮欸的信息|
|rows|预估的需要读取的记录条数|
|filtered|某个表经过搜索条件过滤后剩余的条数的百分比|
|Extra|一些额外的信息|
准备数据：
```sql
CALL insert_s1(10001,10000);

CALL insert_s2(10001,10000);

SELECT @@log_bin_trust_function_creators;

set global log_bin_trust_function_creators=1;

DELIMITER //
CREATE PROCEDURE insert_s2 (IN min_num INT (10),IN max_num INT (10))
BEGIN
DECLARE i INT DEFAULT 0;
SET autocommit = 0;
REPEAT
SET i = i + 1;
INSERT INTO s2 VALUES(
(min_num + i),
rand_string1(6),
(min_num + 30 * i + 5),
rand_string1(6),
rand_string1(10),
rand_string1(5),
rand_string1(10),
rand_string1(10));
UNTIL i = max_num
END REPEAT;
COMMIT;
END //
DELIMITER ;


DELIMITER //
CREATE FUNCTION rand_string1(n INT)
RETURNS VARCHAR(255) #该函数会返回一个字符串
BEGIN
DECLARE chars_str VARCHAR(100) DEFAULT
'abcdefghijklmnopqrstuvwxyzABCDEFJHIJKLMNOPQRSTUVWXYZ';
DECLARE return_str VARCHAR(255) DEFAULT '';
DECLARE i INT DEFAULT 0;
WHILE i < n DO
SET return_str =CONCAT(return_str,SUBSTRING(chars_str,FLOOR(1+RAND()*52),1));
SET i = i + 1;
END WHILE;
RETURN return_str;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE insert_s1 (IN min_num INT (10),IN max_num INT (10))
BEGIN
DECLARE i INT DEFAULT 0;
SET autocommit = 0;
REPEAT
SET i = i + 1;
INSERT INTO s1 VALUES(
(min_num + i),
rand_string1(6),
(min_num + 30 * i + 5),
rand_string1(6),
rand_string1(10),
rand_string1(5),
rand_string1(10),
rand_string1(10));
UNTIL i = max_num
END REPEAT;
COMMIT;
END //
DELIMITER ;




CREATE TABLE s1 (
id INT AUTO_INCREMENT,
key1 VARCHAR(100),
key2 INT,
key3 VARCHAR(100),
key_part1 VARCHAR(100),
key_part2 VARCHAR(100),
key_part3 VARCHAR(100),
common_field VARCHAR(100),
PRIMARY KEY (id),
INDEX idx_key1 (key1),
UNIQUE INDEX idx_key2 (key2),
INDEX idx_key3 (key3),
INDEX idx_key_part(key_part1, key_part2, key_part3)
) ENGINE=INNODB CHARSET=utf8;


CREATE TABLE s2 (
id INT AUTO_INCREMENT,
key1 VARCHAR(100),
key2 INT,
key3 VARCHAR(100),
key_part1 VARCHAR(100),
key_part2 VARCHAR(100),
key_part3 VARCHAR(100),
common_field VARCHAR(100),
PRIMARY KEY (id),
INDEX idx_key1 (key1),
UNIQUE INDEX idx_key2 (key2),
INDEX idx_key3 (key3),
INDEX idx_key_part(key_part1, key_part2, key_part3)
) ENGINE=INNODB CHARSET=utf8;

```



```sql
-- table:表名
-- 查询的每一行都记录对应着一个单表
EXPLAIN SELECT *FROM s1;
-- s1时驱动表，s2：被驱动表
EXPLAIN SELECT * FROM s1 INNER JOIN s2;
-- id：在一个大的查询语句中每个SELECT关键字都对应一个唯一的id
EXPLAIN SELECT * FROM s1 WHERE key1 = 'a';
```
* 优化器将子查询优化称多表查询 id都是一样的
![优化器将子查询优化称多表查询](./files\性能分析-13.PNG)
![优化器将子查询变成多表连接查询](./files\性能分析-14.PNG)
* 存在子查询的情况
![存在子查询的情况](./files\性能分析-12.PNG)

*　UNION去重使用临时表
![UNION去重使用临时表](./files\性能分析-15.PNG)


小结：
1. id如果相同，可以认位是一组，从上往下顺序执行
2. 在所有组中，id值越大，优先级越高，越先执行。
3. 关注点:id号每个号码，表示一趟独立的查询，一个sql的查询趟数越小越好。

### Select_type

|名称描述|描述|
| ---- | ----- |
|SIMPLE|表示不需要union操作或者不包含子查询的简单select查询。有连接查询时，外层的查询为simple，且只有一个|
|PRIMARY|一个需要union操作或者含有子查询的select，位于最外层的单位查询的select_type即为primary。且只有一个|
|UNION|union连接的两个select查询，第一个查询是PRIMARY，除了第一个表外，第二个以后的表select_type都是union|
|UNION RESULT|两个查询UNION后的结果进行取并集|
|SUBQUERY|存在子查询并且优化器优化后还是子查询的情况|
|DEPENDENT UNION|与union一样，出现在union 或union all语句中，但是这个查询要受到外部查询的影响|
|DERIVED|出现子查询，并将子查询的结果作为新表，在新表的基础上做条件查询|
|MATERIALIZED|物化子查询|
|UNCACHEABLE SUBQUERY|一个子查询，其结果无法缓存，必须为外部查询的每一行重新计算|
|UNCACHEABLE UNION||



*　SIMPLE
![SIMPLE](./files\性能分析-16.PNG)

*　PRIMARY
一个需要union操作或者含有子查询的select，位于最外层的单位查询的select_type即为primary。且只有一个
![PRIMARY](./files\性能分析-17.PNG)

*　UNION
union连接的两个select查询，第一个查询是PRIMARY，除了第一个表外，第二个以后的表select_type都是union
![UNION](./files\性能分析-18.PNG)

*　UNION RESULT
union连接的两个select查询，第一个查询是PRIMARY，除了第一个表外，第二个以后的表select_type都是union
![UNION RESULT](./files\性能分析-18.PNG)

* SUBQUERY
除了from字句中包含的子查询外，其他地方出现的子查询都可能是subquery
![SUBQUERY](./files\性能分析-19.PNG)

*　DEPENDENT　SUBQUERY
与dependent union类似，表示这个subquery的查询要受到外部表查询的影响
[DEPENDENT　SUBQUERY](./files\性能分析-20.PNG)


* DENPENDENT UNION
与union一样，出现在union 或union all语句中，但是这个查询要受到外部查询的影响
union result
包含union的结果集，在union和union all语句中,因为它不需要参与查询，所以id字段为null
![DENPENDENT UNION](./files\性能分析-21.PNG)


* DERIVED
出现子查询，并将子查询的结果作为新表，在新表的基础上做条件查询
![DERIVED](./files\性能分析-22.PNG)




* MATERIALIZED
MATERIALIZED 物化子查询可以当作做成一个临时表，比如，下面 in 后面括号里面的查询

![MATERIALIZED](./files\性能分析-23.PNG)


### partitions
分区
例子：
```sql
-- 创建分区表，
-- 按照id分区，id<100 p0分区，其他p1分区
CREATE TABLE user_partitions (id INT auto_increment,
NAME VARCHAR(12),PRIMARY KEY(id))
PARTITION BY RANGE(id)(
PARTITION p0 VALUES less than(100),
PARTITION p1 VALUES less than MAXVALUE
);
DESC SELECT * FROM user_partitions WHERE id>200;
```
![分区](./files\性能分析-24.PNG)

### type针对单表的访问方法
访问的方法有如下:
**system、const、req_ref、ref、fulltext、req_or_null、index_merge、
unique_subquery、index_subquery、range、index、index、ALL、**


* system 
当表中`只有一条记录`并且该表使用的存储引擎的统计数据是精确的，比如MyISAM、Memory，
那么对该表的访问方法就是`system`。
```sql
CREATE TBALE t1(a int);
ALTER TABLE t1 ENGINE=MyISAM
INSERT INTO t1(a) values(1);
EXPLAIN SELECT *FROM t1;
```
[system访问方法](./files\性能分析-25.PNG)

* const
当我们根据主键或者唯一二级索引列与常数进行等值匹配时，对单表的访问方法就是`const`
```sql
EXPLAIN SELECT *FROM s1 WHERE id=10260
```
![const访问方法](./files\性能分析-26.PNG)

*　req_ref
在连接查询是，如果被驱动表是通过主键或者唯一二级索引列等值匹配的方式进行访问的(如果该主键或者唯一二级所i你是联合索引的话，所有的索引列都
必须进行等值比较)，则对该被驱动表的访问方法就是eq_ref
```sql
EXPLAIN SELECT *FROM s1 INNER JOIN s2 ON s1.id=s2.id;
```
![eq_ref访问方法](./files\性能分析-27.PNG)

*　ref
当通过二级索引与常量进行等值匹配时查询某个表，那么该表的访问方法就是ref
![ref 访问方法](./files\性能分析-28.PNG)

* fulltext
全文索引
```sql
create table article(id int primary key auto_increment,authors varchar(32) not null,content text);
insert into article(authors,content) values('zhangsan','文章1'),('lisi','文章2'),('wangwu','文章3');
insert into article(authors,content) values('zhangsan','this is content'),('lisi','what fuck'),('wangwu','just test');
alter table article add fulltext index idx_ft_content(content);
EXPLAIN SELECT *FROM article WHERE MATCH(content) AGAINST('test' IN BOOLEAN MODE);
```
![fulltext访问方法](./files\性能分析-29.PNG)

* req_or_null
当对普二级索引进行等值匹配拆线呢，该索引列的值页可以时NULL值，那么对该表的访问方法就可能时'ref_or_null'
```sql
EXPLAIN SELECT *FROM s1 WHERE key1='a' OR key1 IS NULL;
```
![ref_or_null访问方法](./files\性能分析-30.PNG)


* index_merge
我们的 where 中可能有多个条件(或者join)涉及到多个字段，它们之间进行 AND 或者 OR，那么此时就有可能会使用到 index merge 技术。
index merge 技术如果简单的说，其实就是：对多个索引分别进行条件扫描，然后将它们各自的结果进行合并(intersect/union)。MySQL5.0之前，
一个表一次只能使用一个索引，无法同时使用多个索引分别进行条件扫描。但是从5.1开始，引入了 index merge 优化技术，对同一个表可以使用多个
索引分别进行条件扫描。
```sql
EXPLAIN SELECT *FROM s1 WHERE key1='a' OR key2='b'
```
![index_merge访问方法](./files\性能分析-31.PNG)


*　unique subquery
这个访问方法时针对在一些包含IN子查询的查询语句中，如果查询优化器决定将IN子查询转换为EXISTS
子查询，而且子拆线呢可以使用到主键进行等值匹配的话，那么该子查询执行计划的type的列值就时unique subquery
```sql
EXPLAIN SELECT *FROM s1 WHERE key2 IN (SELECT id FROM s2 WHERE s1.key1=s2.key1) OR key2='a';
```
![unique subquery访问方法](./files\性能分析-32.PNG)


*　index_subquery
此联接类型类似于unique_子查询。它替换子查询中的索引，但它适用于以下形式的子查询中的非唯一索引：
```sql
value IN (SELECT key_column FROM single_table WHERE some_expr)
```
*　range
如果使用索引获取某些`范围区间`的记录，那么就可能使用到`range`访问方法
```sql
EXPLAIN SELECT * FROM s1 WHERE key1 IN ('a', 'b', 'c');
EXPLAIN SELECT * FROM s1 WHERE key1 > 'a' AND key1 < 'b';
```
![range访问方法](./files\性能分析-33.PNG)


*　INDEX
当我们可以使用索引覆盖，但是需要扫面全部的索引记录时，该表的访问方法就是index，就是不需要回表。
```sql
explain select key_part1, key_part2 from s1 where key_part3='a';
```
![index访问方法](./files\性能分析-34.PNG)



* ALL
全表搜索
```sql
 explain select *from s1;
```
![All访问方法](./files\性能分析-35.PNG)

结论：
结果值最好到最坏的顺序依次时:system>const>eq_ref>ref>fulltext>ref_or_null>index_merge>unique_subquery>index_subquery>range>index>ALL
其中比较重要的几个提取出来（见上图中的蓝色）。SQL 性能优化的目标：至少要达到 range 级别，要求是 ref 级别，最好是 consts级别。（阿里巴巴
开发手册要求）

### possible_keys和key
表示的是可能使用到的索引和实际上使用到的索引
```sql
EXPLAIN SELECT * FROM s1 WHERE key1 > 'z' AND key3 = 'a';
```
![可能使用到的索引和实际使用到的索引](./files\性能分析-36.PNG)



### key_len
实际使用到的索引长度(即：字节数)帮你检查`是否充分的利用上了索引`，`值越大越好`,主要针对于联合索引，有一定的参考意义。
**注意:utf-8使用3个字节表示一个字符.**

![整型的key_len占4个字节](./files\性能分析-37.PNG)

允许为空的字段需要占据一个字节

![允许为空的字段需要占据一个字节](./files\性能分析-38.PNG)

UTF-8字符集一个字符需要3个字节表示加上2个字节表示定长和1个字节表示空值

![UTF-8字符集一个字符需要3个字节表示加上2个字节表示定长和1个字节表示空值](./files\性能分析-39.PNG)


