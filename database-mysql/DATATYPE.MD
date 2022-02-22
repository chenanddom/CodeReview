# 数据类型

|  类型   | 类型举例  |
|  ----  | ----  |
| 整数类型  |TINGINT,SMALLINT,MEDIUMINT,INT(或者INTEGER),BIGINT |
| 浮点型  | FLOAT,DOUBLE  |
| 定点数类型  | DECIMAL |
| 位类型  | BIT |
| 日期时间类型  | YEARA,TIME,DATE,DATETIME,TIMESTAMP |
| 文本字符串类型  | CHAR,VARCHAR,TINYTEXTM,TEXT,MEDIUMTEXT,LONGTEXT |
| 枚举类型  | ENUM |
| 集合类型  | SET |
| 二进制字符串类型 | BINARY,VARBINARY,TINYBLOB,BLOB,MEDUMBLOB,LONGBLOB |
| JSON类型 | JSON对象，JSON数组 |


## 整型类型
| 整型类型   | 存储空间  | 有符号数取值范围 | 无符号数取值范围 |
| ---- | ---- | ---- | ---- |
| TINYINT| 1KB| -128~127 | 0~255 |
| SMALLINT | 2KB | -32768~32767 | 0~65545 |
| MEDIUMINT| 3KB | -8388608~8388607 | 0~16777215 |
| INT,INTEGER | 4KB | -2147483648~2147483647 | 0~429497295 |
| BIGINT| 8KB | -9223372036854775808~9223372036854775807 | 0~18446744073709551615 |

* 可选择属性
M：这是表示显示宽度，M的取值范围是0~255.例如int(5):当数据宽度下雨5位的时候在数据前面需要使用字符填充宽度。该项功能需要配合
"ZEROFILL"使用，表示"0"填充宽度，否则指定显示宽度无效。如果设置了显示宽度，那么插入的数据宽度超过显示宽度限制，会不会阶段或
者插入失败？答案：不会对插入的数据集右任何影响，还是按照类型的实际宽度进行保存，显示宽度与类型可以存储的值范围是无关。从MySQL8.0.17
版本开始，整数的数据类型不推荐使用显示宽度属性。
例子：
```sql
CREATE DATABASE IF NOT EXISTS dbtest13 CHARACTER SET 'utf8';
-- 此处字段f2虽然设置了宽度，但是没有配合zerofill使用，所以宽度无法生效，f3如果位数不够就会使用0填充
CREATE TABLE int_test2(f1 INT,f2 INT(5),f3 INT(5) ZEROFILL);
INSERT INTO int_test2(f3) VALUES(12);
INSERT INTO int_test2(f2) VALUES(655345);
INSERT INTO int_test2(f1,f2,f3) VALUES(1,123,123);
INSERT INTO int_test2(f3)VALUES(12345678);

```
![sql执行结果](./files\数据类型-1.PNG)

* UNSIGNED:无符号类型所有的整数类型都有一个可选的属性UNSIGNED（无符号属性），无
符号整数类型的最小取值为0。所以，如果需要在MySQL数据库中保存非负整数值时，可以将整
数类型设置为无符号类型。

TINYINT ：一般用于枚举数据，比如系统设定取值范围很小且固定的场景。
SMALLINT ：可以用于较小范围的统计数据，比如统计工厂的固定资产库存数量等。
MEDIUMINT ：用于较大整数的计算，比如车站每日的客流量等。
INT、INTEGER ：取值范围足够大，一般情况下不用考虑超限问题，用得最多。比如商品编号。
BIGINT ：只有当你处理特别巨大的整数时才会用到。比如双十一的交易量、大型门户网站点击量、证
券公司衍生产品持仓等。

* **注意:**
在实际工作中，系统故障产生的成本远远超过增加几个字段存储空间所产生的成本。因
此，我建议你首先确保数据不会超过取值范围，在这个前提之下，再去考虑如何节省存储空间。


## 浮点类型
```text
浮点数和定点数类型的特点死可以处理小鼠，你可以把整数堪称小鼠的特例。因此，浮点数和定点数的使用
厂家，逼整数大得多。MySQL支持的浮点数的类型分别是FLOAT,DOUBLE,REAL;
```
* FLOAT表示单精度浮点数 占用四个字节

* DOUBLE表示双精度浮点数 占用八个字节

* REAL默认是DOUBLE。如果你把SQL模式设置启用位"REAL_AS_FLOAT",那么，MySQL就认位REAL是float，如果
要启用"REAL_AS_FLOAT"可以使用如下的SQL进行设置
```sql
SET sql_mode="REAL_AS_FLOAT";
```

