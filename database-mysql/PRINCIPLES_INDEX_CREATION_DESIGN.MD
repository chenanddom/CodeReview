[上一章(INNODB数据结构)](./INNODB_DATA_STRUCTURE.MD)
# 索引的创建和设计原则

## 索引的分类
MySQL的索引包括普通索引，唯一索引，全文索引，单列索引，多列索引，和空间索引

* 从**功能逻辑**上说,索引分别是普通索引，唯一索引，主键索引，全文索引。

* 按照**物理实现方式**，索引可以分为两种:聚簇索引和非聚簇索引。

* 按照**作用字段个数**进行划分，分成单列索引和联合索引。

1. 普通索引
在创建索引的时候，不附加恶人和限制条件，只是用于提高查询效率。这列索引可以创建在**任何数据类型**中，其值是否唯一和非空，要有字段本身的完整性约束条件
决定。创建索引以后，可以通过索引进行查询。例如，在student的字段name上建立一个普通索引，查询记录的时候就可以根据该索引进行查询。

2. 唯一索引
使用UNIQUE参数可以设置索引为唯一索引，在创建唯一索引的时候，限制该索引的值必须是唯一的，但是婿控制。在一章数据表李可以多个唯一索引。
例如在表student的字段email中创建唯一性索引，那么字段email的值就必须是唯一的，通过唯一性索引，可以更快速地确定某条记录。

3. 主键索引
主键索引就是一种特殊地唯一性索引，在唯一索引地基础上增加了非空地约束，也就是NOT NULL+QUNIQUE，一张表里最多只能有一个主键索引。
这是有主键索引的物理实现方式决定的，因为疏忽存储在文件中只能按照一种顺序进行存储。


4.单列索引
在表的单个字段上创建索引。单列索引只能根据该字段进行索引。单列索引可以是普通的索引，也可以是唯一性索引，还可以是全文索引。只要保证该索引只对应
一个字段即可。一个表可以有多个单列索引.

5. 多列索引
多列索引是在表的多个字段组合上创建一个索引。该索引指向创建时对应多个字段，可以通过这几个字段进行查询，但是只有拆线呢条件中使用了这些字段中的第一个字段
时才会被使用。例如表中的字段id，name和gender上建立一个索引idx_id_name_gender,只有在拆线呢条件中使用了字段id时该索引才会被使用。时组合索引时应该遵
循**最左前缀原则**。

6. 全文索引
全文索引(也称全文检索)时目前搜索引擎使用的一种关键技术。它能利用【分词技术】等多种算法只能分析处文本文字中关键词的频率和重要性，然后按照一定
的算法规则只能地筛选处我们想要地搜索结果。全文索引非常适合大型数据集，对于小地数据集，它地用户不大。
全文索引一般只能创建在CHAR,VARCHAR或者TEXT类型极其系列类型地字段上，查询数据量较大地字符串类型的字段时，使用全文索引可以提高查询速度。

7. 空间索引
使用**参数SPATIAL**可以设置索引为空间索引。空间索引只能建立在空间数据类型上，这样可以提高系统获取空间按的数据效率。MySQL中的空间类型包括GEOMETRY
,POINT,LINERSTRING和POLYGON等。目前只有MyISAM存储引擎支持空间检索，而且索引的字段不能为空值。对于初学者来说，这类索引很少会用到。

小结：不同的存储引擎支持的索引类型也不一样 
InnoDB ：支持 B-tree、Full-text 等索引，不支持 Hash索引；
MyISAM ： 支持 B-tree、Full-text 等索引，不支持 Hash 索引； 
Memory ：支持 B-tree、Hash 等索引，不支持 Full-text 索引； 
NDB ：支持 Hash 索引，不支持 B-tree、Full-text 等索引； 
Archive ：不支持 B-tree、Hash、Full-text 等索引；

## 创建索引


