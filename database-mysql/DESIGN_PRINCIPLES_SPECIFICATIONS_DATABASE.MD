[上一章(索引优化和查询优化)](./QUERY_OPTIMIZATION_INDEX_OPTIMIZATION.MD)
# 数据库的设计原则和规范

## 范式
在关系型数据库中，关于数据表设计的基本原则、规则就称为范式。可以理解为，一张数据表的设计结
构需要满足的某种设计标准的级别。要想设计一个结构合理的关系型数据库，必须满足一定的范式。

目前关系型数据库有六种常见范式，按照范式级别，从低到高分别是：第一范式（1NF）、第二范式
（2NF）、第三范式（3NF）、巴斯-科德范式（BCNF）、第四范式(4NF）和第五范式（5NF，又称完美
范式）。

### 键和相关属性的概念

*超键:在关系中能唯一标识元组的属性称为关系模式的超键
*候选键:不含多余属性超键称为候选键。也就是在候选键中，若再删除就不是键了。
*主键:用户选做元组标识的一个候选键程序主键
*外键:若时关系模式R中属性K是其他模式的主键，那么k在模式R中称为外键.

举例子:

| 学号 | 姓名 | 性别 | 年龄 | 系别 | 专业 |
| ---- | ---- | ---- | ---- | ---- | ---- |
|20020612 |李辉 |男 |20 |计算机 |软件开发 |
|20060613| 张明| 男 |18 |计算机 |软件开发 |
|20060614| 王小玉| 女 |19 |物理 |力学 |
|20060615| 李淑华| 女 |17 |生物 |动物学| 
|20060616| 赵静| 男 |21 |化学 |食品化学 |
|20060617| 赵静| 女 |20 |生物 |植物学总结|

1. 超键
在关系中能唯一标识元组的属性集称为关系模式的超键。
    因而咱们从例子中能够发现，学号是标识学生实体的唯一标识。
    除此以外咱们还能够把它跟其余属性组合起来:
    ('学号','性别'),('学号','年龄')。像这样也是超键
2.  候选键
不含有多余的属性的超键称为候选键
根据例子可知，学号是一个能够唯一标识元组的唯一标识，所以学号是一个候选键，实际上，候选键是超键的子集，好比(学号,年龄)是超键，可是它不是
候选键。由于它存在额外的属性。

3. 主键
用户选择的候选键作为该元组的唯一标识，那么它就为主键。
简单的说，例子中的元组的候选键为学号，可是咱们选定的它作为该元组的唯一标识，那么学号就为主键。


4. 外键
    外键是相对于主键的，好比在学生的记录里，主键为学号，在成绩单表中也有学号字段，所以学号为成绩单表的外键，为学生表的主键。




### 第一范式
数据库表的每一列都是不可分割的原子项
例子：
![第一范式拆分前](./files/数据库设计原则-1.png)
应该拆分为下图:
![第一范式拆分后](./files/数据库设计原则-2.png)

### 第二范式
每个表必须有且仅有一个数据元素为主键(Primary key),其他属性需完全依赖于主键
举例子:
比赛表 player_game ，里面包含球员编号、姓名、年龄、比赛编号、比赛时间和比赛场地等属性，这
里候选键和主键都为（球员编号，比赛编号），我们可以通过候选键（或主键）来决定如下的关系：
```text
(球员编号, 比赛编号) → (姓名, 年龄, 比赛时间, 比赛场地，得分)
```
但是这个数据表不满足第二范式，因为数据表中的字段之间还存在着如下的对应关系：  
```text
(球员编号) → (姓名，年龄)
(比赛编号) → (比赛时间, 比赛场地)
```
对于非主属性来说，并非完全依赖候选键。这样会产生怎样的问题呢？
```text
1. 数据冗余：如果一个球员可以参加 m 场比赛，那么球员的姓名和年龄就重复了 m-1 次。一个比赛
也可能会有 n 个球员参加，比赛的时间和地点就重复了 n-1 次。
2. 插入异常：如果我们想要添加一场新的比赛，但是这时还没有确定参加的球员都有谁，那么就没
法插入。
3. 删除异常：如果我要删除某个球员编号，如果没有单独保存比赛表的话，就会同时把比赛信息删
除掉。
4. 更新异常：如果我们调整了某个比赛的时间，那么数据表中所有这个比赛的时间都需要进行调
整，否则就会出现一场比赛时间不同的情况。
```
### 第三范式
数据表中的每一列都和主键直接相关，而不能间接相关联,不能有传递依赖的关系
举例子:
球员player表：球员编号、姓名、球队名称和球队主教练。现在，我们把属性之间的依赖关系画出
来，如下图所示： 
![第三范式](./files/数据库设计原则-3.png)
能看到球员编号决定了球队名称，同时球队名称决定了球队主教练，非主属性球队主教练就会传递依
赖于球员编号，因此不符合 3NF 的要求。
如果要达到 3NF 的要求，需要把数据表拆成下面这样：

|表名|属性(字段)|
| ---- | ---- |
|球员表|球员编号，姓名和球队名称|
|球队表|球队名称，球队主教练|


### 巴斯-科德BCNF
首先我们需要确认造成异常的原因：主属性仓库名对于候选键（管理员，物品名）是部分依赖的关系，
这样就有可能导致上面的异常情况。因此引入BCNF，它在 3NF 的基础上消除了主属性对候选键的部分依
赖或者传递依赖关系。
例子:
![三范式设计的表 ](./files/数据库设计原则-4.png)
在这个表中，一个仓库只有一个管理员，同时一个管理员也只管理一个仓库。我们先来梳理下这些属性
之间的依赖关系。
仓库名决定了管理员，管理员也决定了仓库名，同时（仓库名，物品名）的属性集合可以决定数量这个
属性。这样，我们就可以找到数据表的候选键。
候选键：是（管理员，物品名）和（仓库名，物品名），然后我们从候选键中选择一个作为主键，比
如（仓库名，物品名）。
主属性：包含在任一候选键中的属性，也就是仓库名，管理员和物品名。
非主属性：数量这个属性。

2. 是否符合三范式
如何判断一张表的范式呢？我们需要根据范式的等级，从低到高来进行判断。
首先，数据表每个属性都是原子性的，符合 1NF 的要求；
其次，数据表中非主属性”数量“都与候选键全部依赖，（仓库名，物品名）决定数量，（管理员，物品
名）决定数量。因此，数据表符合 2NF 的要求；
最后，数据表中的非主属性，不传递依赖于候选键。因此符合 3NF 的要求。

3. 存在的问题
既然数据表已经符合了 3NF 的要求，是不是就不存在问题了呢？我们来看下面的情况：
1. 增加一个仓库，但是还没有存放任何物品。根据数据表实体完整性的要求，主键不能有空值，因
此会出现插入异常；
2. 如果仓库更换了管理员，我们就可能会修改数据表中的多条记录；
3. 如果仓库里的商品都卖空了，那么此时仓库名称和相应的管理员名称也会随之被删除。
你能看到，即便数据表符合 3NF 的要求，同样可能存在插入，更新和删除数据的异常情况。
4. 问题解决:
根据 BCNF 的要求，我们需要把仓库管理关系 warehouse_keeper 表拆分成下面这样：
仓库表：（仓库名，管理员）
库存表：（仓库名，物品名，数量）    

### 反范式化

```text
1. 为满足某种商业目标 , 数据库性能比规范化数据库更重要
2. 在数据规范化的同时 , 要综合考虑数据库的性能
3. 通过在给定的表中添加额外的字段，以大量减少需要从中搜索信息所需的时间
4. 通过在给定的表中插入计算列，以方便查询
```

* 反范式的新问题
    * 存储空间变大了
    * 一个表中字段做了修改，另一个表中冗余的字段也需要做同步修改，否则数据不一致
    * 若采用存储过程来支持数据的更新、删除等额外操作，如果更新频繁，会非常消耗系统资源
    * 在数据量小的情况下，反范式不能体现性能的优势，可能还会让数据库的设计更加复杂


## ER模型
ER 模型中有三个要素，分别是实体、属性和关系。
实体，可以看做是数据对象，往往对应于现实生活中的真实存在的个体。在 ER 模型中，用矩形来表
示。实体分为两类，分别是强实体和弱实体。强实体是指不依赖于其他实体的实体；弱实体是指对另
一个实体有很强的依赖关系的实体。
属性，则是指实体的特性。比如超市的地址、联系电话、员工数等。在 ER 模型中用椭圆形来表示。
关系，则是指实体之间的联系。比如超市把商品卖给顾客，就是一种超市与顾客之间的联系。在 ER 模
型中用菱形来表示。
注意：实体和属性不容易区分。这里提供一个原则：我们要从系统整体的角度出发去看，可以独立存在
的是实体，不可再分的是属性。也就是说，属性不能包含其他属性。  

## 数据库对象编写建议
### 关于库
1. [强制]库的名称必须控制在32个字符以内，只能使用英文字母、数据和下划线,建议以英文字母开头。
2. [强制]库名中英文一律小写，不同单词采用下划线份个。需见名知意。
3. [强制]库的名称格式:业务系统名称_子系统名称.
4. [强制]库名禁止使用关键字(如type,order等)
5. [强制]创建数据库时必须显示指定字符集，并且字符集只能时utf8或者utf8mb4：CREATE DATABASE DBNAME DEFAULT CHARACTER SET 'utf8';
6. [建议]对于数据库连接数据库账号，尊选权限最小原则。使用数据库账号只能在一个DB下使用，不准跨库。程序使用账号原则上不准有drop权限。
7. [建议]临时库以tmp_为前缀，并且以日期为后缀;备份库以bak_为前缀，并以日期为后缀。

### 关于表，列
1. [强制]表和列的名称必须控制在32个字符以内，表名只能使用英文字母，数据和下划线，建议以英文字母开头。
2. [强制]表名,列名一律小写，不同单词采用菜花香份个，须见名知意。
3. [强制]表名和模块名强相关，同一模块的表名尽量使用统一前缀。比如crm_fund_item
4. [强制]创建表时必须要显式指定字符集为utf8或者utf8mb4
5. [强制]表名和列名禁止使用关键字(如type，order等)
6. [强制]创建表时必须显式指定表存储引擎类型。如无特殊需求，一律为InnoDB.
7. [强制]建表必须有comment.
8. [强制]字段命名应尽可能使用表达实际含义的英文单词或缩写。如:公司ID，不要使用corporation_id，而使用corp_id即可.
9. [强制]不二类型的字段名为is_描述。如member表上标识是否为enabled的会员的字段名为is_enbled。
10. [强制]金直在数据库中存储图片，文件等大的二进制数据。’通常文件很大，短时间内造成数据量快速增长，数据库进行数据库读取时，通常会进行大量的随
机IO操作，文件很大时，IO操作很耗时。通常存储于文件服务器，数据库只存储文件地址信息。
11.[建议]建表时关于主键:表必须要有主键(1)强制要求主键为id，类型为int或者bigint，且为auto_increment建议使用unsigned无符号型。(2)标识
表里每一行主题的字段不要设置为主键，建议设为其他字段的如user_id,order_id等，并建立unique_key索引。因为如果设为主键且主键值为随机插入，则
会导致innodb内部页分裂和大量的随机I/O，性能急剧下降。
12.[建议]核心表(如用户表)必须要要有数据的创建时间字段(create_time)和最后更新的时间字段(update_time)，便于查找问题。
13. [建议]表中的所有字段尽量都是NOT_NULL属性业务可以根据需要定义DEFAULT值。 因为使用NULL值会存在每一行都会占用额外存储空间、数据迁移
容易出错、聚合函数计算结果偏差等问题。
14. [建议]所有存储相同数据的列名和列类型必须一致（一般作为关联列，如果查询时关联列类型不一致会自动进行数据类型隐式转换，会造成列上的索引失效，导致查询效率降低）
15. [建议]中间表（或临时表）用于保留中间结果集，名称以tmp_ 开头。 备份表用于备份或抓取源表快照，名称以bak_ 开头。中间表和备份表定期清理。


### 关于索引
1.[强制]InnoDB表必须主键为id int/bigint auto_increment，且主键值金直被更新
2.[强制]InnoDB和MyISAM存储引擎表，索引类型必须为BTREE。
3.[建议]主键的名称pk_开头，唯一键一uni_或者uk_开头,普通索引一idx_开头，一律使用小写格式，以字段的名称或者缩写作为后缀。
4.[建议]多单词组成columnname,取前几个单词首字母，加末单词组成column_name.如sample表member_id是的索引：idx_sample_mid.
5.[建议] 单个表上的索引个数不能超过6个。
6.[建议]在建立索引时，多考虑建立联合索引，并把区分度最高的字段放在最前面。
7.[建议]在多表JOIN的SQL里，保证被驱动表的连接列上有索引，这样JOIN执行效率最高。
8.[建议]建表或者加索引时，保证表里互相不存在冗余索引。比如:如果表里以及存在key(a,b),则key(a)为冗余索引，需要删除。

### SQL编写
1. [强制]程序端SELECT语句必须指定具体名称，金直携程*;
2. [建议]程序端INSERT语句指定具体字段名称，不要携程INSERT INTO t1 VALUES(...).
3. [建议]除静态表或小表(100行以内),DML语句必须要有WHERE条件，且使用索引查找。
4. [建议]INSERT INTO ... VALUES(xx),(xx),(xx)..这里xx的值不要超过5000个。值过多虽然上线很快，但是会引擎主从同步的延迟。
5. [建议]SELECTS语句不要使用UNION,推荐使用UNION ALL，并且UNION子句个数限制在5以内。
6. [建议]线上环境，多表JOIN不要超过5个表.
7. [建议]减少使用ORDER BY，和业务沟通能不能不排序或者将排序放到程序端取做。ORDER BY,GROUO BY,DISTINCT这些语句较为耗费CPU，数据库的CPU
资源时极其宝贵的，
8.[建议]包含了ORDR BY,GROUP BY,DISTINCT这些查询的语句，WHERE条件过滤出来的结果集保持在1000行以内，否则SQL会变慢。
9.[建议]对单表的多次alter操作必须合并为一次。
对于超过100W行的大表进行alter table，必须经过DBA审核，并在业务低峰期执行，多个alter需整
合在一起。 因为alter table会产生表锁，期间阻塞对于该表的所有写入，对于业务可能会产生极
大影响
10.[建议]批量操作数据时，需要控制事务处理间隔时间，进行必要的sleep。
11.[建议]事务里包含SQL不超过5个,因为过长的事务会导致锁数据较久，MySQL内部缓存、连接消耗过多等问题。
12.[建议]事务里更新语句尽量基于主键或UNIQUE KEY，如UPDATE… WHERE id=XX;
否则会产生间隙锁，内部扩大锁定范围，导致系统性能下降，产生死锁。

[下一章(数据库调优策略)](./TUNING_STRATEGY.MD)






































