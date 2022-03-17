[上一章(性能分析工具的使用)](./USE_OF_PERFORMANCE_ANALYSIS_TOOLS.MD)
# 索引优化和查询优化
1. 主备数据
```sql
CREATE DATABASE IF NOT EXISTS highdb10;

use highdb10;


CREATE TABLE `class` (
`id` INT(11) NOT NULL AUTO_INCREMENT,
`className` VARCHAR(30) DEFAULT NULL,
`address` VARCHAR(40) DEFAULT NULL,
`monitor` INT NULL ,
PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
CREATE TABLE `student` (
`id` INT(11) NOT NULL AUTO_INCREMENT,
`stuno` INT NOT NULL ,
`name` VARCHAR(20) DEFAULT NULL,
`age` INT(3) DEFAULT NULL,
`classId` INT(11) DEFAULT NULL,
PRIMARY KEY (`id`)
#CONSTRAINT `fk_class_id` FOREIGN KEY (`classId`) REFERENCES `t_class` (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



set global log_bin_trust_function_creators=1;


#随机产生字符串
DELIMITER //
CREATE FUNCTION rand_string(n INT) RETURNS VARCHAR(255)
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


#用于随机产生多少到多少的编号
DELIMITER //
CREATE FUNCTION rand_num (from_num INT ,to_num INT) RETURNS INT(11)
BEGIN
DECLARE i INT DEFAULT 0;
SET i = FLOOR(from_num +RAND()*(to_num - from_num+1)) ;
RETURN i;
END //
DELIMITER ;

#创建往stu表中插入数据的存储过程
DELIMITER //
CREATE PROCEDURE insert_stu( START INT , max_num INT )
BEGIN
DECLARE i INT DEFAULT 0;
SET autocommit = 0; #设置手动提交事务
REPEAT #循环
SET i = i + 1; #赋值
INSERT INTO student (stuno, name ,age ,classId ) VALUES
((START+i),rand_string(6),rand_num(1,50),rand_num(1,1000));
UNTIL i = max_num
END REPEAT;
COMMIT; #提交事务
END //
DELIMITER ;


#执行存储过程，往class表添加随机数据
DELIMITER //
CREATE PROCEDURE `insert_class`( max_num INT )
BEGIN
DECLARE i INT DEFAULT 0;
SET autocommit = 0;
REPEAT
SET i = i + 1;
INSERT INTO class ( classname,address,monitor ) VALUES
(rand_string(8),rand_string(10),rand_num(1,100000));
UNTIL i = max_num
END REPEAT;
COMMIT;
END //
DELIMITER ;

CALL insert_class(10000);

CALL insert_stu(100000,500000);
```

## 索引失效的案例

* 全值匹配我最爱
如果表没有创建任何索引，那么就无法使用索引，此时对表的查询就会使用全值匹配的方式进行
例子:
```sql
EXPLAIN SELECT *FROM s1 WHERE age=20;
```
![全值匹配我最爱](./files\索引优化-查询优化-1.PNG)
```sql
-- 添加索引
 CREATE INDEX idx_age ON student(age);
```
![增对单列增加索引](./files\索引优化-查询优化-2.PNG)

```sql
EXPLAIN SELECT *FROM student WHERE age=20 AND classId=4;
```
![两个字段的联合索引](./files\索引优化-查询优化-3.PNG)
```sql
-- 创建联合索引
CREATE INDEX idx_age_classId ON student(age,classId);
EXPLAIN SELECT *FROM student WHERE age=20 AND classId=4;
```
![创建联合索引后](./files\索引优化-查询优化-4.PNG)

以上的的几次查询我们可以得出：如果查询的条件超出了索引包含的列， 那么会先按照索引的查找对应的主键索引，然后根据主键索引回表查询，根据回表查询
找出所需要的字段。