### 隐式创建
```sql
CREATE TABLE  table_name[col_name data_type]
[UNIQUE |FULLTEXT|SPATIAL][INDEX|KEY][index_name](col_name[length ])[ASC|DESC]
```
*　UNIQUE 、FULLTEXT 和SPATIAL 为可选参数，分别表示唯一索引、全文索引和空间索引；
* INDEX 与KEY 为同义词，两者的作用相同，用来指定创建索引；
* index_name 指定索引的名称，为可选参数，如果不指定，那么MySQL默认col_name为索引名；
* col_name 为需要创建索引的字段列，该列必须从数据表中定义的多个列中选择；
* length 为可选参数，表示索引的长度，只有字符串类型的字段才能指定索引长度；
* ASC 或DESC 指定升序或者降序的索引值存储。

例子
```sql
-- 创建普通索引
CREATE TABLE book(
book_id INT,
book_name VARCAHR(100),
authors VARCHAR(100),
info VARCHAR(100),
comment VARCHAR(100),
year_publication YEAR,
INDEX(year_publication)
);
-- 创建唯一索引
CREATE TABLE test1(
id INT NOT NULL,
name VARCHAR(30),
UNIQUE INDEX uk_idx_id(id));
-- 创建主键索引 (如果主键创建了auto_increment就无法删除)
CREATE TABLE student (
id INT(10) UNSIGNED AUTO_INCREMENT ,
student_no VARCHAR(200),
student_name VARCHAR(200),
PRIMARY KEY(id)
); 
-- 删除主键 ,上面的主键无法删除，因为添加了auto_increment属性
ALTER TABLE student DROP PRIMARY KEY;
-- 创建单列索引
CREATE TABLE test2(
id INT NOT_NULL,
name VARCHAR(50),
INDEX single_idx_name(name(20))
);
-- 创建组合索引
CREATE TABLE test3(
id INT(11) NOT NULL,
name CHAR(30) NOT NULL,
age INT(11) NOT NULL,
info VARCHAR(255),
INDEX multi_idx_id_name_age(id,name,age)
);
-- 创建全文索引
CREATE TABLE test4(
id INT NOT NULL,
name VARCHAR(32),
author VARCHAR(64),
info VARCHAR(255),
FULLTEXT INDEX ft_idx(info)
);
SHOW INDEX FROM test4;
ALTER TABLE test4 DROP INDEX ft_idx;
ALTER TABLE test4 ADD FULLTEXT INDEX ft_idx(info);
-- 创建空间索引
CREATE TABLE test5(
id INT NOT NULL,
geo GEOMETRY NOT NULL,
SPATIAL INDEX spa_idx_geo(geo)
);
```
* 注意:
    1. 全文索引中的数据一般不需要不需要使用like的查询方式，一般使用match(col1...) aginst('查询关键字');
    2. 需要注意到的时MySQL5.5以前除了MyISAM存储引擎才可以使用上面的匹配的方式
    3. 使用match(col...) aginst()比like模糊查询的速度高出很多。    

### 显示创建索引
如果表已经创建成功了，如果此时需要创建索引，用显示的添加索引
```sql
ALTER TABLE table_name ADD [UNIQUE|FULL TEXT|SPATIAL] [INDEX|KEY][index_name](col_name(length),...)[ASC|DESC];
```
或者
```sql
CREATE [UNIQUE|FULLTEXT|SPATIAL] INDEX index_name ON  table_name (col_name[length],...)[ASC|DESC]
```

## 索引设计原则

### 哪些字段适合创建索引
1. 字段的数值有唯一性的限制
```text
业务上具有唯一特性的字段面积十时组合字段也必须简称唯一的索引
说明:不要以为唯一索引影响了insert速度，这个速度时可以忽略不计的，但是提高查找速度时明显的。
```
2. 频繁作为WHERE查询条件的字段
某个字段在SELECT语句的WHERE条件中经常被使用到，那么就需要给这个字段创建索引了。尤其是在数据量大的情况下；
创建普通索引就可以答复提升疏忽的查询效率
例子：
```sql
-- 表在没有加索引的时候就执行需要535ms，
SELECT *FROM student_info WHERE student_id=140532;
-- 添加索引
ALTER TABLE student_info ADD KEY student_id_idx(student_id);
-- 再次执行上面的sql所消耗的时间就是33ms

```

