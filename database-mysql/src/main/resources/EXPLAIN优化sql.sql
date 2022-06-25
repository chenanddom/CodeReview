#table 表名
# 查询的每一行的记录都对应这一个单表
explain select *from s1;

# s1:驱动表 s2:被驱动表
EXPLAIN SELECT * FROM s1 INNER JOIN s2;

# id:在一个大的查询语句中每个select关键字都对应一个唯一的id
EXPLAIN SELECT *FROM s1 WHERE key1='a';


EXPLAIN select *from s1 INNER JOIN s2 ON s1.key1=s2.key1
WHERE s1.common_field='a';


EXPLAIN SELECT *FROM s1 WHERE key1 IN (SELECT key3 FROM s2);

EXPLAIN SELECT *FROM s1 UNION ALL SELECT *FROM s2;

# 联合查询需要将并集的部分去除一份,并集的部分需要在临时表去做，所以会出现三条记录
EXPLAIN SELECT *FROM s1 UNION SELECT *FROM s2;



EXPLAIN SELECT * FROM s1 WHERE key1 IN (SELECT key1 FROM s2 WHERE key1 ='a' UNION SELECT key1 from s1 WHERE key1='b');


# dependent subquery
EXPLAIN SELECT * from s1 WHERE key1 IN (SELECT key1 FROM s2 WHERE s1.key2 = s2.key2) OR key3='a';

# derived 派生表
EXPLAIN SELECT * FROM (SELECT key1 ,count(*) c from s1 GROUP BY key1) as derived_s1 WHERE c>1;

EXPLAIN SELECT * FROM s1  WHERE key1 IN (SELECT key1 FROM s2);









CREATE TABLE t(i int) Engine=MyISAM;



INSERT INTO t VALUES(1);


EXPLAIN SELECT * FROM t;

DROP TABLE IF EXISTS tt ;
CREATE TABLE tt(i int) Engine=INNODB;

INSERT INTO tt VALUES(1);

EXPLAIN SELECT * FROM tt;

# 对于主键和唯一索引的等值查询就是const的，其他的普通索引就不是了。
EXPLAIN SELECT *from s1 where id=10005;

EXPLAIN SELECT *FROM s1 where key2=10126

EXPLAIN SELECT *FROM s1 where key3='SzUYke'

# 在连接查询的时候，如果被驱动的表是通过主键或者唯一二级索引列等值匹配的方式进行访问的，(如果该主键或者唯一二级及索引是联合索引的话，所有的索引列都必须是进行等值比较)，则对该驱动表的访问方法是`eq_ref`.
EXPLAIN SELECT * FROM s1 INNER JOIN s2 ON s1.id = s2.id;



# 使用普通的二级索引进行等值访问的话，那么访问的类型就是`ref`
EXPLAIN SELECT * FROM s1 INNER JOIN s2 ON s1.key1 = s2.key1;


# ref_or_null
explain select *from s1 WHERE key1 ='a' OR key1 is null;

# index_merge
explain select *from s1 where key1 ='a' or key3='b';


# `unique_subquery`是针对在一些包含`IN`子查询的查询语句中，如果查询优化器决定将`IN`转换为`EXISTS`子查询，而且子查询可以使用到主键进行等值匹配的话，那么该子查询执行计划的type就是unique_subquery.
EXPLAIN SELECT * FROM s1 where key2 IN (SELECT id FROM s2 WHERE s1.key1=s2.key1) OR key3='a';



# range 实现范围查找
EXPLAIN SELECT * FROM s1 WHERE key1 IN('a','b','c');
EXPLAIN SELECT * FROM s1 WHERE key1>='a' and key1<='c';

# 当我们可以使用索引覆盖，但是需要扫描全部的饿索引记录的时候，该表的访问方法就是`index`.,(key_part1,key_part2,key_part3)是联合索引
EXPLAIN select key_part2 FROM s1 where key_part2='a';


EXPLAIN SELECT * FROM s1 WHERE common_field IN (SELECT key3 FROM s2 where s1.key1 = s2.key1) OR key3 = 'a';


system>const>eq_ref>ref>fulltext>ref_or_null>index_merge>unique_subquery>index_subquery>range>index>ALL






# key_len=303 ,utf8字符编码的情况下一个字符占3个字节，所以需要300(数据存储的空间大小)+1(一个字节表示该字段是否为空)+2(2个字节表示数据的长度)
explain SELECT *from s1 WHERE id=10005;
# key_len=303 ,utf8字符编码的情况下一个字符占3个字节，所以需要300(数据存储的空间大小)+1(一个字节表示该字段是否为空)+2(2个字节表示数据的长度)
EXPLAIN SELECT * FROM s1 WHERE key1='PvJXRs';
# key_len=303 ,utf8字符编码的情况下一个字符占3个字节，所以需要300(数据存储的空间大小)+1(一个字节表示该字段是否为空)+2(2个字节表示数据的长度)
explain select *from  s1 where key2=10096;
# key_len=303 ,utf8字符编码的情况下一个字符占3个字节，所以需要300(数据存储的空间大小)+1(一个字节表示该字段是否为空)+2(2个字节表示数据的长度)
explain select *from s1 where key3='kuuNFNYDYg';
# key_len=303 ,utf8字符编码的情况下一个字符占3个字节，所以需要300(数据存储的空间大小)+1(一个字节表示该字段是否为空)+2(2个字节表示数据的长度)
explain select *from s1 where key_part1='a';


# Extra=No tables used
EXPLAIN SELECT 1;

# Extra=Impossible Where
EXPLAIN SELECT *FROM s1 where 1!=1

# Using Where
EXPLAIN SELECT *FROM s1 where common_field='etJagJLLwb' ;
EXPLAIN SELECT *FROM s1 where key1 = 'orQcQY' AND common_field='etJagJLLwb' ;



# 当查询列表的有MIN或者MAX聚合函数，但是并没有符合WHERE子句中的搜索条件的记录时，将会提示如下的信息:Extra=No matching min/max row
EXPLAIN SELECT MIN(key1) FROM s1 WHERE key1 = 'abcdefg';

# 查询条件和返回的列都是对应的索引那么extra=Using index
EXPLAIN SELECT key1 FROM s1 WHERE key1='a';

# 使用到了索引，但是需要使用索引进行条件过滤，然后将过滤出来的记录再次回表查找.Extra = Using index condition
EXPLAIN SELECT *from s1 WHERE key1>'z' AND key1 LIKE '%a';



# 在使用左外连接的情况下，如果驱动表的某个不为空的列的筛选条件是不为空，那么Extra就会返回Using where,Not exists
EXPLAIN SELECT *FROM s1 LEFT JOIN s2 ON s1.key1 = s2.key1 WHERE s2.id IS NULL;



# 如果查询的使用使用到了多列索引，而且索引的关联条件都是OR，那么Extra的提示信息如下:Using union(idx_key1,idx_key3); Using where
EXPLAIN SELECT *FROM s1 WHERE key1='a' OR key3='a';


#如果排序没有使用索引，那么Extra=Using filesort
EXPLAIN SELECT *FROM s1 ORDER BY common_field;



#MySQL的很多功能需要借助临时表来完成，例如去重、排序之类的，比如在许多包含DISTINCT、GROUP BY、UNION等子句的拆线呢过程中，如果不能有效的利益索引完成查询，MySQL很有可能需求通过简历内部的临时表来执行查询。如果在拆线呢过程中使用到了内部的临时表，在执行计划的Extra就会提示Using temporary。
EXPLAIN SELECT DISTINCT common_field FROM s1;




EXPLAIN FORMAT=JSON SELECT DISTINCT common_field FROM s1;