* 最左前缀匹配原则
创建联合索引的时候，如果使用WHERE语句进行条件筛选，那么索引左边的字段的需要包含，并且条件要从左到右依次添加才可以生效，否则无法使用索引
```text

mysql> show index from student;
+---------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
| Table   | Non_unique | Key_name | Seq_in_index | Column_name | Collation | Cardinality | Sub_part | Packed | Null | Index_type | Comment | Index_comment | Visible | Expression |
+---------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
| student |          0 | PRIMARY  |            1 | id          | A         |      498372 |     NULL |   NULL |      | BTREE      |         |               | YES     | NULL       |
| student |          1 | idx_age  |            1 | age         | A         |          48 |     NULL |   NULL | YES  | BTREE      |         |               | YES     | NULL       |
+---------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
2 rows in set (0.00 sec)

mysql> EXPLAIN SELECT *FROM student WHERE age=20 AND classId=4;
+----+-------------+---------+------------+------+---------------+---------+---------+-------+------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key     | key_len | ref   | rows | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+---------+---------+-------+------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_age       | idx_age | 5       | const | 9859 |    10.00 | Using where |
+----+-------------+---------+------------+------+---------------+---------+---------+-------+------+----------+-------------+
1 row in set, 1 warning (0.01 sec)

mysql> CREATE INDEX idx_age_classId ON student(age,classId);
Query OK, 0 rows affected (1.45 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> EXPLAIN SELECT *FROM student WHERE age=20 AND classId=4;
+----+-------------+---------+------------+------+-------------------------+-----------------+---------+-------------+------+----------+-------+
| id | select_type | table   | partitions | type | possible_keys           | key             | key_len | ref         | rows | filtered | Extra |
+----+-------------+---------+------------+------+-------------------------+-----------------+---------+-------------+------+----------+-------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_age,idx_age_classId | idx_age_classId | 10      | const,const |    9 |   100.00 | NULL  |
+----+-------------+---------+------------+------+-------------------------+-----------------+---------+-------------+------+----------+-------+
1 row in set, 1 warning (0.00 sec)

mysql> EXPLAIN SELECT *FROM student WHERE age=20 AND classId=4 AND `name`='abc';
+----+-------------+---------+------------+------+-------------------------+-----------------+---------+-------------+------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys           | key             | key_len | ref         | rows | filtered | Extra       |
+----+-------------+---------+------------+------+-------------------------+-----------------+---------+-------------+------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_age,idx_age_classId | idx_age_classId | 10      | const,const |    9 |    10.00 | Using where |
+----+-------------+---------+------------+------+-------------------------+-----------------+---------+-------------+------+----------+-------------+
1 row in set, 1 warning (0.00 sec)

mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE student.age=30 AND student.name = 'abcd' ;
+----+-------------+---------+------------+------+-------------------------+---------+---------+-------+-------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys           | key     | key_len | ref   | rows  | filtered | Extra       |
+----+-------------+---------+------------+------+-------------------------+---------+---------+-------+-------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_age,idx_age_classId | idx_age | 5       | const | 10061 |    10.00 | Using where |
+----+-------------+---------+------------+------+-------------------------+---------+---------+-------+-------+----------+-------------+
1 row in set, 2 warnings (0.01 sec)

mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE student.classid=1 AND student.name = 'abcd';
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 498372 |     1.00 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 2 warnings (0.00 sec)

mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student
    -> WHERE classid=4 AND student.age=30 AND student.name = 'abcd';
+----+-------------+---------+------------+------+-------------------------+-----------------+---------+-------------+------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys           | key             | key_len | ref         | rows | filtered | Extra       |
+----+-------------+---------+------------+------+-------------------------+-----------------+---------+-------------+------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_age,idx_age_classId | idx_age_classId | 10      | const,const |    6 |    10.00 | Using where |
+----+-------------+---------+------------+------+-------------------------+-----------------+---------+-------------+------+----------+-------------+
1 row in set, 2 warnings (0.00 sec)

mysql> CREATE INDEX idx_age_classId_name ON student(age,classId,name);
Query OK, 0 rows affected (1.73 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> show index from student;
+---------+------------+----------------------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
| Table   | Non_unique | Key_name             | Seq_in_index | Column_name | Collation | Cardinality | Sub_part | Packed | Null | Index_type | Comment | Index_comment | Visible | Expression |
+---------+------------+----------------------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
| student |          0 | PRIMARY              |            1 | id          | A         |      498372 |     NULL |   NULL |      | BTREE      |         |               | YES     | NULL       |
| student |          1 | idx_age              |            1 | age         | A         |          48 |     NULL |   NULL | YES  | BTREE      |         |               | YES     | NULL       |
| student |          1 | idx_age_classId      |            1 | age         | A         |          45 |     NULL |   NULL | YES  | BTREE      |         |               | YES     | NULL       |
| student |          1 | idx_age_classId      |            2 | classId     | A         |       50160 |     NULL |   NULL | YES  | BTREE      |         |               | YES     | NULL       |
| student |          1 | idx_age_classId_name |            1 | age         | A         |          48 |     NULL |   NULL | YES  | BTREE      |         |               | YES     | NULL       |
| student |          1 | idx_age_classId_name |            2 | classId     | A         |       49455 |     NULL |   NULL | YES  | BTREE      |         |               | YES     | NULL       |
| student |          1 | idx_age_classId_name |            3 | name        | A         |      498372 |     NULL |   NULL | YES  | BTREE      |         |               | YES     | NULL       |
+---------+------------+----------------------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
7 rows in set (0.01 sec)

mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE student.classid=1 AND student.name = 'abcd';
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 498372 |     1.00 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 2 warnings (0.00 sec)

mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE student.age=30 AND student.name = 'abcd' ;
+----+-------------+---------+------------+------+----------------------------------------------+---------+---------+-------+-------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys                                | key     | key_len | ref   | rows  | filtered | Extra       |
+----+-------------+---------+------------+------+----------------------------------------------+---------+---------+-------+-------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_age,idx_age_classId,idx_age_classId_name | idx_age | 5       | const | 10061 |    10.00 | Using where |
+----+-------------+---------+------------+------+----------------------------------------------+---------+---------+-------+-------+----------+-------------+
1 row in set, 2 warnings (0.01 sec)

mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE student.age=30 AND student.classId=4 AND student.name = 'abcd' ;
+----+-------------+---------+------------+------+----------------------------------------------+----------------------+---------+-------------------+------+----------+-------+
| id | select_type | table   | partitions | type | possible_keys                                | key                  | key_len | ref               | rows | filtered | Extra |
+----+-------------+---------+------------+------+----------------------------------------------+----------------------+---------+-------------------+------+----------+-------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_age,idx_age_classId,idx_age_classId_name | idx_age_classId_name | 73      | const,const,const |    1 |   100.00 | NULL  |
+----+-------------+---------+------------+------+----------------------------------------------+----------------------+---------+-------------------+------+----------+-------+
1 row in set, 2 warnings (0.00 sec)
```

