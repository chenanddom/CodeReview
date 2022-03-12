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
使用场景:它对于比较开销时非常有用的，特别时我们iyou好几种查询方式可选的时候。
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


