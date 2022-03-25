[上一章(数据库优化策略)](./TUNING_STRATEGY.MD)
# 数据库事务
SHOW ENGINES命令来查看当前MySQL支持的存储引擎都有哪些，以及这额存储引擎是否支持事务
![SHOW ENGINES查看MySQL支持的存储引擎](./files\数据库事务-1.PNG)

* 基本概念
事务：一组逻辑操作单元，使数据从一种状态变换到另一种状态。
事务处理的原则：保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。当在一个事务中执行多个操作时，要么所有的事
务都被提交( commit )，那么这些修改就永久地保存下来；要么数据库管理系统将放弃所作的所有修改，整个事务回滚( rollback )到最初状态。

* 事务的ACID特性

    * 原子性（atomicity）：
        原子性是指事务是一个不可分割的工作单位，要么全部提交，要么全部失败回滚。
    * 一致性（consistency）：
        （国内很多网站上对一致性的阐述有误，具体你可以参考 Wikipedia 对Consistency 的阐述）
        根据定义，一致性是指事务执行前后，数据从一个合法性状态变换到另外一个合法性状态。这种状态
        是语义上的而不是语法上的，跟具体的业务有关。
        那什么是合法的数据状态呢？满足预定的约束的状态就叫做合法的状态。通俗一点，这状态是由你自己
        来定义的（比如满足现实世界中的约束）。满足这个状态，数据就是一致的，不满足这个状态，数据就
        是不一致的！如果事务中的某个操作失败了，系统就会自动撤销当前正在执行的事务，返回到事务操作
        之前的状态。
    * 隔离型（isolation）：
        事务的隔离性是指一个事务的执行不能被其他事务干扰，即一个事务内部的操作及使用的数据对并发的
        其他事务是隔离的，并发执行的各个事务之间不能互相干扰。
        如果无法保证隔离性会怎么样？假设A账户有200元，B账户0元。A账户往B账户转账两次，每次金额为50
        元，分别在两个事务中执行。如果无法保证隔离性，会出现下面的情形：
```sql
UPDATE accounts SET money = money - 50 WHERE NAME = 'AA';
UPDATE accounts SET money = money + 50 WHERE NAME = 'BB';
```
![](./files\数据库事务-2.png)
    *　持久性（durability）：
        持久性是指一个事务一旦被提交，它对数据库中数据的改变就是永久性的，接下来的其他操作和数据库
        故障不应该对其有任何影响。
        持久性是通过事务日志来保证的。日志包括了重做日志和回滚日志。当我们通过事务对数据进行修改
        的时候，首先会将数据库的变化信息记录到重做日志中，然后再对数据库中对应的行进行修改。这样做
        的好处是，即使数据库系统崩溃，数据库重启后也能找到没有更新到数据库系统中的重做日志，重新执
        行，从而使事务具有持久性。

* 事务的状态
我们现在知道事务是一个抽象的概念，它其实对应着一个或多个数据库操作，MySQL根据这些操作所执
行的不同阶段把事务大致划分成几个状态：
1.活动的（active）
    事务对应的数据库操作正在执行过程中时，我们就说该事务处在活动的状态。
    部分提交的（partially committed）
    当事务中的最后一个操作执行完成，但由于操作都在内存中执行，所造成的影响并没有刷新到磁盘
    时，我们就说该事务处在部分提交的状态。
2. 失败的（failed）
    当事务处在活动的或者部分提交的状态时，可能遇到了某些错误（数据库自身的错误、操作系统
    错误或者直接断电等）而无法继续执行，或者人为的停止当前事务的执行，我们就说该事务处在失
    败的状态。
```sql
    UPDATE accounts SET money = money - 50 WHERE NAME = 'AA';
    UPDATE accounts SET money = money + 50 WHERE NAME = 'BB';
```    
3. 中止的（aborted）
    如果事务执行了一部分而变为失败的状态，那么就需要把已经修改的事务中的操作还原到事务执
    行前的状态。换句话说，就是要撤销失败事务对当前数据库造成的影响。我们把这个撤销的过程称
    之为回滚。当回滚操作执行完毕时，也就是数据库恢复到了执行事务之前的状态，我们就说该事
    务处在了中止的状态。
    举例：
4. 提交的（committed）
    当一个处在部分提交的状态的事务将修改过的数据都同步到磁盘上之后，我们就可以说该事务处
    在了提交的状态。
一个基本的状态转换图如下所示：