* 计算、函数、类型转换(自动或者手动)导致索引失效

1. 使用函数会使得索引失效：
```sql
mysql> EXPLAIN SELECT *FROM student WHERE name like 'abc%';
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 498372 |    11.11 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 1 warning (0.00 sec)

mysql> CREATE INDEX idx_name ON student(name);
Query OK, 0 rows affected (1.71 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> EXPLAIN SELECT *FROM student WHERE name like 'abc%';
+----+-------------+---------+------------+-------+---------------+----------+---------+------+------+----------+-----------------------+
| id | select_type | table   | partitions | type  | possible_keys | key      | key_len | ref  | rows | filtered | Extra                 |
+----+-------------+---------+------------+-------+---------------+----------+---------+------+------+----------+-----------------------+
|  1 | SIMPLE      | student | NULL       | range | idx_name      | idx_name | 63      | NULL |   26 |   100.00 | Using index condition |
+----+-------------+---------+------------+-------+---------------+----------+---------+------+------+----------+-----------------------+
1 row in set, 1 warning (0.00 sec)

mysql> EXPLAIN SELECT SQL_NO_CACHE *FROM student WHERE LEFT(student.name,3)='abc';
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 498372 |   100.00 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 2 warnings (0.00 sec)
```
2. 查询语句添加计算就会使得索引失效
```sql
mysql> EXPLAIN SELECT *FROM student WHERE stu_no+1=90001;
ERROR 1054 (42S22): Unknown column 'stu_no' in 'where clause'
mysql> EXPLAIN SELECT *FROM student WHERE stu_no+1=90001
    -> ;
ERROR 1054 (42S22): Unknown column 'stu_no' in 'where clause'
mysql> DESC student;
+---------+-------------+------+-----+---------+----------------+
| Field   | Type        | Null | Key | Default | Extra          |
+---------+-------------+------+-----+---------+----------------+
| id      | int         | NO   | PRI | NULL    | auto_increment |
| stuno   | int         | NO   |     | NULL    |                |
| name    | varchar(20) | YES  | MUL | NULL    |                |
| age     | int         | YES  | MUL | NULL    |                |
| classId | int         | YES  |     | NULL    |                |
+---------+-------------+------+-----+---------+----------------+
5 rows in set (0.00 sec)

mysql> EXPLAIN SELECT *FROM student WHERE stuno+1=90001;
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 498372 |   100.00 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 1 warning (0.00 sec)

mysql> CREATE INDEX idx_stuno ON student(stuno);
Query OK, 0 rows affected (1.22 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> EXPLAIN SELECT *FROM student WHERE stuno+1=90001;
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 498372 |   100.00 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 1 warning (0.00 sec)

mysql> EXPLAIN SELECT *FROM student WHERE stuno=90001;
+----+-------------+---------+------------+------+---------------+-----------+---------+-------+------+----------+-------+
| id | select_type | table   | partitions | type | possible_keys | key       | key_len | ref   | rows | filtered | Extra |
+----+-------------+---------+------------+------+---------------+-----------+---------+-------+------+----------+-------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_stuno     | idx_stuno | 4       | const |    1 |   100.00 | NULL  |
+----+-------------+---------+------------+------+---------------+-----------+---------+-------+------+----------+-------+
1 row in set, 1 warning (0.00 sec)
```
3. 类型转换导致索引失效：
```sql
mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE NAME = 123;
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | idx_name      | NULL | NULL    | NULL | 498372 |    10.00 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 5 warnings (0.00 sec)

mysql> EXPLAIN SELECT SQL_NO_CACHE * FROM student WHERE NAME = '123';
+----+-------------+---------+------------+------+---------------+----------+---------+-------+------+----------+-------+
| id | select_type | table   | partitions | type | possible_keys | key      | key_len | ref   | rows | filtered | Extra |
+----+-------------+---------+------------+------+---------------+----------+---------+-------+------+----------+-------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_name      | idx_name | 63      | const |    1 |   100.00 | NULL  |
+----+-------------+---------+------------+------+---------------+----------+---------+-------+------+----------+-------+
1 row in set, 2 warnings (0.00 sec)
```
*　范围条件右边的列索引失效
在联合索引的时候，如果索引左边的字段进行了范围查找，那么索引中该字段的右边的字段就无法使用索引
```sql
mysql> CREATE INDEX idx_age_classId_name ON student(age,classId,name);
Query OK, 0 rows affected (1.77 sec)
Records: 0  Duplicates: 0  Warnings: 0
-- 这个地方可以看到索引的使用长度只有前面的age字段和classId字段(4字节+1字节记录是否空)。
mysql> EXPLAIN SELECT * FROM student WHERE student.age=30 AND student.classId>20 AND student.name='abc';
+----+-------------+---------+------------+-------+----------------------+----------------------+---------+------+-------+----------+----------------------------------+
| id | select_type | table   | partitions | type  | possible_keys        | key                  | key_len | ref  | rows  | filtered | Extra                            |
+----+-------------+---------+------------+-------+----------------------+----------------------+---------+------+-------+----------+----------------------------------+
|  1 | SIMPLE      | student | NULL       | range | idx_age_classId_name | idx_age_classId_name | 10      | NULL | 18054 |    10.00 | Using index condition; Using MRR |
+----+-------------+---------+------------+-------+----------------------+----------------------+---------+------+-------+----------+----------------------------------+
1 row in set, 1 warning (0.01 sec)
```
调整索引字段位置之后
```sql
mysql> CREATE INDEX idx_age_name_classid ON student(age,name,classId);
Query OK, 0 rows affected (1.84 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> EXPLAIN SELECT *FROM student WHERE age>30 AND `name`='abc' AND classId>20;
+----+-------------+---------+------------+------+----------------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys        | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+----------------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | idx_age_name_classid | NULL | NULL    | NULL | 498372 |     1.67 | Using where |
+----+-------------+---------+------------+------+----------------------+------+---------+------+--------+----------+-------------+
1 row in set, 1 warning (0.00 sec)

mysql> show index from student;
+---------+------------+----------------------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
| Table   | Non_unique | Key_name             | Seq_in_index | Column_name | Collation | Cardinality | Sub_part | Packed | Null | Index_type | Comment | Index_comment | Visible | Expression |
+---------+------------+----------------------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
| student |          0 | PRIMARY              |            1 | id          | A         |      498372 |     NULL |   NULL |      | BTREE      |         |               | YES     | NULL       |
| student |          1 | idx_age_name_classid |            1 | age         | A         |          48 |     NULL |   NULL | YES  | BTREE      |         |               | YES     | NULL       |
| student |          1 | idx_age_name_classid |            2 | name        | A         |      473904 |     NULL |   NULL | YES  | BTREE      |         |               | YES     | NULL       |
| student |          1 | idx_age_name_classid |            3 | classId     | A         |      498372 |     NULL |   NULL | YES  | BTREE      |         |               | YES     | NULL       |
+---------+------------+----------------------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
4 rows in set (0.01 sec)
--  索引长度为73 age(5字节)+name(60字节记录内容2字节记录内容长度+1字节记录空)+classId(5字节)
mysql> EXPLAIN SELECT *FROM student WHERE age=30 AND `name`='abc' AND classId>20;
+----+-------------+---------+------------+-------+----------------------+----------------------+---------+------+------+----------+-----------------------+
| id | select_type | table   | partitions | type  | possible_keys        | key                  | key_len | ref  | rows | filtered | Extra                 |
+----+-------------+---------+------------+-------+----------------------+----------------------+---------+------+------+----------+-----------------------+
|  1 | SIMPLE      | student | NULL       | range | idx_age_name_classid | idx_age_name_classid | 73      | NULL |    1 |   100.00 | Using index condition |
+----+-------------+---------+------------+-------+----------------------+----------------------+---------+------+------+----------+-----------------------+
1 row in set, 1 warning (0.00 sec)
```

