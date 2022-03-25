[上一章(MySQL事务)](./MYSQL_TRANSACTION.MD)
# MySQL事务日志
事务有4种特性:原子性，一致性，隔离性和持久性。那么事务的这四种特性时基于什么机制实现的呢？
* 事务的隔离性有锁机制实现。
* 事务的原子性，一致性和持久性由事务的redo日志和undo日志来保证。

    * REDO LOG称为重做日志，提供在写入操作，回复提交事务修改的爷操作，用来保证事务的持久性。
    
    * UNDO LOG称为回滚日志，回滚日志，回滚行记录到某个特定版本，用来保证事务的原子性，一致性。
有的DBA或许会认为 UNDO 是 REDO 的逆过程，其实不然。REDO和UNDO都可以视为是一种回府操作，但是：
* redo log:是存储引擎层(innodb)生成的日志，记录的是"**物理级别**"上的页修改操作，比如页号xxx,偏移量yyy写入的'zz'数据。主要为了保证数据的
可靠性
* undo log:是存储引擎层(innodb)生成日志，记录的是**逻辑操作**日志，比如对某一行数据进行了insert语句操作，那么undo log就记录一条与之相
反的DELETE操作。主要用于事务的回滚(undo log记录的是每个修改操作的你操作)和一致性非锁定读(undo log回滚行记录到某种特定的版本---MVCC，即多版本并发控制。)

## redo日志
InnoDB存储引擎是以页位单位来管理存储空间的。在真正访问页面之前，需要把在磁盘上的页缓存到内存中的Buffer Pool之后才可以访问。所有的变更都
必须先更新缓冲池中的数据，然后缓冲池中的脏页会以一定的频率被刷入磁盘(checkpoint机制)，通过缓冲池来优化CPU和磁盘之间的鸿沟，这样就可以保
证整体的性能不会下降太快

* 为什么需要REDO日志
一方面，缓冲池可以帮助我们消除CPU和磁盘之间的鸿沟，checkpoint机制可以保证数据的最终落盘，然
而由于checkpoint 并不是每次变更的时候就触发的，而是master线程隔一段时间去处理的。所以最坏的情
况就是事务提交后，刚写完缓冲池，数据库宕机了，那么这段数据就是丢失的，无法恢复。
另一方面，事务包含持久性的特性，就是说对于一个已经提交的事务，在事务提交后即使系统发生了崩
溃，这个事务对数据库中所做的更改也不能丢失。
那么如何保证这个持久性呢？ 一个简单的做法：在事务提交完成之前把该事务所修改的所有页面都刷新
到磁盘，但是这个简单粗暴的做法有些问题
另一个解决的思路：我们只是想让已经提交了的事务对数据库中数据所做的修改永久生效，即使后来系
统崩溃，在重启后也能把这种修改恢复出来。所以我们其实没有必要在每次事务提交时就把该事务在内
存中修改过的全部页面刷新到磁盘，只需要把修改了哪些东西记录一下就好。比如，某个事务将系统
表空间中第10号页面中偏移量为100 处的那个字节的值 1 改成2 。我们只需要记录一下：将第0号表
空间的10号页面的偏移量为100处的值更新为 2 。
![为什么需要REDO日志](./files\数据库事务-9.png)


### REDO日志的好处、特点
    * 好处
       * redo日志降低了刷盘频率
       * redo日志占用的空间非常小
       存储表空间ID,页号，偏移量以及需要更新的值，所需存储的空间是很小的，刷盘很快。
    *　特点
        * redo日志是顺序写入磁盘的
            在执行事务的过程中，每执行一条语句，就可能产生若干调redo日志，这些日志是按照产生的顺序写入磁盘。
            也就是使用顺序IO，效率比随机IO快.
            
        * 事务执行过程中，redo log不断记录   
             redo log 跟随bin log的区别，redo log是存储引擎层产生的，而bin log是数据库层产生的。假设一个事务，对表做10万此行的记录
             插入，在这个过程中，一直不断的往redo log顺序记录，而bin log不会记录，直到这个事务提交，才会写入到bin log文件中。
           