3. 经常GROUP BY 和ORDER BY的列
索引就是让数据按照某种顺序进行存储或者检索，因此当我们使用GROUP BY对数据进行分组查询，或者使用ORDER BY 对树进行排序的时候，就需要对分组
或者排序的字段进行索引。如果待排序的列有多个，那么可以在这些列上建立组合索引。
```sql
SELECT student_id,count(*) "num" FROM student_info GROUP BY student_id

```
没有创建索引时的执行情况如下:
![group by创建索引之前](./files\索引的创建规则-1.PNG)
使用索引后
```sql
ALTER TABLE student_info ADD KEY student_id_idx(student_id);
```
![grouo by创建索引之后](./files\索引的创建规则-2.PNG)

如果是GROUP BY 和ORDER BY 都使用了那就可以创建一个联合索引
```sql
SELECT student_id,count(*) "num" FROM student_info GROUP BY student_id ORDER BY create_time DESC
```
没有创建索引之前结果如下:
![没有创建索引之前](./files\索引的创建规则-3.PNG)
```sql
-- 创建联合索引
ALTER table student_info ADD INDEX idx_student_createtime(student_id,create_time DESC);
```
![创建联合索引之后再此执行可以得到如下结果](./files\索引的创建规则-4.PNG)

4. UPDATE、DELETE 的 WHERE 条件列
对数据按照某个条件进行查询后再进行 UPDATE 或 DELETE 的操作，如果对 WHERE 字段创建了索引，就
能大幅提升效率。原理是因为我们需要先根据 WHERE 条件列检索出来这条记录，然后再对它进行更新或
删除。如果进行更新的时候，更新的字段是非索引字段，提升的效率会更明显，这是因为非索引字段更
新不需要对索引进行维护。

5.DISTINCT 字段需要创建索引
有时候我们需要对某个字段进行去重，使用 DISTINCT，那么对这个字段创建索引，也会提升查询效率。
比如，我们想要查询课程表中不同的 student_id 都有哪些，如果我们没有对 student_id 创建索引，执行
SQL 语句：
```sql
SELECT DISTINCT(student_id) FROM `student_info`;
```

5. DISTINCT字段需要创建索引
有时候我们需要对某个字段进行去重，使用 DISTINCT，那么对这个字段创建索引，也会提升查询效率。
比如，我们想要查询课程表中不同的 student_id 都有哪些，如果我们没有对 student_id 创建索引，执行
SQL 语句：
```sql
SELECT DISTINCT(student_id) FROM `student_info`;
```
运行结果（600637 条记录，运行时间 0.683s ）：
如果我们对 student_id 创建索引，再执行 SQL 语句：
```sql
SELECT DISTINCT(student_id) FROM `student_info`;
```
运行结果（600637 条记录，运行时间 0.010s ）：
你能看到 SQL 查询效率有了提升，同时显示出来的 student_id 还是按照递增的顺序进行展示的。这是因
为索引会对数据按照某种顺序进行排序，所以在去重的时候也会快很多

6. 多表JOIN连接操作时，创建索引注意事项
首先， 连接表的数量尽量不要超过 3 张，因为每增加一张表就相当于增加了一次嵌套的循环，数量级增
长会非常快，严重影响查询的效率。
其次， 对 WHERE 条件创建索引，因为 WHERE 才是对数据条件的过滤。如果在数据量非常大的情况下，
没有 WHERE 条件过滤是非常可怕的。
最后， 对用于连接的字段创建索引，并且该字段在多张表中的类型必须一致。比如 course_id 在
student_info 表和 course 表中都为 int(11) 类型，而不能一个为 int 另一个为 varchar 类型。

```sql
SELECT
	s.student_id,
	s.NAME,
	c.course_name 
FROM
	student_info s
	JOIN course c ON s.course_id = c.course_id 
WHERE
	s.name ='cBAXkj'
```
![没创建索引前](./files\索引的创建规则-6.PNG)