* <>或者!=不等于的使用导致索引失效
```sql
mysql> DROP INDEX idx_age_name_classid ON student;
Query OK, 0 rows affected (0.02 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> show index from student;
+---------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
| Table   | Non_unique | Key_name | Seq_in_index | Column_name | Collation | Cardinality | Sub_part | Packed | Null | Index_type | Comment | Index_comment | Visible | Expression |
+---------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
| student |          0 | PRIMARY  |            1 | id          | A         |      498372 |     NULL |   NULL |      | BTREE      |         |               | YES     | NULL       |
+---------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
1 row in set (0.00 sec)

mysql> CREATE INDEX idx_name ON student(name);
Query OK, 0 rows affected (1.71 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> EXPLAIN SELECT *FROM student WHERE name <> 'abc';
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | idx_name      | NULL | NULL    | NULL | 498372 |    50.15 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 1 warning (0.00 sec)

mysql> EXPLAIN SELECT *FROM student WHERE name = 'abc';
+----+-------------+---------+------------+------+---------------+----------+---------+-------+------+----------+-------+
| id | select_type | table   | partitions | type | possible_keys | key      | key_len | ref   | rows | filtered | Extra |
+----+-------------+---------+------------+------+---------------+----------+---------+-------+------+----------+-------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_name      | idx_name | 63      | const |    1 |   100.00 | NULL  |
+----+-------------+---------+------------+------+---------------+----------+---------+-------+------+----------+-------+
1 row in set, 1 warning (0.00 sec)

mysql> EXPLAIN SELECT *FROM student WHERE name != 'abc';
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | idx_name      | NULL | NULL    | NULL | 498372 |    50.15 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 1 warning (0.00 sec)
```
* IS NOT NULL 无法使用索引 IS NULL 可以使用索引
```sql
mysql> CREATE INDEX idx_age ON student(age);
Query OK, 0 rows affected (1.27 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> show index from student;
+---------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
| Table   | Non_unique | Key_name | Seq_in_index | Column_name | Collation | Cardinality | Sub_part | Packed | Null | Index_type | Comment | Index_comment | Visible | Expression |
+---------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
| student |          0 | PRIMARY  |            1 | id          | A         |      498372 |     NULL |   NULL |      | BTREE      |         |               | YES     | NULL       |
| student |          1 | idx_age  |            1 | age         | A         |          48 |     NULL |   NULL | YES  | BTREE      |         |               | YES     | NULL       |
+---------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
2 rows in set (0.01 sec)
-- 此处的查询因为使用了 IS NOT NULL 无法走索引
mysql> EXPLAIN SELECT *FROM student WHERE age IS NOT NULL;
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | idx_age       | NULL | NULL    | NULL | 498372 |    50.00 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 1 warning (0.01 sec)
-- 此处的所有走索引
mysql> EXPLAIN SELECT *FROM student WHERE age IS NULL;
+----+-------------+---------+------------+------+---------------+---------+---------+-------+------+----------+-----------------------+
| id | select_type | table   | partitions | type | possible_keys | key     | key_len | ref   | rows | filtered | Extra                 |
+----+-------------+---------+------------+------+---------------+---------+---------+-------+------+----------+-----------------------+
|  1 | SIMPLE      | student | NULL       | ref  | idx_age       | idx_age | 5       | const |    1 |   100.00 | Using index condition |
+----+-------------+---------+------------+------+---------------+---------+---------+-------+------+----------+-----------------------+
1 row in set, 1 warning (0.00 sec)
```

