# 变量，流程控制，游标

## 变量

在MySQL数据库中，变量分为**系统变量**以及**用户自定义变量**


### 系统变量
```text
变量由系统定义，不是用户定义，属于服务器层面。启动MySQL服务，生成MySQL服务实例期间，
MySQL将为MySQL服务器内存中的系统变量赋值，这些系统变量定义了当前MySQL服务实例的属性、特
征。这些系统变量的值要么是编译MySQL时参数的默认值，要么是配置文件（例如my.ini等）中的参数
值。大家可以通过网址 https://dev.mysql.com/doc/refman/8.0/en/server-systemvariables.
html 查看MySQL文档的系统变量。
系统变量分为全局系统变量（需要添加global 关键字）以及会话系统变量（需要添加 session 关键
字），有时也把全局系统变量简称为全局变量，有时也把会话系统变量称为local变量。如果不写，默认
会话级别。静态变量（在 MySQL 服务实例运行期间它们的值不能使用 set 动态修改）属于特殊的全局系
统变量。
每一个MySQL客户机成功连接MySQL服务器后，都会产生与之对应的会话。会话期间，MySQL服务实例
会在MySQL服务器内存中生成与该会话对应的会话系统变量，这些会话系统变量的初始值是全局系统变
量值的复制。如下图：
```
![系统变量](./files\变量-1.PNG)

* 全局系统变量针对于所有会话（连接）有效，但不能跨重启
* 会话1对某个全局系统变量值的修改会导致会话2中同一个全局系统变量值的修改。

在MySQL中有些系统变量只能是全局的，例如 max_connections 用于限制服务器的最大连接数；有些系
统变量作用域既可以是全局又可以是会话，例如 character_set_client 用于设置客户端的字符集；有些系
统变量的作用域只能是当前会话，例如 pseudo_thread_id 用于标记当前会话的 MySQL 连接 ID。
例子1：
```sql
-- 查看所有全局变量
SHOW GLOBAL VARIABLES;
```
![查看所有全局变量](./files\变量-2.PNG)
示例2
```sql
-- 查看所有会话变量
SHOW SESSION VARIABLES;
-- 等价于下面的SQL
SHOW VARIABLES;

```
![查看会话变量](./files\变量-3.PNG)

* 查看满足条件的系统变量可以使用下面的sql
```sql
#查看满足条件的部分系统变量。
SHOW GLOBAL VARIABLES LIKE '%标识符%';
#查看满足条件的部分会话变量
SHOW SESSION VARIABLES LIKE '%标识符%';
```

* 查看指定系统变量

作为 MySQL 编码规范，MySQL 中的系统变量以两个“@” 开头，其中“@@global”仅用于标记全局系统变
量，“@@session”仅用于标记会话系统变量。“@@”首先标记会话系统变量，如果会话系统变量不存在，
则标记全局系统变量。
```sql
#查看指定的系统变量的值
SELECT @@global.变量名;
#查看指定的会话变量的值
SELECT @@session.变量名;
#或者
SELECT @@变量名;
```

* 修改系统变量的值

有些时候，数据库管理员需要修改系统变量的默认值，以便修改当前会话或者MySQL服务实例的属性、
特征。具体方法：
方式1：修改MySQL 配置文件，继而修改MySQL系统变量的值（该方法需要重启MySQL服务）
方式2：在MySQL服务运行期间，使用“set”命令重新设置系统变量的值

修改变量的规范如下:
```sql
SET @@global.变量名=变量值;
#方式2：
SET GLOBAL 变量名=变量值;
#为某个会话变量赋值
#方式1：
SET @@session.变量名=变量值;
#方式2：
SET SESSION 变量名=变量值;
```
例子：
```sql
-- 查看当前mysql服务的最大链接数
SET @@global.max_connections=161;
SELECT @@global.max_connections;
-- 设置数据库事务隔离级别,MySQL8.0改成了transaction_isolation，MySQL8.0以前都是tx_isolation
SELECT @@session.transaction_isolation;
SET @@session.transaction_isolation='REPEATABLE-READ';
-- 设置数据库事务是否自动提交
SELECT @@global.autocommit;
SET @@global.autocommit=0;
```


### 用户变量
    用户变量时用户自己定义的，作为MySQL编码规范，MySQL中的用户变量以一个@开头。根据作用范围不同，可以分为会话用户变量和局部变量.
* 用户变量分类
    
    *  会话用户变量:作用域和会话变量一样，只对当前连接会话有效。
    
    * 局部变量:旨在BEGIN和END语句块中有效。局部变量只能在存储过程和存储函数中使用.
    
    
    