```sql
ALTER TABLE student_info ADD INDEX idx_course_id(course_id);

	
ALTER TABLE student_info ADD INDEX idx_name(name);

	
ALTER TABLE course ADD INDEX idx_course_id(course_id);
```
![创建索引后](./files\索引的创建规则-7.PNG)

7. 使用列的类型小的创建索引
    * 数据类型越小，在查询的时候比较操作越快
    * 数据类型越小，索引占用的存储空间就越小，在一个数据页内就可以放下更多的记录，从而减少磁盘的I/O带来的性能损耗，也就以为着可以把更多
    的数据页缓存在内存中，从而加快读取的效率.


8. 使用字符串前缀创建索引
假设我们的字符串很长，那存储的一个字符串就需要占用很大的存储空间。我们需要为这个自渡川建立索引时，那就意味着在对于的B+树中有这么两个问题。
* B+树索引中的记录需要把该列的完整字符串存储起来，更费时。而且字符串越长，索引中占用的存储空间越大。
* 如果B+树索引中索引列存储的字符串很长，那在做字符串比较时会占用更多的时间，
我们可以通过截取字段的前面的一部分内容建立索引，这个就叫前缀索引 。这样在查找记录的时候虽然不能精确的定位到记录的为止，但是能定位到相应的
前缀所在的为止，然后根据前缀相同的记录的主键值徽标查完整的肌肤穿值。即节约空间，有减少了字符串的比较时间，还答题能解决排序的问题。

问题是，截取多少呢？截取得多了，达不到节省索引存储空间的目的；截取得少了，重复内容太多，字
段的散列度(选择性)会降低。怎么计算不同的长度的选择性呢？
可以使用下面的公式进行计算:
```sql
count(distinct left(列名, 索引长度))/count(*)
```
在 varchar 字段上建立索引时，必须指定索引长度，没必要对全字段建立索引，根据实际文本
区分度决定索引长度。
说明：索引的长度与区分度是一对矛盾体，一般对字符串类型数据，长度为 20 的索引，区分度会高达
90% 以上，可以使用 count(distinct left(列名, 索引长度))/count(*)的区分度来确定。

9. 区分度高(散列性高)的列适合作为索引

10. 使用最频繁的列放到联合索引的左侧

11. 在多个字段都要创建索引的情况下，联合索引优于单值索引

* 限制索引的数目
在实际的工作中，我们也需要注意平衡，索引的数目不是越多越好。我们需要限制每张表上索引的数量，将以单张表索引数量不超过6个，原因
①每个索引都需要占用从盘空间，索引越多，需要的磁盘空间越大。
②索引会影响INSERT,DELETE,UPDATE等语句的性能，因为表中的数据更改的同时，索引也会进行调正和更新，会造成负担。
③优化器在选择优化拆线呢的时候，会更具统一信息，对每一个可以用到的索引进行评估，以生出一个最好的执行计划，如果同时有 很多个索引都可以用于查询，
会曾华MySQL优化器生成执行计划的时间,降低查询的性能.


## 哪些情况不适合创建索引？
1. 在where中使用不到的字段，不要设置索引

2. 数据量小的表最好不要使用索引

3. 有大量重复数据的列上不要建立索引
结论：当数据重复度大，比如高于 10% 的时候，也不需要对这个字段使用索引。   

4. 避免对经常更新的表创建过多的索引
    1. 索引需要维护
    2. 查询速度提高，但是插入更新的速度大大变慢.
    

5. 不建议用无序的值作为索引
    例如身份证、UUID(在索引比较时需要转为ASCII，并且插入时可能造成页分裂)、MD5、HASH、无序长字
    符串等。

6. 删除不再使用或者很少使用的索引

7. 不要定义冗余或重复的索引


[下一章(性能分析工具的使用)](./USE_OF_PERFORMANCE_ANALYSIS_TOOLS.MD)