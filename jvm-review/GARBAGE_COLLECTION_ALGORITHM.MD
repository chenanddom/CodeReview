# 垃圾回收算法

## 判断对象存活一般有两种方式：引用计数算法和可达性分析算法。

### 引用计数法
```text
引用计数算法（Reference Counting）比较简单，对每个对象保存一个整型的引用计数器属性。用于记录对象被引用的情况。

对于一个对象A，只要有任何一个对象引用了A，则A的引用计数器就加1；当引用失效时，引用计数器就减1。只要对象A的引用计数器的值为0，即表示对象A不可能再被使用，可进行回收。

优点：实现简单，垃圾对象便于辨识；判定效率高，回收没有延迟性。

缺点：它需要单独的字段存储计数器，这样的做法增加了存储空间的开销。

每次赋值都需要更新计数器，伴随着加法和减法操作，这增加了时间开销。 引用计数器有一个严重的问题，即无法处理循环引用的情况。这是一条致命缺陷，导致在Java的垃圾回收器中没有使用这类算法。
```
例子:
[RefCountGC.java](./src/main/java/com/itdom/jvm/chapter15/RefCountGC.java)
执行上面的代码得到如下的运行结果：
* 没有运行System.gc();
```shell script
Heap
 PSYoungGen      total 76288K, used 16794K [0x000000076b380000, 0x0000000770880000, 0x00000007c0000000)
  eden space 65536K, 25% used [0x000000076b380000,0x000000076c3e6838,0x000000076f380000)
  from space 10752K, 0% used [0x000000076fe00000,0x000000076fe00000,0x0000000770880000)
  to   space 10752K, 0% used [0x000000076f380000,0x000000076f380000,0x000000076fe00000)
 ParOldGen       total 175104K, used 0K [0x00000006c1a00000, 0x00000006cc500000, 0x000000076b380000)
  object space 175104K, 0% used [0x00000006c1a00000,0x00000006c1a00000,0x00000006cc500000)
 Metaspace       used 3297K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 359K, capacity 388K, committed 512K, reserved 1048576K
```
*　运行System.gc
```shell script
[GC (System.gc()) [PSYoungGen: 15482K->744K(76288K)] 15482K->752K(251392K), 0.0009496 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[Full GC (System.gc()) [PSYoungGen: 744K->0K(76288K)] [ParOldGen: 8K->616K(175104K)] 752K->616K(251392K), [Metaspace: 3299K->3299K(1056768K)], 0.0038904 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
Heap
 PSYoungGen      total 76288K, used 1966K [0x000000076b380000, 0x0000000770880000, 0x00000007c0000000)
  eden space 65536K, 3% used [0x000000076b380000,0x000000076b56b9e0,0x000000076f380000)
  from space 10752K, 0% used [0x000000076f380000,0x000000076f380000,0x000000076fe00000)
  to   space 10752K, 0% used [0x000000076fe00000,0x000000076fe00000,0x0000000770880000)
 ParOldGen       total 175104K, used 616K [0x00000006c1a00000, 0x00000006cc500000, 0x000000076b380000)
  object space 175104K, 0% used [0x00000006c1a00000,0x00000006c1a9a190,0x00000006cc500000)
 Metaspace       used 3311K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 360K, capacity 388K, committed 512K, reserved 1048576K
```