* 会话用户变量
    * 变量的定义
    ```
        -- 方式1:"="或":=" 
        SET @用户变量=值;
        SET @用户变量:=值;
        -- 方式2:":="或INTO关键字
        SELECT @用户变量:=表达式 [FROM等子句] 
        SELECT 表达式 INTO @用户变量 [FROM等子句] 
     ```
    * 查看用户变量的值(查看，比较，运算)
    ```sql
      SELECT @用户变量
    ```      
 例子:
```sql
SELECT @num:=COUNT(*) FROM employees;
SELECT @num;

SELECT AVG(emp.salary) INTO @avg_sal FROM employees emp;
SELECT @avg_sal;
```  

* 局部变量
定义:可以使用DECLARE语句定义一个局部变量
作用域:仅仅在定义它的BEGIN...END中有效
位置:只能放在BEGIN...END中，而且只能放在第一句.

```sql
DELIMITER $
BEGIN
    -- 声明局部变量
    DECLARE 变量名1 变量数据类型 [DEFAULT 变量默认值]
    DECLARE 变量名2，变量名3,... 变量数据类型 [DEFAULT 变量默认值]
    -- 为局部变量赋值
    SET 变量1=值;
    SELECT 值 INTO 变量名2 [FROM 子句]
    -- 查看局部变量的值
    SELECT 变量1,变量2,变量3,...;   
END $
DELIMITER ; 
  
```
例子1:
```sql
DELIMITER $
CREATE PROCEDURE add_value()
BEGIN
	DECLARE a INT DEFAULT 0;
	DECLARE b INT DEFAULT 0;
	DECLARE s INT; 
	SET a=1;
	SET b=2;
	SET s = a+b;
	SELECT a,b,s;
END $
DELIMITER ;
CALL add_value();
```
![存储过程add_value()定义局部变量](./files\变量-4.PNG)

例子2：
```sql
DELIMITER $
CREATE PROCEDURE different_salary(IN emp_id INT,OUT dif_salary DOUBLE)
BEGIN
		# 声明局部变量
		DECLARE emp_sal,mgr_sal DOUBLE DEFAULT 0.0;
		DECLARE mgr_id INT;
		
		SELECT salary INTO emp_sal FROM employees WHERE employee_id = emp_id;
		SELECT manager_id INTO mgr_id FROM employees WHERE employee_id = emp_id;
		SELECT salary INTO mgr_sal FROM employees WHERE employee_id = mgr_id;
		SET dif_salary = mgr_sal-emp_sal;
END $
DELIMITER ;

SET @emp_id = 102;

CALL different_salary(@emp_id,@diff_sal);

SELECT @diff_sal;
```

## 定义条件与处理程序

定义条件时实现定义程序执行过程中可能遇到的问题，处理程序定义了在遇到问题时应当采取有的处理的方式，并且保证存储过程
或者存储函数在遇到警告或者错误时继续执行。这样可以增强存储程序处理问题的能力，避免程序一层停止运行.

说明：定义条件和处理程序在存储过程、存储函数中都是支持的。

### 定义条件

定义条件就是给MySQL中错误的代码命名，这有助于存储的程序代码更清晰。它将一个错误名字和指定错误条件关联起来，
这个名字可以随后被用在定义存储程序的DECLARE HANDLER语句中。

格式:
```sql
DECLARE 错误名称 CONDITION FOR  错误码(错误条件)
```
 
错误码说明

* MySQL_error_code和sqlstate_value都可可以表示MySQL的错误。
    * MySQL_error_code是数值类型错误代码。
    * sqlstate_value是长度为5的字符串类型错误代码
例子：
```sql
-- 执行插入语句，出错ERROR 1364 (HY000): Field 'email' doesn't have a default value
INSERT INTO employees(last_name)VALUES('zhangsan')
``` 
执行上面的sql会出错，因为employees有些email字段是非空的，所以要插入空字段是不被允许的。
上面的1364是MySQL_error_code, HY000是sqlstate_value  
定义如下:              
```sql
DECLARE Field_Not_Be_NULL CONDITION FOR 1364;
或者
DECLARE Field_Not_Be_NULL CONDITION FOR SQLSTATE 'HY000'
```

### 定义处理程序
```sql
DECLARE 处理方式 HANDLER FOR  错误类型 处理语句;
```
* 处理方式 
    * CONTINUE:表示晕倒错误不处理，继续执行
    * EXIT:表示遇到错误马上退出
    * UNDO:表示遇到错误后撤回之前的操作。MySQL中暂时不支持这样的操作
    
