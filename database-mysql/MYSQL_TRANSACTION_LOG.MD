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
### 写入redo log buffer过程

* 概念补充:Mini-Transaction
MySQL把对底层页面中的一次原子访问的过程称之位一个Mini-Transaction,简称位mtr，比如，向某个索引对于的B+树中插入一条记录的过程就是一个
Mini-Transaction。一个所谓的mtr可以包含一组redo日志，在进行崩溃恢复时这一组redo日志作为一个不可份个的整体。

一个事务可以包含若干条语句，每一条语句其实都是由若干个mtr组成，每一个mtr又可任意包含若干redo日志，画个图表示它们的关系就是这样的。
![mtr](./files\数据库事务-14.PNG)

*　redo 日志写入log buffer
向log buffer中写入日志的过程时顺序的，也就先往前边的block中写，当block的空闲空间用完之后再往下一个block中写。当我们想往log buffer中写
如redo日志时，第一个遇到的问题就是应该写再哪个block的哪个偏移量处，所以InnoDB的设计者特意提供了一个称之为buf_free的全局变量，该变量指明后
续写入的redo日志应该写入到log buffer中的哪个位置，如图所示:

![redo 日志写入log buffer](./files\数据库事务-15.PNG)

一个mtr执行过程中可能产生若干条redo日志，这些redo日志时一个不可份个的组，所以其实并不是没生成一条redo日志，就将器插入到log buffer中，
而是每个mtr运行过程中产生的日志先暂存到一个地方，当该mtr结束的时候，将过程中产生的一组redo日志再全部赋值到log buffer中。我们现在假设有两个
名为T1,T2的事务，每个事务都包含2个mtr，我们给这个几个mtr命名一下
    * 事务T1的两个mtr分贝称为mtr_T1_1和mtr_T1_2.
    * 事务T2的两个mtr分贝称为mtr_T2_1和mtr_T2_2
    每个mtr都会产生一组redo日志，用示意图来描述一下这些mtr产生的日志情况：
![每个mtr都会产生一组redo日志](./files\数据库事务-16.PNG)  

不同的事务可能是并发执行的，所以T1 、T2 之间的mtr 可能是交替执行的。

![事务是可以并发执行的](./files\数据库事务-17.PNG)


* redo log block的结构
一个redo log block是由日志头，日志体，日志尾组成的，日志头12个字节，日志尾占用8个字节，真正村粗数据的就是512-12-8=496字节

    * 为什么一个block设计成512字节？
        这个和从盘的扇区有关，机械磁盘默认的山区就是512字节，如果你要写入的数据大于512字节，那么写入山区肯定不知一个，这是就要设计到盘片的
        转动，找到下一个扇区，假设现在需要写入的两个扇区A和B，如果扇区A写入成功，而扇区B写入失败，那么就会出现非原子性的写入，而如果每次
        只写入和扇区的大小一样的512字节，那么每次的写入都是原子性的。
        
* 相关的参数设置
    * innodb_log_group_home_dir ：指定 redo log 文件组所在的路径，默认值为./ ，表示在数据库
      的数据目录下。MySQL的默认数据目录（ var/lib/mysql ）下默认有两个名为ib_logfile0 和
      ib_logfile1 的文件，log buffer中的日志默认情况下就是刷新到这两个磁盘文件中。此redo日志
      文件位置还可以修改。
    * innodb_log_files_in_group：指明redo log file的个数，命名方式如：ib_logfile0，iblogfile1...
      iblogfilen。默认2个，最大100个。
    * innodb_flush_log_at_trx_commit：控制 redo log 刷新到磁盘的策略，默认为1。
    * innodb_log_file_size：单个 redo log 文件设置大小，默认值为 48M 。最大值为512G，注意最大值
      指的是整个 redo log 系列文件之和，即（innodb_log_files_in_group * innodb_log_file_size ）不能大
      于最大值512G。总共的redo日志文件大小其实就是： innodb_log_file_size × innodb_log_files_in_group 。
      ![Innodb相关参数设置](./files\数据库事务-18.PNG)
*　checkpoint
在整个日志文件组中还有两个重要的属性，分别是write pos、checkpoint
    * write pos 是当前记录的位置，一边写一边后移
    * checkpoint 是当前要擦除的位置，也是往后推移。
每次刷盘redo log 记录到日志文件组中，write pos位置就会后移更新。每次MySQL加载文件组恢复数据时，会清空加载果的redo log记录，并吧checkpoint
后移更新。write pos和checkpoint之间的还空着的部分可以用来写入新的redo log

![checkpoint与write pos的执行顺序](./files\数据库事务-19.png)