* FLOAT和DOUBLE的区别?

* 为什么浮点数类型的无符号数取值范围，只相当于有符号数取值范围的一半，也就是只相当于
  有符号数取值范围大于等于零的部分呢？
  
  MySQL 存储浮点数的格式为： 符号(S) 、尾数(M) 和 阶码(E) 。因此，无论有没有符号，MySQL 的浮
  点数都会存储表示符号的部分。因此， 所谓的无符号数取值范围，其实就是有符号数取值范围大于等于
  零的部分。

    * MySQL允许使用非标准语法(其他数据库未必支持，因此如果涉及到数据迁移，则最好不要这么使用):
    FLOAT(M,D)或者DOUBLE(M,D).这里M成为经定义，D称为标度。(M,D)中，M=整数位+小数位，D=小数位。
    D<=M<=255，0<=D<=30

    * FLOAT和DOUBLE类型在不指定(M,D)时，默认会按照实际的精度(由实际的硬件和操作系统决定)来显示
    * 说明:浮点类型，也可以假UNSIGNED，但是不会改变数据的范围，例如:FLOAT(3,2) UNSIGNED仍然只能表示
    0-9.99的范围
    * 不管是否显式设置精度(M,D),MySQL的处理方案如下:
        * 如果存储时，整数部分超出了范围，MySQL就会报错，不允许存储这样的值
        * 如果存储时，小数部分超出了范围，就分一下的情况：
            * 若四舍五入之后，整数部分没有超出范围，则只是警告，但是能成功操作并四舍五入删除多余的小数位后保存。
            例如FLOAT(5,2)列内插入999.009，近似结果时999.01
            * 若四舍五入后，整数部分超出了范围，则MySQL报错，并拒绝处理，例如FLOAT(5,2)列内插入999.995和-999.995都会报错。

    * 从MySQL 8.0.17开始，FLOAT(M,D) 和DOUBLE(M,D)用法在官方文档中已经明确不推荐使用，将来可
      能被移除。另外，关于浮点型FLOAT和DOUBLE的UNSIGNED也不推荐使用了，将来也可能被移除。


* 注意:
浮点数类型有个缺陷，就是不精准。下面我来重点解释一下为什么 MySQL 的浮点数不够精准。比如，我
们设计一个表，有f1这个字段，插入值分别为0.47,0.44,0.19，我们期待的运行结果是：0.47 + 0.44 + 0.19 =
1.1
```sql
CREATE TABLE float_test4(
f1 DOUBLE
)
INSERT INTO float_test4 VALUES(0.47),(0.44),(0.19); 
SELECT SUM(f1) FROM float_test4;

```
![上面SQL执行结果](./files\数据类型-2.PNG)

结论:
```text
查询结果是 1.0999999999999999。看到了吗？虽然误差很小，但确实有误差。 你也可以尝试把数据类型
改成 FLOAT，然后运行求和查询，得到的是， 1.0999999940395355。显然，误差更大了。
那么，为什么会存在这样的误差呢？问题还是出在 MySQL 对浮点类型数据的存储方式上。
MySQL 用 4 个字节存储 FLOAT 类型数据，用 8 个字节来存储 DOUBLE 类型数据。无论哪个，都是采用二
进制的方式来进行存储的。比如 9.625，用二进制来表达，就是 1001.101，或者表达成 1.001101×2^3。如
果尾数不是 0 或 5（比如 9.624），你就无法用一个二进制数来精确表达。进而，就只好在取值允许的范
围内进行四舍五入。
在编程中，如果用到浮点数，要特别注意误差问题，因为浮点数是不准确的，所以我们要避免使用“=”来
判断两个数是否相等。同时，在一些对精确度要求较高的项目中，千万不要使用浮点数，不然会导致结
果错误，甚至是造成不可挽回的损失。那么，MySQL 有没有精准的数据类型呢？当然有，这就是定点数
类型： DECIMAL 。
```
## 定点数类型
* MySQL中的定点疏忽类型只有DECIMAL一种类型。DECIMAL(M,D)占用M+2字节
* 使用 DECIMAL(M,D) 的方式表示高精度小数。其中，M被称为精度，D被称为标度。0<=M<=65，
  0<=D<=30，D<M。例如，定义DECIMAL（5,2）的类型，表示该列取值范围是-999.99~999.99。
