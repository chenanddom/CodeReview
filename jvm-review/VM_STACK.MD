# 虚拟机栈
```text
为了涉及跨平台，JAVA的指令集都是根据栈来设计实现的。不同的CPU架构不同。所以不能设计为基于寄存器的。
基于栈的架构的优点：跨平台，指令集小，编译实现起来容易，缺点是性能下降，实现同样的过年需要更多的指令。
```

* 问题：
  * 有不少Java开发人员一提到Java内存结构，就会非常粗粒度地将JVM中的内存区理解为仅有Java堆（heap）和Java栈（stack）？为什么？
    ```text
    受到C/C++的影响，这两种高级语言是将内存管理分为堆和栈的，同时也体现出了堆区和栈区在VM内存管理的重要性。首先栈是运行时的单位，而堆是存储的单位
    栈解决程序的运行问题，即程序如何执行，或者说如何处理数据。 堆解决的是数据存储的问题，即数据怎么放，放哪里
    ```
  * Java虚拟机栈是什么？
    ```text
    Java虚拟机栈也叫Java栈。每个线程创建的时候都会创建一个虚拟机栈，其内部保存了一个个栈帧，对应着一次次的Java方法调用。是线程私有的，生命周期和
    线程的生命周期是一致的，它的主要作用主要是保存方法的局部变量、部分结果，并参与方法的调用与结果的返回    
    ```
  * 栈的特点
    ```text
    栈是一种快速有效的分配存储的方式，访问速度仅次于寄存器的速度，每次方法的访问伴随着压栈和出栈，程序执行结束就会出栈，栈不存在垃圾手机的问题
    ```


示例：
```java
/**
 * 测试栈设置栈大小-Xss256k
 * 默认情况打印到11421
 * 设置了虚拟机器参数之后打印数据就到了2462，说明虚拟机参数设置生效了.
 */
public class StackTest {
    public static int num=1;
    public static void main(String[] args) {
        System.out.println(num);
        num++;
        main(args);
    }
}
出现错误：
Exception in thread "main" java.lang.StackOverflowError

```

* 关于Slot的理解
  * JVM会为局部变量表中的每个Slot都分配一个访问索引，通过这个索引即可访问到局部变量表中知道的局部变量值。
  * 一个方法被调用的时候，它的的方法参数和方法内定义的局部变量都会被按顺序复制到局部变量表的Slot
  * 如果需要访问局部变量表中一个64bit的局部变量值时，只需要使用前一个索引即可。(比如：访问long或者double类型变量)
  * 如果当前帧是由构造方法或者示例方法创建的，那么盖对象引用this将会存放在index为0的slot处，其余的参数按照参数表顺序继续排列。


* Slot的重复利用
  * 栈帧中的局部变量表中的槽位是可以重复用的，如果一个局部变量过了其作用域，那么在其作用域之后生命的新的局部变量就很有可能会复用国企局部变量的槽位，达到节约资源的目的。
    ```java
         public class ReuseSlotTest {
         public void method1(){
             int a=0;
             System.out.println(a);
             int b=0;
         }
         public void method2(){
             {
                 int a=0;
                 System.out.println(a);
             }
             int b=0;
         }
               
         public static void main(String[] args) {
             new ReuseSlotTest().method2();
         }
       }

    ```
* 静态变量与局部变量的对比
```markdown
* 参数分配完毕之后，再恩据方法体内定义的变量的顺序和作用域分配
* 我们知道类变量表有两次初始化的机会，一次是在加载后的"准备"阶段，这个阶段主要进行零值的初始化操作，第二个阶段主要是"初始化"阶段，这个阶段才是正在的
  将我们想赋的值进行赋给了变量。
* 和类变量不同的是局部变量一旦定义了就必须人为的去进行初始化操作，否则无法正常的使用。
```
示例
```java
public class LocalVariableTest {
public void localVariableMethod(){
    int a = 0;//此处不赋值无法正常使用。
    System.out.println(a);//此处使用会报错
}
}

```


















































