如果 write pos 追上 checkpoint ，表示日志文件组满了，这时候不能再写入新的 redo log记录，MySQL 得
停下来，清空一些记录，把 checkpoint 推进一下。


## Undo日志
redo log是事务持久性的保证，undo log是事务原子性的保证。在事务中**更新数据**的**前置操作**其实要先写入一个undo log

![Undo日志优先写](./files\数据库事务-20.png)

对Undo log的理解：
```text
事务需要保证原子性，也就是事务中的操作要么全部完成，要么什么也不做。但有时候事务执行到一半
会出现一些情况，比如：
情况一：事务执行过程中可能遇到各种错误，比如服务器本身的错误， 操作系统错误，甚至是突
然断电导致的错误。
情况二：程序员可以在事务执行过程中手动输入ROLLBACK 语句结束当前事务的执行。
以上情况出现，我们需要把数据改回原先的样子，这个过程称之为回滚，这样就可以造成一个假象：这
个事务看起来什么都没做，所以符合原子性要求。

如果我们对进行改动(INSERT,UPDATE,DELETE)，都需要把"留一受"----把回滚时所需的东西记录下来：
    1. 插入一条记录时，至少需要把这条记录的主键值记下来，之后回滚的时候只需要把这个主键值对应的记录删除就可以了。(对于每个INSERT,InnoDB
        存储因为会完成一个DELETE)
    2. 删除一条记录时，会把删除前的这条记录的相关的内容都记下来，这样下来，我们回滚时在把记录下来的内容插入到表中(对于每个DELETE，InnoDB
        InnoDB存储引起会执行一个INSERT)
    3. 修改一条记录，至少要把修改这条记录前的旧值记录下来，这样之后回滚时再把这条记录**更新为旧值**就可以了(对于每个UPDATE，InnoDB存储
        引起会执行一个相反的UPDATE，将修改前的行放回去)。
MySQL把这些为了回滚而记录的这些内容称为**撤销日志**或者**回滚日志**(即Undo log)。注意，由于查询操作(SELECT)并不会修改任何用户记录，所以
再查询操作执行时，并不需要记录相应的undo日志。
此外，undo log 会产生redo log，也就是undo log的产生会伴随者redo log的产生，这时因为undo log 也需要持久性的保护。

```
* Undo log的作用
    1. 回滚数据
        undo log是**逻辑日志**，因此只是将疏忽逻辑的恢复到原来的样子。所有修改都被逻辑地取消了，但是数据结构和页本身再回滚之后可能是不相同了
        因为再多用户地并发系统中，可能会由很多并发事务。数据库的主要任务就是协调对数据记录的并发访问，比如，一个事务再修改当前一个页中的某几条
        记录，同时还有别的事务再对同一个页中的另外几条记录进行修改。因此，不饿呢个将一个页回滚到事务开始的样子，因为这样会影响到其他事务正
        在进行的工作
    2.  MVCC
        undo log的另一个作用是MVCC，即InnoDB存储引起中MVCC的实现是通过undo来完成的。当用户读取一行记录的时候，若该记录已经被气压事务占用
        当前事务可以通过undo读取之前行版本信息，以此来实现非锁定读取。
        
        
### Undo log的存储结构
1. 回滚段与Undo 页
InnoDB对undo log的管理采用段的方式，也就是**回滚段(rollback segment)**。每个回滚段记录了1024个undo log segment，而再每个undo log segment
段中进行undo页的申请。
    * 在InnoDB1.1版本之前(不包括1.1版本)，只有一个rollback segment,因此支持同时在先的事务限制为1024.虽然绝大多数的应用来水都已经够用了。
    * 从1.1版本开始InnoDB支持最大128个rollback segment,故其支持同时在线的事务限制提高到了128*1024。
    ![undo log段的大小](./files\数据库事务-21.PNG)
       
虽然InnoDB1.1版本支持128个rollback segment,但是这些rollback segment都存储于贡献空间的ibdata中。从InnoDB1.2版本开始，可以通过
rollback segment做一步的设置。这些参数包括:
* innodb_undo_directory:设置rollback segment文件所在的路径。这个意味着rollback segment可以存放在贡献表空间以外的位置，即可以设置独立 
表空间。该参数的默认值为./表示当前的InnoDB存储引起的目录。
* innodb_undo_logs:设置rollback segment的个数，默认值是128.在InnoDB1.2版本中，该参数用来代替之前的参数innodb_rollback_segment
* innodb_undo_tablespaces:设置构成rollback segment文件的数量，这样rollback segment可以较为平均的分布在多个文件中。设置该参数后，
会在路径innodb_undo_directory看到undo为前缀的文件，该文件就代表了rollback segment文件。