![数据库事务的状态转换](./files\数据库事务-3.png)



##事务的使用

*　显式事务
1. START TRANSACTION 或者BEGIN,作用时开启显示的一个事务.
```sql
BEGIN;
START TRANSACTION; 
```
   START TRANSACTION 语句相较于BEGIN特别之处在于，后面能够根据几个修饰符:
        1. READ ONLY:表示当前事务时一个只读事务，也就是术语该事务的数据库操作只能读取数据，而不能修改数据。
            补充：只读事务中只是不允许修改哪些其他事务也能访问到的表中的数据，对于临时表来说(我们使用CREATE TEMPORARY TABLE创建的表)，
            由于它们只能在当前会话中可见，所以只读事务其实也是可以堆临时表进行增、删、该操作的。
        2. READ WRITE:表四当前事务时一个读写事务，也就是属于该事务的数据库操作既可以读取数据，爷可以修改数据.
        3. WITH CONSISTENT SNAPSHOT:启动一致性读。
2. 一系列的DML

3. 提交事务或者重视事务
```sql
COMMIT;
ROLLBACK;
ROLLBACK TO [savepoint]
```
关于savepoint线管的操作有:
```sql
-- 在事务中创建保存点，方便后面针对保存点进行回滚，一个事务中可以保存多个保存点。
SAVEPOINT 保存点名称;
-- 删除某个保存点
RELEASE SAVEPOINT 保存点名称;

```


* 隐式事务
```sql
-- 查看系统变量autocommit是否开启自动提交事务的
SHOW VARIABLES LIKE '%autocommit%';
```
![查看autocommit是否开启自动提交事务](./files\数据库事务-4.PNGs)
如果我们想关闭这种自动提交的功能，可以使用下面两种方式:
1. 显示的使用START TRANSACTION 或者 BEGIN语句开启一个事务。这样在本次事务提交或者 回滚前会暂时关闭自动提交的功能。
2. 把系统变量autocommit的值设置位OFF，就想这样
```sql
SET autocommit=OFF;
或者
SET autocommit=0;
```

    * 隐式提交数据的情况
        1.数据定义语言(Data definition language,缩写DDL)
        数据库对象，指的就是数据库，表，视图，存储过程等结构，当我们使用CREATE,ALTER,DROP等语句区修改数据库对象时，就会隐式的提交前面语句所属的事务。
        即：
```sql
BEGIN;
SELECT ...#事务中的一条语句
UPDATE ...#事务中的一条语句
...#事务的其他语句
CREATE TABLE ...#此语句会隐式的提交前面语句所属的事务。
```        
         2. 隐式使用或者修改mysql数据库中的表
            当我们使用ALTER USER,CREATE USER,GRANT,RENAME USER,REVOLK,SET PASSWD等语句时也会隐式的提交前边语句所属的事务。
         
         3. 事务控制或者关于锁定的语句
            当我们在一个事务还没提交或者回滚时又使用了START TRANSACTION或者BEGIN语句开启另一个事务时，会隐式的提交上一个事务
```sql
BEGIN ;

SELECT ...#事务中的一条语句
UPDATE ...#事务中的一条语句
...#事务中的其它语句
BEGIN;#此语句会影视的提交前面的语句所属的事务。
```                  
               当前的autocommit=OFF的时候，如果我们将autocommit=ON,也会隐式的提交前面的事务的。
               
               使用LOCK TABLES,UNLOCK TABLES等关于锁定的语句也会隐式的提交前边语句所属的事务。
           4. 加载数据的语句
                使用LOAD DATA语句来批量加载数据库中的导入数据时，也会隐式的提交前边的语句所属的事务。
           5. 关于MySQL复制的一些语句
                使用START SALVE,STOP SLAVE,REST SLAVE,CHANGE MASTER TO等玉壶时都会隐式提交前面的语句所属的事务。
           6. 其他的一些语句
                使用ANALYZE TABLE,CACHE INDEX,CHECK TABLE,FLUSH,LOAD INDEX INTO CACHE,OPTIMIZE TABLE,REPAIR TABLE,REST
                等语句也会隐式的提交前边语句所属的事务。                 

* 事务的回滚
![事务回滚的例子](./files\数据库事务-5.PNG)






## 事务的隔离级别

* 数据并发问题
针对事务的隔离性和并发性，我们怎么做取舍呢？先看一下访问相同数据的事务在不保证串行执行（也
就是执行完一个再执行另一个）的情况下可能会出现哪些问题：
1. 脏写（ Dirty Write ）

