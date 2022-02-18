# Mysql

## windows 10 安装mysql

### 先卸载原本安装的mysql服务
```text
将mysql5.7和mysql8.0同时安装在windows10 上这个是可以实现的，需要将原来的mysql服务全部删除掉，包括了软件的删除和相关的数据的删除，
包括了C:\ProgramData\MySQL目录下的数据文件和配置文件的删除.除此之外还需要将注册表上的关于mysql的注册信息都一并的删除掉。
a.HKEY_LOCAL_MACHINE\SYSTEM\ControlSet001\services\MySQL
b.HKEY_LOCAL_MACHINE\SYSTEM\ControlSet002\services\MySQL
c.HKEY_LOCAL_MACHINE\SYSTEM\ControlSet001\services\evenlog\Application\MySQL
d.HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\services\eventlog\Application\MySQL
(如果没有在注册表找到可以不用删除。)
```
### 下载软件并安装
```text
首先下载软件，分别是mysql-installer-community-5.7.34.0.msi和mysql-installer-community-8.0.26.0.msi,首先按照提示一步步安装了mysql8
然后安装mysql5.7。需要注意的是，安装了mysql8之后，如果需要安装mysql5就会提示需要删除原本安装的mysql8,但是我们并不需要做这样的更新，我们
需要的是将mysql5.7和mysql8同时安装在同一台机器上，所以如果提示更新mysql8,我们只需要取消即可，此时会进入一个界面可以选择添加新的服务，点击
添加服务之后就会进入选择添加对应的版本的服务，将选中的版本移动到右边框，并且选中，此时会弹出一个绿色的饿advance的字样，选中这个字，我们的就
可以选择将我们的服务安装到具体的目录，并且设置服务的相关的数据存储的位置。后面的一路选择next和execute就性，最后设置服务的信息就需要将断开和、
原来mysql8服务的端口设置的不一样即可。
```
### MySQL5.7将默认的拉丁改成utf8
```text
在我安装mysql5.7时设置的data的目录找到my.ini，编辑my.ini文件，在mysql项下面添加default-character-set=utf8,在mysqld项下面添加
character-set-server=utf8和collation-server=utf8-general-ci,最后在服务列表将mysql服务列表将服务重启即可。当然也可以使用命令
停止：net stop mysql
启动：net satrt mysql
```


### 用户忘记
```text
问题1：root用户密码忘记，重置的操作
1: 通过任务管理器或者服务管理，关掉mysqld(服务进程) 
2: 通过命令行+特殊参数开启mysqld mysqld --
defaults-file="D:\ProgramFiles\mysql\MySQLServer5.7Data\my.ini" --skip-grant-tables
3: 此时，mysqld服务进程已经打开。并且不需要权限检查 
4: mysql -uroot 无密码登陆服务器。另启动一
个客户端进行 
5: 修改权限表 （1） use mysql; （2）update user set authentication_string=password('新密
码') where user='root' and Host='localhost'; （3）flush privileges; 
6: 通过任务管理器，关掉mysqld服务进程。 
7: 再次通过服务管理，打开mysql服务。 
8: 即可用修改后的新密码登陆。
```
## SQL

### SQL的分类
* DDL
数据库定义语言:CREATE\ALTER\DROP\RENAME\TRUNCATE

* DML
数据操作语言:INSERT\DELETE\UPDATE\SELECT

* DCL 
数据控制语言: COMMIT\ROLLBACK\SAVEPOINT\GRANT\REVOKE(回收权限)


### SQL 语言的规则与规范

* SQL可以卸载一行或者多行。为了提高可读性，各字句分行写，必要时使用缩进
* 每条命令以;或者\g或\G结束
* 关键字不能被缩写或者换行
* 关于标点符好
    * 必须保证所有的(),单引号，双引号时城队结束的
    * 必须使用英文状态下的半角输入方式
    * 字符型和日期时间类型的树可以使用单引号('')表示
    * 列的别名，尽量使用双引号(""),而且不建议省略as (这样做的好处是在列中如果存在空格等，也可以将含空格的一并算做别名，否则会报错)

### SQL 大小写规范
* MySQL在windows环境下大小写不敏感
* MySQL在Linux环境下大小写敏感
    * 数据库名，表名，表的别名，变量名严格区分大小写。
    * 关键字，函数名，列名(或者字段名)，列的别名(字段的别名)时忽略大小写的。
* 推荐采用统一的书写规范
    * 数据库名，表名，表的别名，字段名，字段别名等都小写
    * SQL关键字，函数名，绑定变量等都大写。
    
### 注释
```sql
单行注释:#注释文字(MySQL特有的方式)
单行注释:-- 注释文字(--后面需要宝航一个空格)
多行注释: /* 注释文字 */
```    
    
