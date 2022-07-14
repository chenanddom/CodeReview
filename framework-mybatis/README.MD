# MyBatis




## MyBatis缓存
```text
为了提高服务的并发性能，所有引入了缓存，也许说到缓存象到的是redis，但是，
实际的项目开发中需要有多级缓存。因为多级缓存的存在，可以减少了数据库的网络
IO和磁盘IO，减少了在IO上的消耗，增加了系统的并发性能.MyBatis也作为一个出
色的ORM框架也是拥有了出色的缓存机制，主要包括了一级缓存和二级缓存。MyBatis的
一级缓存默认是打开的，而且mybatis的一级缓存解决了DBMS的脏读和不可重复读的问题。
mybatis的以及缓存主要与sqlsession相关联。如果sqlSession关闭了缓存会清空，如果
做了update操作(不是xml的文件的标签)也会清除缓存，在提交事务commit和回滚事务 
rollback都会轻触缓存
```






