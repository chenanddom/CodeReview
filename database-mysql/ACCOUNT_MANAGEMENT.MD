[上一章(MySQL数据目录)](./MYSQL_DATA_DIRECTORY.MD)
# MySQL账户管理
启动MySQL服务后，可以通过mysql命令来登录MySQL服务器，命令如下：
```sql
mysql –h hostname|hostIP –P port –u username –p DatabaseName –e "SQL语句"
```
* 下面详细介绍命令中的参数：

    * -h参数后面接主机名或者主机IP，hostname为主机，hostIP为主机IP。
    * -P参数后面接MySQL服务的端口，通过该参数连接到指定的端口。MySQL服务的默认端口是3306，不使用该参数时自动连接到3306端口，port为连接的端口号。
    * -u参数后面接用户名，username为用户名。
    * -p参数会提示输入密码。
    * DatabaseName参数指明登录到哪一个数据库中。如果没有该参数，就会直接登录到MySQL数据库中，然后可以使用USE命令来选择数据库。
    * -e参数后面可以直接加SQL语句。登录MySQL服务器以后即可执行这个SQL语句，然后退出MySQL服务器。
例子：
```sql
mysql -uroot -p -hlocalhost -P3306 mysql -e "select host,user from user"
```
![连接执行sql](./files\账号-1.PNG)

## 创建用户
语法形式如下:
```sql
CREATE USER  用户名 [IDENTIFIED BY '密码'][,用户名 [INDETIFIED BY '密码']]
```
* 用户名参数表示新建用户的账户，由用户（User） 和主机名（Host） 构成；
*　“[ ]”表示可选，也就是说，可以指定用户登录时需要密码验证，也可以不指定密码验证，这样用户
  可以直接登录。不过，不指定密码的方式不安全，不推荐使用。如果指定密码值，这里需要使用
  IDENTIFIED BY指定明文密码值。
*　CREATE USER语句可以同时创建多个用户。

例子：
```sql
CREATE USER chendom IDENTIFIED BY 'Root@123';
或
CREATE USER ’chendom‘@'localhost' IDENTIFIED BY 'rOOT@123' 
```  

## 修改用户名
```sql
UPDATE mysql.user SET USER='chendong' WHERE USER='chendom';
```
例子:
```sql
 update mysql.user set user='chendom' where user='chendong';
FLUSH PRIVILEGES;
```
## 删除用户
```sql
-- 方式1
DROP USER user[,user]…;
DROP USER 'chendom'@'%';
-- 方式2
DELETE FROM mysql.user WHERE Host=’hostname’ AND User=’username’;
FLUSH PRIVILEGES;
```

## 权限管理

### 权限列表
```sql
-- 获取权限列表
show privileges;
```
* 
    1. _CREATE和DROP权限_,可以创建新的数据库和表，或删除(移动)已有数据库和表。如果将MySQL数据库中的DROP权限授予某个用户，用户
        就可以删除MySQL访问权限保存的数据库.
    2. SELECT、INSERT、UPDATE和DELETE权限允许在一个数据库现有的表上实施操作。
    3. SELECT权限只有在他们真正的从ige表中检索行的时候才被用到。
    4. INDEX权限 允许创建或者删除索引，INDEX使用于已有的表。如果具有某个表的CREATE权限，就可以才CREATE TABLE语句中包括索引的定义，
    5. ALTER权限可以使用ALTER TABLE来更改表的结构和重新命名表，
    6. CREATE ROUTINE权限捞创建保存的程序(函数和程序)，ALTER ROUTINE 权限用来更改和删除保存的程序，EXECUTE权限用来执行保存的程序。
    7. GRANT权限允许授权给其他用户，可用于数据库，表和保存的程序。
    8. FILE权限使用户可以使用LOAD DATAINFILE和SELECT ... INTO OUTFILE语句读或写服务器上的文件任何授予FILE使用权限的用户都能够读
    或者写MySQL服务器上的任何文件(说明用户可以读任何数据库的目录下的文件，因为服务器可以访问这些文件)。
    
### 授予权限的原则
权限控制主要是出于安全因素，因此需要遵循以下几个经验原则：
1、只授予能满足需要的最小权限，防止用户干坏事。比如用户只是需要查询，那就只给select权限就可
以了，不要给用户赋予update、insert或者delete权限。

2、创建用户的时候限制用户的登录主机，一般是限制成指定IP或者内网IP段。

3、为每个用户设置满足密码复杂度的密码。

4、定期清理不需要的用户，回收权限或者删除用户。


### 授予权限
给用户授权的方式有两种，分贝时通过把角色赋予用户给用户授权和直接给用户授权，用户时数据库的使用者我们可以通过给用户授予访问数据库中资源的权限
，来控制使用者对数据库的访问，消除安全隐患。
授权的格式如下:
```sql
GRANT 权限1，权限2，权限3...权限n ON 数据库名.表名 TO 用户名@用户地址 [IDENTIFIED BY '密码口令']
```
![用户没赋权时的样子](./files\权限-2.PNG)
使用下面的语句赋权给该用户
```sql
-- 将dbtest1数据库下的所有表的查看和更新的权限赋值给chendom用户
GRANT SELECT ,UPDATE ON dbtest1.* TO 'chendom'@'%'
```
![给chendom这个用户赋权读取和修改的权限后](./files\权限-3.PNG)

### 回收权限
收回权限就是取消已经赋予用户的某些权限。收回用户不必要的权限可以在一定程度上保证系统的安全
性。MySQL中使用REVOKE语句取消用户的某些权限。使用REVOKE收回权限之后，用户账户的记录将从
db、host、tables_priv和columns_priv表中删除，但是用户账户记录仍然在user表中保存（删除user表中
的账户记录使用DROP USER语句）。
注意：在将用户账户从user表删除之前，应该收回相应用户的所有权限。
```sql
REVOKE 权限1，权限2...权限n ON 数据库.表名 RFOM 用户名@用户地址
```
例子：
```sql
-- 回收权限后，需要第二次登录后才有效果
REVOKE UPDATE ON dbtest1.employees FROM 'chendom'@'%';
```
![回收权限后的效果](./files\权限-4.PNG)





[下一章(逻辑架构)](./LOGIC_FRAMEWORK.MD)