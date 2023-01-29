## PC寄存器
```markdown
JVM中的程序计数器(Program Counter Register)中，Register的命名源于CPU的寄存器，寄存器存储之类相关的现场信息，
CPU只有把数据装在到寄存器才能够运行。(这里的PC寄存器只是对实际的物理寄存器的一个抽象，也称为程序钩子)

```
* 作用:用来存储指向下一天指令的地址，也即将要执行的指令diamagnetic。有执行引擎读取下一条指令。

[PC寄存器](https://app.diagrams.net/#Hchenanddom%2FCharts%2Fmain%2Frepo%2FJVM%E8%BF%90%E8%A1%8C%E6%97%B6%E6%95%B0%E6%8D%AE%E5%8C%BA.drawio)
![PC寄存器](./files/PC寄存器.png)


PC寄存器的解释：

```text
PC寄存器时一小块不起眼的内存，但是，它的执行速度时非常的快的，它是线程私有的，每个线程都有自己的PC寄存器，任何线程都有且只有一个方法在执行，也就是
栈里面的当前方法，程序计数器会存储线程执行的方法的地址，但是，如果是native方法，则是未指定值(undefined)
```