* DECIMAL(M,D)的最大取值范围与DOUBLE类型一样，但是有效的数据范围是由M和D决定的。
  DECIMAL 的存储空间并不是固定的，由精度值M决定，总共占用的存储空间为M+2个字节。也就是
  说，在一些对精度要求不高的场景下，比起占用同样字节长度的定点数，浮点数表达的数值范围可
  以更大一些。
* 定点数在MySQL内部是以字符串的形式进行存储，这就决定了它一定是精准的。

*　当DECIMAL类型不指定精度和标度时，其默认为DECIMAL(10,0)。当数据的精度超出了定点数类型的
 精度范围时，则MySQL同样会进行四舍五入处理。

例子：
```sql
create table float_test5(c1 decimal,c2 decimal(5,2));
DESC float_test5;
insert into float_test5 values(123.123,123.123);
select *from float_test5;
```
![SQL执行结果](./files\数据类型-3.PNG)

## 位类型
BIT类型中存储的时二进制值，类似010110

|二进制字符串类型|长度|长度范围|占用空间|
| ---- | ---- | ---- | ---- |
|BIT(M)|M|1<=M<=64 |约为(M+7)/8各字节|

```sql
create table bit_test1(
f1 bit,
f2 bit(5),
f3 bit(64)
);
desc bit_test1;
insert into bit_test1(f1) values(1),(0);
insert into bit_test1(f1) value(2);
insert into bit_test1(f2) values (23);
-- 此处如果需要以十进制显示，那么就可以在字段后面+0
select f1+0,f2+0 from bit_test1;
```
![sql执行结果](./files\数据类型-4.PNG)

## 日期类型

|类型|名称|字节|日期格式|最小值|最大值|
| ---- | ---- | ---- | ---- | ---- | ---- |
|YEAR|年|1|YYYY或YY|1901|2155|
|TIME|时间|3|HH:MM:SS|-838:59:59|83859:59:59|
|DATE|日期|3|YYYY-MM-DD|1000-01-01|9999-12-03|
|DATETIME|日期时间|8|YYYY-MM-DD HH:MM:SS|1000-01-01 00:00:00|9999-12-31 23:59:59|
|TIMESTAMP|日期时间|4|YYYY-MM-DD HH:MM:SS|1970-01-01 00:00:00 UTC|2038-01-19 03:14:07UTC|

* YEAR 类型
YEAR类型时用来表示年分的，在所有的日期时间类型中档所占用的存储空间最小，只需要1字节的存储空间。
在MySQL中，YEAR有一下的几种存储格式
    * 以4位字符串或者数字格式表示YEAR类型其格式位YYYY,最小值位1901，最大值为2155
    * 以2位字符串格式表示YEAR类型，最小值时00，最大值时99
        * 当取值位01到69时，表示2001打拍2069
        * 当取值位70到99时，表示1970到1999
        * 当取值整数的0或者00的化，那么就是0000年
        * 当取值时日期/字符串的'0'添加的化，是2000年
**注意:**
从MySQL5.5.27开始，2位格式的YEAR已经不推荐使用。YEAR默认格式就是“YYYY”，没必要写成YEAR(4)，
从MySQL 8.0.19开始，不推荐使用指定显示宽度的YEAR(4)数据类型。 

例子:
```sql
CREATE TABLE date_time_test1(
f1 year,
f2 year(4)
);

DESC date_time_test1;

INSERT INTO date_time_test1 VALUES ('2021','2022')
-- 70 代表1970年，69代表了2069年
insert into date_time_test1 values (70,69);
-- 整数的0代表了0000年，‘0’表示2000年
INSERT INTO date_time_test1 VALUES (0,'0');

select *from date_time_test1;
```
![sql执行结果](./files\数据类型-5.PNG)

* DATE类型
DATE类型表示日期，没有时间的部分，格式位YYYY-MM-DD，分别表示年月日。日期需要3个字节存储空间.在项Date类型的饿字段中插入数据时，同样
需要满足一定的格式条件。
    * 以YYYY-MM-DD格式或者YYYYMMDD格式表示的字符串日期，其自小取值位1000-01-01,最大的取值位9999-12-03.YYYYMMDD格式会被转换位YYYY-MM-DD格式
    * 以YY-MM-DD格式或者YYMMDD格式表示的字符串日期，此格式中，年份位两位数值或者字符串满足的YEAR类型的的格式条件为:：当年份取值为00到69时，会被转
    化为2000到2069；当年份取值为70到99时，会被转化为1970到1999。
    * 使用CURRENT_DATE() 或者NOW() 函数，会插入当前系统的日期。