### redo log的组成
* Redo log可以简单的分为一下两个部分:
    * 重做日志的缓冲(redo log buffer),保护在内存中，是易失的。
        redo log buffer大小，默认大小是16M，最大是4096M，最小是1M。
        ![innodb_log_buffer_size的大小](./files\数据库事务-10.PNG)
       
    * 重做体制文件(redo log file)，保存在硬盘中，是持久的。
        ![事务日志文件](./files\数据库事务-11.PNG)    
        
### redo的整体流程
redo log的流转流程如下：
![redo log的流转流程](./files\数据库事务-12.PNG)
```text
1. 先将源时数据从磁盘读如内存中，修改数据的内存拷贝
2. 生成一条重做日志并写入redo log buffer,记录的是数据被修改后的值
3. 当事务commit时，将redo log buffer中的内容刷新到redo log file,对redo log file采用追加的方式写入。
4. 定期将内存中修改的数据刷新到磁盘进行持久化。
```

### redo log的刷盘策略
redo log的写入不是直接写入磁盘的，InnoDB引擎会在redo log的时候先写入redo log buffer,之后以一定测频率刷入到真正的redo log file中。这里的一
定频率时怎么样的呢？InnoDB存储引擎使用的是怎么样的刷盘策略？

![redo log的刷盘策略](./files\数据库事务-13.png)

注意： redo log刷盘到redo log file的过程并不是真正的刷到磁盘中区，只是刷入到文件系统缓存(page cache)中去(这是现代操作系统的为了提高
文件写入的效率做的一个优化)，真正的的吸入会交给系统自己来决定(比如page cache足够大了)。那么对于InnoDB来说就存在一个问题，如果交给系统来同步，
同样如果系统宕机，那么数据页就丢失了(虽然整个系统宕机的概率比较小)。

针对这种情况，InnoDB给出了Innodb_flush_log_at_trx_commit参数，该参数控制commit提交的事务时，如何将redo log buffer中的日志刷新到redo
log file中。它支持三种策略：
    1. 设置为0 ：表示每次事务提交时不进行刷盘操作。（系统默认master thread每隔1s进行一次重做日
       志的
    2. 设置为1 ：表示每次事务提交时都将进行同步，刷盘操作（ 默认值）
    3. 设置为2 ：表示每次事务提交时都只把 redo log buffer 内容写入 page cache，不进行同步。由os自
       己决定什么时候同步到磁盘文件。   

例子：
```sql

#10-事务日志

USE atguigudb3;

CREATE TABLE test_load(
a INT,
b CHAR(80)
)ENGINE=INNODB;


#创建存储过程，用于向test_load中添加数据
DELIMITER//
CREATE PROCEDURE p_load(COUNT INT UNSIGNED)
BEGIN
DECLARE s INT UNSIGNED DEFAULT 1;
DECLARE c CHAR(80)DEFAULT REPEAT('a',80);
WHILE s<=COUNT DO
INSERT INTO test_load SELECT NULL,c;
COMMIT;
SET s=s+1;
END WHILE;
END //
DELIMITER;

#测试1：
#设置并查看：innodb_flush_log_at_trx_commit

SHOW VARIABLES LIKE 'innodb_flush_log_at_trx_commit';

#set GLOBAL innodb_flush_log_at_trx_commit = 1;

#调用存储过程
CALL p_load(30000); #1min 28sec

#测试2：
TRUNCATE TABLE test_load;

SELECT COUNT(*) FROM test_load;

SET GLOBAL innodb_flush_log_at_trx_commit = 0;

SHOW VARIABLES LIKE 'innodb_flush_log_at_trx_commit';

#调用存储过程
CALL p_load(30000); #37.945 sec

#测试3：
TRUNCATE TABLE test_load;

SELECT COUNT(*) FROM test_load;

SET GLOBAL innodb_flush_log_at_trx_commit = 2;

SHOW VARIABLES LIKE 'innodb_flush_log_at_trx_commit';

#调用存储过程
CALL p_load(30000); #45.173 sec
```

