* 错误类型
    * SQLSTATE '字符串错误码':表示长度的为5的sqlstate_value类型的错误代码
    * MySQL_error_code:匹配数值类型错误代码
    * 错误名称:表示DECLARE...CONDITION定义的错误条件名称.
    * SQLWARNING:匹配所有的以01开头的SQLSTATE错误代码
    * NOT FOUND:匹配所有以02开头的SQLSTATE错误代码
    * SQLEXCEPTION:匹配所有没有被SQLWARNING或者NOT FOUND捕获的SQLSTATE错误代码
* 处理语句：
 如果出现上述条件之一，则采用对应的处理方式，并执行指定的处理语句。语句可以是"SET 变量=值"这样的简单语句，也可以使用BEGIN...END编写复合语句


例子
```sql
DECLARE CONTINUE HANDLER FOR SQLSTATE 'HY000' SET  @info='Field Not Be NULL'
#方法1：捕获sqlstate_value
DECLARE CONTINUE HANDLER FOR SQLSTATE '42S02' SET @info = 'NO_SUCH_TABLE';
#方法2：捕获mysql_error_value
DECLARE CONTINUE HANDLER FOR 1146 SET @info = 'NO_SUCH_TABLE';
#方法3：先定义条件，再调用
DECLARE no_such_table CONDITION FOR 1146;
DECLARE CONTINUE HANDLER FOR NO_SUCH_TABLE SET @info = 'NO_SUCH_TABLE';
#方法4：使用SQLWARNING
DECLARE EXIT HANDLER FOR SQLWARNING SET @info = 'ERROR';
#方法5：使用NOT FOUND
DECLARE EXIT HANDLER FOR NOT FOUND SET @info = 'NO_SUCH_TABLE';
#方法6：使用SQLEXCEPTION
DECLARE EXIT HANDLER FOR SQLEXCEPTION SET @info = 'ERROR';
```


测试定义条件和处理程序的例子:
```sql
DELIMITER $
CREATE PROCEDURE condition_test()
BEGIN
		DECLARE Field_Not_Be_NULL CONDITION FOR 1364;
-- 		DECLARE CONTINUE HANDLER FOR Field_Not_Be_NULL SET @info='FIEL NOT BE NULL';
		DECLARE EXIT HANDLER FOR Field_Not_Be_NULL SET @info='FIEL NOT BE NULL';


		SET @T=1;
		INSERT INTO employees(last_name)VALUES('zhangsan');
		SET @T=2;
		INSERT INTO employees(last_name)VALUES('lisi');
		SET @T=3;
		INSERT INTO employees(last_name)VALUES('wangwu');
		SELECT @T,@info;
END $
DELIMITER ;

DROP PROCEDURE IF EXISTS condition_test;

CALL condition_test();
```
通过修改存储过程的条件和处理程序可以得到一下现象
* 如果是CONTINUE，那么程序就会得到x=3和info=FIEL NOT BE NULL
* 如果是EXIT程序就退出没有任何显示.

## 流程控制

### 分支结构之 IF
```sql
IF 表达式1 THEN 操作1
[ELSEIF 表达式2 THEN 操作2]
[ELSE 操作N]
END IF
```
例子：
```sql
-- 声明存储过程“update_salary_by_eid1”，定义IN参数emp_id，输入员工编号。判断该员工薪资如果低于8000元并且入职时间超过5年，就涨薪500元；否则就不变。
DELIMITER $
CREATE PROCEDURE update_salary_by_eid1(IN emp_id INT)
BEGIN

		DECLARE emp_sal DOUBLE DEFAULT 0.0;
		DECLARE hire_year DOUBLE DEFAULT 0.0;
		
		SELECT salary INTO emp_sal FROM employees WHERE employee_id=emp_id;

		SELECT DATEDIFF(CURDATE(),hire_date)/365  INTO hire_year FROM employees WHERE employee_id=emp_id;
		
		IF emp_sal<8000 AND hire_year>5
		THEN UPDATE employees SET salary = salary+500 WHERE employee_id=emp_id;
		END IF;
END $

DELIMITER ;


DROP PROCEDURE IF EXISTS update_salary_by_eid1

CALL update_salary_by_eid1(104);

SELECT  *FROM employees WHERE employee_id=104;
```
![执行存储程序之前](./files\分支结构-1.PNG)
![执行存储程序之后](./files\分支结构-2.PNG)




### 分支结构之 CASE

### 循环结构之LOOP

### 循环结构之WHILE


### 循环结构之REPEAT


### 跳转语句之LEAVE语句


### 跳转语句之ITERATE语句


















