![相关参数的设置](./files\数据库事务-22.PNG)


* undo 页的重用
当我们开启一个事务需要写undo log的时候，就得先去undo log segement中找一个空闲的位置，当由空位的时候，就去申请undo 页，在这个申请到的undo
页中进行undo log的写入。我们知道mysql的一个大小是16k。为每一个事务分配一个页，是非常的浪费空间的，如果应用的TPS(每秒的处理事务的数目为1000
大概需要16M，1分钟就需要1G的存储)，这样磁盘的空间会增长的很快，而且很多空间都是浪费的，此时，undo页iu设计成了可以重用的，当事务提交后，会被放
到一个链表中，然后判断undo页的使用空间是否小于3/4,如果小于3/4的话，则表示当前的undo页可以被重用，那么它 就不会被回收，其他事务的undo log
可以记录在当前的undo页的后面。由于undo log是离散的，所以清理对于的磁盘空间时，效率不高。

* 回滚段与事务
1. 每个事务只会使用一个回滚段，一个回滚段在同一时刻可能会服务于多个事务。
2. 当一个事务开始的时候，会制定一个回滚段，在事务进行的过程中，当数据被修改时，原始的数
据会被复制到回滚段。
3. 在回滚段中，事务会不断填充盘区，直到事务结束或所有的空间被用完。如果当前的盘区不够
用，事务会在段中请求扩展下一个盘区，如果所有已分配的盘区都被用完，事务会覆盖最初的盘
区或者在回滚段允许的情况下扩展新的盘区来使用。
4. 回滚段存在于undo表空间中，在数据库中可以存在多个undo表空间，但同一时刻只能使用一个
undo表空间。
5. 当事务提交时，InnoDB存储引擎会做以下两件事情：
    * 
        * 将undo log放入列表中，以供之后的purge操作
    * 
        * 判断undo log所在的页是否可以重用，若可以分配给下个事务使用


* 回滚段中的数据分类
    1. 未提交的回滚数据(uncommitted undo information)
        该数据所关联的事务并为提交，用于实现读一致性，所以该数据不能被其他事务的数据覆盖。
        
    2. 已经提交但未过期的回滚数据(committed undo information)
        该树关联的事务已经提交，但是仍然收到undo retention参数的保持时间的影响。
        
    3. 事务已经提交并过期的数据(expired undo information)
        事务已经提交，而且数据保存的时间已经超过了undo retention参数指定的时间，术语已经国企的深故居，当回滚段了之后，会优先覆盖"事务
        已经提交并过期的数据".
        
* undo log的类型
   * insert undo log
   insert undo log是指在insert操作中产生的undo log。因为insert操作的记录，只对事务本身可见，对其他事务不可见(这是事务隔离性的要求)，
   故该undo log可以在事务提交后直接删除。不需要进行purge操作。
   
   * update undo log
   update undo log记录的是对delete和update操作产生的undo log。该undo log可能需要提高MVCC机制，因此不能在事务提交时进行删除。提交时
   放入undo loh链表，等待purge线程进行最后是删除.
   
   
* undo log的生命周期
1. 只有buffer Pool的流程：

![只有buffer pool的执行流程](./files\数据库事务-23.png)

2. 有了Redo Log和Undo Log之后
 
![有了Redo Log和Undo Log之后的执行流程](./files\数据库事务-24.png)

* 存储结构
![MySQL的存储结构](./files\数据库事务-25.png)


当我们执行INSERT时：
```sql
begin;
INSERT INTO user (name) VALUES ("tom");
```
![MySQL的存储结构](./files\数据库事务-26.png)

当我们执行UPDATE时：

![MySQL的存储结构](./files\数据库事务-27.png)

```sql
UPDATE user SET id=2 WHERE id=1;
```
![更新](./files\数据库事务-28.png)


* undo log是如何回滚的
  以上面的例子来说，假设执行rollback，那么对应的流程应该是这样：
  1. 通过undo no=3的日志把id=2的数据删除
  2. 通过undo no=2的日志把id=1的数据的deletemark还原成0
  3. 通过undo no=1的日志把id=1的数据的name还原成Tom
  4. 通过undo no=0的日志把id=1的数据删除


* undo log的删除
    * 针对于insert undo log
        因为insert操作的记录，只对事务本身可见，对其他事务不可见。故该undo log可以在事务提交后直接删
        除，不需要进行purge操作。

    * 针对于update undo log
        该undo log可能需要提供MVCC机制，因此不能在事务提交时就进行删除。提交时放入undo log链表，等
        待purge线程进行最后的删除。



[下一章(数据库锁)](./MYSQL_LOCK.MD)