### 命名规则
* 数据库、表名不得超过30个字符，变量名限制为29个
* 必须只能包含 A–Z, a–z, 0–9, _共63个字符
* 数据库名、表名、字段名等对象名中间不要包含空格
* 同一个MySQL软件中，数据库不能同名；同一个库中，表不能重名；同一个表中，字段不能重名
* 必须保证你的字段没有和保留字、数据库系统或常用方法冲突。如果坚持使用，请在SQL语句中使用`（着重号）引起来
* 保持字段名和类型的一致性，在命名字段并为其指定数据类型的时候一定要保证一致性。假如数据
 类型在一个表里是整数，那在另一个表里可就别变成字符型了   

## 数据导入指令
```text
在命令行客户端登录mysql，使用source指令导入
```
## 基本的SELECT语句
### 去重
```sql
SELECT DISTINCT department_id FROM employees;
-- DISTINCT 其实是对后面所有列名的组合进行去重，你能看到最后的结果是 74 条，因为这 74 个部
-- 门id不同，都有 salary 这个属性值。如果你想要看都有哪些不同的部门（department_id），只需
-- 要写DISTINCT department_id 即可，后面不需要再加其他的列名了。
SELECT DISTINCT department_id,salary FROM employees;
```
![第一句sql去重前](./files\sql去重-1.PNG)
![第一句sql去重后](./files\sql去重-2.PNG)

![第二句sql去重前](./files\sql去重-3.PNG)
![第二句sql去重后](./files\sql去重-4.PNG)


### 控制运算处理
```sql
SELECT employee_id,salary,commission_pct,12*salary*(1+IFNULL(commission_pct,0)) "annual_sal" FROM employees;
```
![sql加ifnull()函数之前](./files\sql空值参与运算-1.PNG)
![sql加ifnull()函数之后](./files\sql空值参与运算-2.PNG)

### 着重号
```sql
出现表名或者字段名和关键字相同，则可以使用着重号标识，这样就可以避免执行sql报错
```

## 运算符

### 比较运算符
比较运算符用来对表达式左边的操作数和右边的操作数进行比较，比较的结果为真则返回1，比较的结果为假返回0，其他的情况则返回NULL
比较运算符经常被用来作为SELECT查询语句的条件进行使用，发牛的符合条件的结果记录。
* 等号运算符
    * 等号运算符(=)判断两边的值，字符串或者表达式是否相等，如果相等则返回1，不相等返回0
    * 在使用等号运算符是，遵循如下的原则
        * 如果等号两边的值，字符串或者表达式都为字符串，则MySQL会按照字符串进行比较，其比较的是每个字符串中字符的ANSI编码是否相等
        * 如果等号两边的值都是整数，则MySQL会按照整数来比较两个值的大小
        * 如果等号两边的值ige是整数，另一个是字符串，那么MySQL会将字符串转化为数字进行比较s
        * 如果等号两边的值，字符串或者表达式中有一个为NULL,则比较结果为NULL
        
```sql
SELECT 1=1,1='1',1=0,'a'='a',(5+3)=(2+6),''=NULL,NULL=NULLFROM dual;
```        
SQL执行结果
![等号运算符sql执行结果](./files\比较运算符-1.PNG)

```sql
# 字符串和数字互相转换，如果缓缓不成功就看作是0，此处的字符串abc是无法正常转换成ASIIC码的，所以默认返回0.
SELECT 1=2,0='abc',1='abc'FROM DUAL;
```
SQL执行结果
![等号运算符sql执行结果](./files\比较运算符-2.PNG)

* 不等于运算符
```sql
SELECT 1<>1,1!=2,'a'!='b',(3+4)<>(2+6),'a'!=NULL,NULL<>NULL FROM DUAL;
```
![不等于运算符sql执行结果](./files\比较运算符-3.PNG)


* 非符号运算符 LEAST(),GREATEST()
这两个运算符主要是从列表中选出最小值和最大值，在列表中如果出现了NULL值，那么这个列表中的值是无法比较大小的，此时返回一个NULL值
例子:
```sql
SELECT LEAST(1,2,4,5,6,7),LEAST('b','a','c'),LEAST(1,NULL,2) FROM DUAL
```
![运行结果](./files\比较运算符-4.PNG)

```sql
-- 最大值运算符
SELECT GREATEST(1,2,5),GREATEST('D','A','F'),GREATEST(1,NULL,2) FROM DUAL;
```
![sql执行结果](./files\比较运算符-5.PNG)


* 转义字符
```sql
-- ESCAPE 使用转义字符
SELECT job_id FROM jobs where job_id LIKE 'IT\_%';
-- 使用ESCAPE自定义转义字符
SELECT job_id FROM jobs where job_id LIKE 'IT$_%' ESCAPE '$';
```
![sql执行结果](./files\比较运算符-6.PNG)



## 多表查询

















