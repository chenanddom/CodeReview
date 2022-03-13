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




















