对于两个事务 Session A、Session B，如果事务Session A 修改了另一个未提交事务Session B 修改过的数
据，那就意味着发生了脏写
CREATE TABLE student (
studentno INT,
name VARCHAR(20),
class varchar(20),
PRIMARY KEY (studentno)
) Engine=InnoDB CHARSET=utf8;
INSERT INTO student VALUES(1, '小谷', '1班');
mysql> select * from student;
+-----------+--------+-------+
| studentno | name | class |
+-----------+--------+-------+
| 1 | 小谷 | 1班 |
+-----------+--------+-------+
1 row in set (0.00 sec)


2. 脏读（ Dirty Read ）

对于两个事务 Session A、Session B，Session A 读取了已经被 Session B 更新但还没有被提交的字段。
之后若 Session B 回滚，Session A 读取的内容就是临时且无效的。
Session A和Session B各开启了一个事务，Session B中的事务先将studentno列为1的记录的name列更新
为'张三'，然后Session A中的事务再去查询这条studentno为1的记录，如果读到列name的值为'张三'，而
Session B中的事务稍后进行了回滚，那么Session A中的事务相当于读到了一个不存在的数据，这种现象
就称之为脏读。


3. 不可重复读（ Non-Repeatable Read ）

对于两个事务Session A、Session B，Session A 读取了一个字段，然后 Session B 更新了该字段。 之后
Session A 再次读取同一个字段， 值就不同了。那就意味着发生了不可重复读。
我们在Session B中提交了几个隐式事务（注意是隐式事务，意味着语句结束事务就提交了），这些事务
都修改了studentno列为1的记录的列name的值，每次事务提交之后，如果Session A中的事务都可以查看
到最新的值，这种现象也被称之为不可重复读。


4. 幻读（ Phantom ）

对于两个事务Session A、Session B, Session A 从一个表中读取了一个字段, 然后 Session B 在该表中插
入了一些新的行。 之后, 如果 Session A 再次读取同一个表, 就会多出几行。那就意味着发生了幻读。
Session A中的事务先根据条件 studentno > 0这个条件查询表student，得到了name列值为'张三'的记录；
之后Session B中提交了一个隐式事务，该事务向表student中插入了一条新记录；之后Session A中的事务
再根据相同的条件 studentno > 0查询表student，得到的结果集中包含Session B中的事务新插入的那条记
录，这种现象也被称之为幻读。我们把新插入的那些记录称之为幻影记录。


* SQL的四种隔离级别

上面介绍了几种并发事务执行过程中可能遇到的一些问题，这些问题有轻重缓急之分，我们给这些问题
按照严重性来排一下序：
我们愿意舍弃一部分隔离性来换取一部分性能在这里就体现在：设立一些隔离级别，隔离级别越低，并
发问题发生的就越多。SQL标准中设立了4个隔离级别：
READ UNCOMMITTED ：读未提交，在该隔离级别，所有事务都可以看到其他未提交事务的执行结
果。不能避免脏读、不可重复读、幻读。
READ COMMITTED ：读已提交，它满足了隔离的简单定义：一个事务只能看见已经提交事务所做
的改变。这是大多数数据库系统的默认隔离级别（但不是MySQL默认的）。可以避免脏读，但不可
重复读、幻读问题仍然存在。
REPEATABLE READ ：可重复读，事务A在读到一条数据之后，此时事务B对该数据进行了修改并提
交，那么事务A再读该数据，读到的还是原来的内容。可以避免脏读、不可重复读，但幻读问题仍
然存在。这是MySQL的默认隔离级别。
SERIALIZABLE ：可串行化，确保事务可以从一个表中读取相同的行。在这个事务持续期间，禁止
其他事务对该表执行插入、更新和删除操作。所有的并发问题都可以避免，但性能十分低下。能避
免脏读、不可重复读和幻读。
SQL标准中规定，针对不同的隔离级别，并发事务可以发生不同严重程度的问题，具体情况如下：

|隔离级别|藏独可能性|不可重复读可能性|幻读可能性|
| ---- | ---- | ---- | ---- |
|READ UNCOMMITED|Yes|Yes|Yes|
|READ COMMIT|No|Yes|Yes|
|REPEATABLE READ|No|No|Yes|
|SERIALIZABLE|No|No|No|