```sql
CREATE TABLE date_time_test2(f1 DATE);

DESC date_time_test2;

INSERT INTO date_time_test2 VALUES('2022-02-22'),('20220222'),(20220222);

INSERT INTO date_time_test2 VALUES ('00-02-22'),('69-02-22'),('70-02-22'),('000222'),('700222'),('990222')

SELECT *FROM date_time_test2;
```
![sql执行结果](./files\数据类型-6.PNG)

* DATETIME
DATETIME类型在所有的日期时间类型中占用的存储空间最大，总共需要8 个字节的存储空间。在格式上
为DATE类型和TIME类型的组合，可以表示为YYYY-MM-DD HH:MM:SS ，其中YYYY表示年份，MM表示月
份，DD表示日期，HH表示小时，MM表示分钟，SS表示秒。
在向DATETIME类型的字段插入数据时，同样需要满足一定的格式条件。
    * 以YYYY-MM-DD HH:MM:SS 格式或者YYYYMMDDHHMMSS 格式的字符串插入DATETIME类型的字段时，
      最小值为1000-01-01 00:00:00，最大值为9999-12-03 23:59:59。
        * 以YYYYMMDDHHMMSS格式的数字插入DATETIME类型的字段时，会被转化为YYYY-MM-DD
          HH:MM:SS格式。
        * 使用函数CURRENT_TIMESTAMP() 和NOW() ，可以向DATETIME类型的字段插入系统的当前日期和
          时间。  

例子:

```sql
CREATE TABLE IF NOT EXISTS date_time_test3(f1 DATETIME);

DESC date_time_test3;

INSERT INTO date_time_test3 VALUES('2022-02-22 17:45:50'),('20220222174550')

INSERT INTO date_time_test3 VALUES ('99-02-22 17:45:50'),('990222174550'),('69-02-22 17:45:50'),('690222174550');

INSERT INTO date_time_test3 VALUES (CURDATE()),(NOW()),(CURRENT_TIMESTAMP());

SELECT *FROM date_time_test3;
```
![datetime类型sql执行结果](./files\数据类型-7.PNG)


* TIMESTAMP类型
TIMESTAMP类型也可以表示日期时间，其显示格式与DATETIME类型相同，都是YYYY-MM-DD
HH:MM:SS ，需要4个字节的存储空间。但是TIMESTAMP存储的时间范围比DATETIME要小很多，只能存储
“1970-01-01 00:00:01 UTC”到“2038-01-19 03:14:07 UTC”之间的时间。其中，UTC表示世界统一时间，也叫
作世界标准时间
    * 存储数据的时候需要对当前时间所在的时区进行转换，查询数据的时候再将时间转换回当前的时
      区。因此，使用TIMESTAMP存储的同一个时间值，在不同的时区查询时会显示不同的时间。
向TIMESTAMP类型的字段插入数据时，当插入的数据格式满足YY-MM-DD HH:MM:SS和YYMMDDHHMMSS
时，两位数值的年份同样符合YEAR类型的规则条件，只不过表示的时间范围要小很多。
如果向TIMESTAMP类型的字段插入的时间超出了TIMESTAMP类型的范围，则MySQL会抛出错误信息。

例子:
```sql
create table if not exists date_time_test4(
ts timeStamp
);
desc date_time_test4

insert into date_time_test4 values ('2022-02-22 17:45:50'),('20220222174550'),('70-02-22 17:45:50'),('700222174550')

INSERT INTO date_time_test4 VALUES ('990101030405');

select *from date_time_test4;
```
![timestamp类型sql执行结果](./files\数据类型-8.PNG)

*  TIMESTAMP和DATETIME的区别：
TIMESTAMP存储空间比较小，表示的日期时间范围也比较小
底层存储方式不同，TIMESTAMP底层存储的是毫秒值，距离1970-1-1 0:0:0 0毫秒的毫秒值。
两个日期比较大小或日期计算时，TIMESTAMP更方便、更快。
TIMESTAMP和时区有关。TIMESTAMP会根据用户的时区不同，显示不同的结果。而DATETIME则只能
反映出插入时当地的时区，其他时区的人查看数据必然会有误差的。
例子:

```sql

```






用得最多的日期时间类型，就是 DATETIME 。虽然 MySQL 也支持 YEAR（年）、 TIME（时间）、
DATE（日期），以及 TIMESTAMP 类型，但是在实际项目中，尽量用 DATETIME 类型。因为这个数据类型
包括了完整的日期和时间信息，取值范围也最大，使用起来比较方便。毕竟，如果日期时间信息分散在
好几个字段，很不容易记，而且查询的时候，SQL 语句也会更加复杂。



































