[上一章(数据类型)](./DATATYPE.MD)
# 外键约束

外键约束的关键字是FOREIGN KEY

* 主表和从表或者是父表和子表
主表(父表):被引用的表，被参考的表
从表(子表):引用别人的表，参考别人的表

* 特点:
    1. 从表的外键列，必须引用/参考主表的主键或者唯一约束的列(因为被依赖/被参考的值必须是唯一的)
    2. 在创建约束的时候，如果不给外键约束命名，默认的名不是列表，而实自动长生坑一个外键名(例如student_ibfk_1),也可以指定外键约束名
    3. 创建(CREATE)表是就指定外键约束的话，先创建主表，再创建从表。
    4. 删除表时，先删除从表(或者先删除外键约束)，再删除主表.
    5. 当主表的记录内从表参照时，主表的记录将不允许删除，如果要删除数据，需要先删除从表的依赖该记录的数据，然后才可以删除主表的数据
    6. 在"从表"中指定外键约束，并且一个表可以创建多个外键约束.
    7. 从表的外键列与主表被参照的列名字可以不相同，但是数据了类型一定要一样的，逻辑意义时一致的。如果类型不一样，在创建子表的时候，会出现错误
       "ERROR 1005 (HY000):Can't create table 'database.tablename'(error:150)"
    8. 当创建外键约束时，系统默认会在所在的列上简历对于的普通索引。但是索引名是列名，不是外键的约束名。(根据外键查询的效率很高)；
    9. 删除外键约束后，必须手动删除对应的索引.
    
    
例子:
```sql
-- 创建主表
CREATE TABLE IF NOT EXISTS dept(
dept_id INT,
`dept_name` VARCHAR(32)
);

DESC dept;
-- 要创建从表需要将从表的外键和主表的对应，而且外键要是主键或者唯一列
ALTER TABLE dept ADD PRIMARY KEY (dept_id);
-- 创建从表
CREATE TABLE IF NOT EXISTS emp(
emp_id INT PRIMARY KEY,
emp_name VARCHAR(32),
dept_id INT,
CONSTRAINT fk_dept_id FOREIGN KEY (dept_id) REFERENCES dept(dept_id)
)
DESC emp;
-- 查看表的相关的饿索引
SELECT *FROM information_schema.`TABLE_CONSTRAINTS` WHERE table_name='emp'
CREATE TABLE IF NOT EXISTS emp2(
emp_id INT PRIMARY KEY,
emp_name VARCHAR(32),
dept_id INT,
FOREIGN KEY (dept_id) REFERENCES dept(dept_id)
);
SELECT *FROM information_schema.`TABLE_CONSTRAINTS` WHERE table_name='emp2'
-- 需要先增加主表的数据才可以添加从表的数据，否则就无法实现数据的完整性.
INSERT INTO dept(dept_id,dept_name) VALUES (1001,'CEO'),(1002,'人事部'),(1003,'财务部');
-- 添加从表的数据
INSERT INTO emp(emp_id,emp_name,dept_id) VALUES (1,'张三',1001),(2,'李四',1003),(3,'王五',1002);
-- 添加主表没有的外键就会报错Cannot add or update a child row: a foreign key constraint fails (`dbtest13`.`emp`, CONSTRAINT `fk_dept_id` FOREIGN KEY (`dept_id`) REFERENCES `dept` (`dept_id`))
INSERT INTO emp(emp_id,emp_name,dept_id) VALUES (4,'张三',1004)
SELECT *FROM dept;
SELECT *FROM emp;
```
![dept的数据](./files\外键引用-1.PNG)
![emp表的数据](./files\外键引用-2.PNG)
    
* 建表后添加外键
一般情况下，表与表的关联都是提前设计好了的，因此，会在创建表的时候就把外键约束定义好。不
过，如果需要修改表的设计（比如添加新的字段，增加新的关联关系），但没有预先定义外键约束，那
么，就要用修改表的方式来补充定义。
```sql
ALTER TABLE 从表名 ADD [CONSTRAINT 约束名] FOREIGN KEY (从表的字段) REFERENCES 主表名(被引用
字段) [on update xx][on delete xx];
```       
例子:
```sql
CREATE TABLE IF NOT EXISTS emp3(emp_id INT ,emp_name VARCHAR(32),dept_id INT);
DESC emp3;
ALTER TABLE emp3 ADD CONSTRAINT emp3_fk_dept_id FOREIGN KEY (dept_id) REFERENCES dept(dept_id);
-- 查看外键约束
SELECT * FROM information_schema.`TABLE_CONSTRAINTS` WHERE table_name='emp3';
-- 查看索引(MyAQL对外键约束进行会添加一个普通索引，如果需要删除外加你约束，需要我们手动的对外键自动添加的索引进行删除)
SHOW INDEX FROM emp3;
```
![sql查看表的相关外键信息](./files\外键引用-3.PNG)       
![sql查看你索引信息](./files\外键引用-3.PNG)       

### 外键约束等级

* Cascade方式:在父表上update/delete记录时，同步update/delete掉子表的匹配记录
* Set null方式:在父表上update/delete时,将子表上匹配记录的列设置为null，但是要注意子表的外键列不能为not null
* No action方式:如果子表中有匹配的记录，则不允许对富比埃对应候选键进行update/delete操作
* Restrict方式:同no action ,都是立即检查外键约束
* Set default方式(在可视化根据中可能显示空白):父表有变更时，子表将外键列设置成一个默认的值，但是Innodb不能识别。


例子:
```sql
CREATE TABLE IF NOT EXISTS emp3(
emp_id INT PRIMARY KEY,
emp_name VARCHAR(32),
dept_id INT,
CONSTRAINT emp3_fk_dept_id FOREIGN KEY (dept_id) REFERENCES dept(dept_id) ON UPDATE CASCADE ON DELETE SET NULL
);

INSERT INTO emp3 VALUES(1,'zhangsan',1001),(2,'lisi',1002),(3,'wangwu',1003);

SELECT *FROM emp3;

SELECT *FROM dept;

DROP TABLE IF EXISTS emp,emp2;

DROP TABLE IF EXISTS emp3;

DESC emp3;

SELECT * FROM information_schema.`TABLE_CONSTRAINTS` WHERE table_name='emp3';

UPDATE dept SET dept_id=1004 WHERE dept_id=1003;

```
![emp3原数据](./files\约束级别-1.PNG)
![dept原数据](./files\约束级别-2.PNG)
![dept更新后数据](./files\约束级别-3.PNG)
![emp3更新后数据](./files\约束级别-4.PNG)