# 类加载子系统
![类加载子系统](./files/类加载体系-1.png)

* 类加载器的相关概念
  * 类加载器子系统负责从文件系统或者网络加载Class文件，Class文件在文件开头有特定的饿文件标识。
  * ClassLoader只负责class文件的加载，制语它是否可以运行，则有Execution Engine决定
  * 加载的类信息存放于一块称为方法去的内存空间。除了类的信息外，方法区中还会存放运行时的常量池信息，可能包括字符串字面量和数字常量(这部分常量信息
    是Class文件中常量池部分的内存映射)
* 类加载器ClassLoader角色
  * class file存在于本地硬盘上，可以理解未设计师在之上的模板，而最终在这个模板在执行的时候要加载到JVM当中来根据这个文件实例化出n个一模一样的实例
  * class file加载到JVM中，被称为DNA元数据模板，放在方法区
  * 在.class文件->JVM->最终为元数据模板，此过程就要一个运输工具(类加载其 Class Loader)扮演一个快递元的角色
  
![类加载器ClassLoader角色](./files/类加载器的角色-1.png)


* 类的加载过程
    * 验证(Verify) 
       * 目的在于确保Class文件的字节流中包含信息符合当前虚拟机要求，保障被加载的类的正确性，不会危害虚拟机自身的安全。
       * 主要包括四种验证，文件格式验证，元数据验证，字节码验证，符号引用验证。
    * 准备(Prepare)
       * 为类变量分配内存并且设置该类变量的默认初始化值，即零值
       * 这里不包含使用final修饰的static，因为final在编译的时候会分配了，准备阶段会显示的初始化
       * 在理不会为实例变量分配初始化，类变量会分配在方法区中，而实例变量是会随着对象一起分配到Java堆中
    * 解析(Resolve)
       * 将常量池内的符号引用转换为直接引用的过程
       * 事实上，戒心操作王五会伴随这JVM在执行完成之后在执行
       * 符号引用就是一组符号来表述所引用的目标，符号引用的字面量形式明确定义在《java虚拟机规范》的Class文件格式中。直接引用就是直接指向目标
          的指针、相对偏移量或者一个建姐定位到目标的句柄
       * 戒心动作主要针对类或者接口、字段、类方法、接口方法，方法类型等，对应常量池中的CONSTANT_Class_info、CONSTANT_Fieldref_info、
         CONSTANT_Methodref_info等
![类的加载过程](.files/类加载的过程-1.png)


```java
public class CustomerClazz {
    public static int n=1;//prepare环节只会赋值为0值--->initing(初始化阶段)才将n=1
    static {
        n=20;
    }


    public static void main(String[] args) {
        System.out.println(n);
    }
}
```




































































  