* like以通配符%开头索引失效
```sql
mysql> CREATE INDEX idx_name ON student(name);
Query OK, 0 rows affected (1.71 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> EXPLAIN SELECT *FROM student WHERE name like '%abc';
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 498372 |    11.11 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 1 warning (0.00 sec)

mysql> EXPLAIN SELECT *FROM student WHERE name like '%abc%';
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 498372 |    11.11 | Using where |
+----+-------------+---------+------------+------+---------------+------+---------+------+--------+----------+-------------+
1 row in set, 1 warning (0.00 sec)

mysql> EXPLAIN SELECT *FROM student WHERE name like 'abc%';
+----+-------------+---------+------------+-------+---------------+----------+---------+------+------+----------+-----------------------+
| id | select_type | table   | partitions | type  | possible_keys | key      | key_len | ref  | rows | filtered | Extra                 |
+----+-------------+---------+------------+-------+---------------+----------+---------+------+------+----------+-----------------------+
|  1 | SIMPLE      | student | NULL       | range | idx_name      | idx_name | 63      | NULL |   26 |   100.00 | Using index condition |
+----+-------------+---------+------------+-------+---------------+----------+---------+------+------+----------+-----------------------+
1 row in set, 1 warning (0.00 sec)
```

* OR 前后存在非索引的列，索引失效
```sql
mysql> SHOW INDEX FROM student;
+---------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
| Table   | Non_unique | Key_name | Seq_in_index | Column_name | Collation | Cardinality | Sub_part | Packed | Null | Index_type | Comment | Index_comment | Visible | Expression |
+---------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
| student |          0 | PRIMARY  |            1 | id          | A         |      498372 |     NULL |   NULL |      | BTREE      |         |               | YES     | NULL       |
| student |          1 | idx_name |            1 | name        | A         |      346577 |     NULL |   NULL | YES  | BTREE      |         |               | YES     | NULL       |
+---------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
2 rows in set (0.01 sec)

mysql> CREATE INDEX idx_age ON student(age);
Query OK, 0 rows affected (1.34 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> EXPLAIN SELECT * FROM student WHERE age=20 OR name like 'abc%';
+----+-------------+---------+------------+-------------+------------------+------------------+---------+------+------+----------+-------------------------------------------------+
| id | select_type | table   | partitions | type        | possible_keys    | key              | key_len | ref  | rows | filtered | Extra                                           |
+----+-------------+---------+------------+-------------+------------------+------------------+---------+------+------+----------+-------------------------------------------------+
|  1 | SIMPLE      | student | NULL       | index_merge | idx_name,idx_age | idx_age,idx_name | 5,63    | NULL | 9885 |   100.00 | Using sort_union(idx_age,idx_name); Using where |
+----+-------------+---------+------------+-------------+------------------+------------------+---------+------+------+----------+-------------------------------------------------+
1 row in set, 1 warning (0.01 sec)

mysql> EXPLAIN SELECT * FROM student WHERE age=20 OR name like 'abc%' OR classId>20;
+----+-------------+---------+------------+------+------------------+------+---------+------+--------+----------+-------------+
| id | select_type | table   | partitions | type | possible_keys    | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+---------+------------+------+------------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | student | NULL       | ALL  | idx_name,idx_age | NULL | NULL    | NULL | 498372 |    41.97 | Using where |
+----+-------------+---------+------------+------+------------------+------+---------+------+--------+----------+-------------+
1 row in set, 1 warning (0.00 sec)
```


* 数据库和表的字符集统一使用utf8mb4
统一使用utf8mb4( 5.5.3版本以上支持)兼容性更好，统一字符集可以避免由于字符集转换产生的乱码。不
同的字符集进行比较前需要进行转换会造成索引失效。






## 关联查询优化


    

























